package at.jku.dke.ida.scxml.query;

import at.jku.dke.ida.scxml.configuration.SCXMLConfig;
import at.jku.dke.ida.shared.display.ErrorDisplay;
import at.jku.dke.ida.shared.display.MessageDisplay;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;
import at.jku.dke.ida.shared.spring.BeanUtil;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.core.JsonLdUtils;
import com.github.jsonldjava.core.RDFDataset;
import com.github.jsonldjava.utils.JsonUtils;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

/**
 * A service that sends requests to the query interface to retrieve the result for the current analysis situation.
 */
@Service
public class QueryExecutor {

    private static final Logger LOG = LogManager.getLogger(QueryExecutor.class);
    private static final MediaType CSV = MediaType.get("text/csv");
    private static final MediaType JSON = MediaType.get("application/ld+json");

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
        this.client = new OkHttpClient();
    }

    /**
     * Executes the query.
     *
     * @param model The session model.
     * @return CSV-data returned by the query endpoint or {@code null} if an error occurred
     */
    public String executeQuery(SessionModel model) {
        LOG.info("Executing query");
        model.setDisplayData(new MessageDisplay("pleaseWaitQuery", model.getLocale()));

        if (!model.getAnalysisSituation().isExecutable()) {
            model.setDisplayData(new ErrorDisplay("errorNotExecutable", model.getLocale()));
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
        String asIRI = sendAnalysisSituation(model);

        // Request result
        return requestResult(asIRI);
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

        LOG.debug("Sending request to {}.", request.url());
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected HTTP status code: " + response);
            if (response.body() == null) throw new IOException("Body is null");
            return response.body().string();
        }
    }

    /**
     * Sends the analysis situation to the backend.
     *
     * @param model The session model.
     * @return The IRI of the created analysis situation.
     * @throws IOException If an error occurred while sending the analysis situation.
     */
    private String sendAnalysisSituation(SessionModel model) throws IOException {
        String asIRI = requestAnalysisSituationIRI();

        RequestBody body = RequestBody.create(JSON, createJsonRequestBody(asIRI, model));
        Request request = new Request.Builder()
                .url(config.getQueryEndpoint() + String.format("analysis/situations/ida_%s/add", model.getSessionId()))
                .post(body)
                .build();

        LOG.debug("Sending request to {}.", request.url());
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected HTTP status code: " + response);
        }
        return asIRI;
    }

    private String createJsonRequestBody(String asIRI, SessionModel model) {
        if (!(model.getAnalysisSituation() instanceof NonComparativeAnalysisSituation)) return "";
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) model.getAnalysisSituation();

        RDFDataset dataset = new RDFDataset();
        dataset.setNamespace("http://dke.jku.at/inga/cubes#", "qbx");
        dataset.setNamespace("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf");

        dataset.addTriple(asIRI, "rdf:type", "qbx:AnalysisSituation");
        as.getMeasures().forEach(m -> dataset.addTriple(asIRI, "qbx:hasMeasure", m));

        Object result = JsonLdProcessor.fromRDF(dataset);
        try {
            return JsonUtils.toPrettyString(result);
        } catch (IOException ex) {
            LOG.error("Could not format JSON-LD.", ex);
            return "";
        }
    }

    /**
     * Requests the result of the created analysis situation.
     *
     * @param asIRI The IRI of the analysis situation.
     * @return The CSV-result.
     * @throws IOException If an error occurred while executing the query.
     */
    private String requestResult(String asIRI) throws IOException {
        Request request = new Request.Builder()
                .url(config.getQueryEndpoint() + asIRI + ".csv")
                .get()
                //.addHeader("Accept", CSV.toString())
                .build();

        LOG.debug("Sending request to {}.", config.getQueryEndpoint());
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected HTTP status code: " + response);
            if (response.body() == null) throw new IOException("Body is null");
            // TODO: check content type
            return response.body().string();
        }
    }
}
