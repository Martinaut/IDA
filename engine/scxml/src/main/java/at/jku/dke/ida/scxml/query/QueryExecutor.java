package at.jku.dke.ida.scxml.query;

import at.jku.dke.ida.scxml.configuration.SCXMLConfig;
import at.jku.dke.ida.shared.display.ResultErrorDisplay;
import at.jku.dke.ida.shared.display.WaitMessageDisplay;
import at.jku.dke.ida.shared.session.SessionModel;
import at.jku.dke.ida.shared.spring.BeanUtil;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static at.jku.dke.ida.scxml.query.AnalysisSituationConverter.convertAnalysisSituationToJsonLD;

/**
 * A service that sends requests to the query interface to retrieve the result for the current analysis situation.
 */
@Service
public class QueryExecutor {

    private static final Logger LOG = LogManager.getLogger(QueryExecutor.class);
    private static final MediaType CSV = MediaType.get("text/csv");
    private static final MediaType JSON = MediaType.get("application/ld-json");

    private final SCXMLConfig config;
    private final OkHttpClient client;

    /**
     * Instantiates a new instance of class {@linkplain QueryExecutor}.
     *
     * @param config The endpoint configuration.
     */
    @Autowired
    public QueryExecutor(SCXMLConfig config) {
        this.config = config;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Executes the query.
     *
     * @param model The session model.
     * @return CSV-data returned by the query endpoint or {@code null} if an error occurred
     */
    public String executeQuery(SessionModel model) {
        LOG.info("Executing query");
        model.setDisplayData(new WaitMessageDisplay("pleaseWaitQuery", model.getLocale()));

        if (!model.getAnalysisSituation().isExecutable()) {
            model.setDisplayData(new ResultErrorDisplay("errorNotExecutable", model.getLocale()));
            return null;
        }

        String csv;
        try {
            if (config.getQueryEndpoint().equals(QueryMockExecutor.MOCK_URL)) {
                LOG.info("Using mock executor");
                csv = BeanUtil.getBean(QueryMockExecutor.class).sendRequest(model);
            } else {
                LOG.info("Using HTTP executor");
                csv = sendRequest(model);
            }
        } catch (IOException ex) {
            LOG.error("Could not load result from query endpoint.", ex);
            return null;
        }

        return csv;
    }

    /**
     * Sends a request to the query endpoint and returns the result as csv.
     *
     * @param model The session model.
     * @return CSV received from endpoint
     * @throws IOException If an error occurred while executing the query.
     */
    private String sendRequest(SessionModel model) throws IOException {
        // Send Analysis Situation
        String sitName = sendAnalysisSituation(model);

        // Request result
        return requestResult(sitName);
    }

    /**
     * Sends the analysis situation to the query backend.
     *
     * @param model The session model.
     * @return The situation name.
     * @throws IOException If an error occurred while sending the analysis situation.
     */
    private String sendAnalysisSituation(SessionModel model) throws IOException {
        final String sessionId = "urn:ida:" + model.getSessionId();
        final String situationName = "IDA Analysis Situation: " + sessionId + '_' + UUID.randomUUID();

        LOG.debug("Sending analysis situation '{}' ", situationName);
        post(config.getQueryEndpoint() + String.format("analysis/situations/%s/add", sessionId),
                convertAnalysisSituationToJsonLD(situationName, model.getAnalysisSituation(), () -> {
                    try {
                        return requestAnalysisSituationIRI();
                    } catch (IOException e) {
                        return "urn:uuid:" + UUID.randomUUID();
                    }
                }));

        return situationName;
    }

    /**
     * Requests a new Analysis Situation IRI from the backend.
     *
     * @return new IRI
     * @throws IOException If an error occurred while requesting the IRI.
     */
    private String requestAnalysisSituationIRI() throws IOException {
        Request request = new Request.Builder()
                .url(config.getQueryEndpoint() + "analysis/situations/new")
                .get()
                .build();

        LOG.debug("Sending get IRI request to {}.", request.url());
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected HTTP status code: " + response);
            if (response.body() == null) throw new IOException("Body is null");
            return response.body().string();
        }
    }

    /**
     * Sends a post request to the query backend.
     *
     * @param url         The URL.
     * @param bodyContent The content of the post-content (json-format)
     * @throws IOException If an error occurred while executing the request.
     */
    private void post(String url, String bodyContent) throws IOException {
        RequestBody body = RequestBody.create(JSON, bodyContent);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        LOG.debug("Sending post request to {}.", request.url());
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected HTTP status code: " + response);
        }
    }

    /**
     * Requests the result of the created analysis situation.
     *
     * @param situationName The name of the situation.
     * @return The CSV-result.
     * @throws IOException If an error occurred while executing the query.
     */
    private String requestResult(String situationName) throws IOException {
        Request request = new Request.Builder()
                .url(config.getQueryEndpoint() + "analysis/situations/" + situationName + ".csv")
                .get()
                .addHeader("Accept", CSV.toString())
                .build();

        LOG.debug("Sending result request to {}.", request.url());
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected HTTP status code: " + response);
            if (response.body() == null) throw new IOException("Body is null");
            // TODO if (!CSV.equals(response.body().contentType())) throw new IOException("Invalid content type");
            return response.body().string();
        }
    }
}
