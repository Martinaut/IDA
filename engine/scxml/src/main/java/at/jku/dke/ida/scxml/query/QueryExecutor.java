package at.jku.dke.ida.scxml.query;

import at.jku.dke.ida.scxml.configuration.SCXMLConfig;
import at.jku.dke.ida.shared.display.ResultErrorDisplay;
import at.jku.dke.ida.shared.display.WaitMessageDisplay;
import at.jku.dke.ida.shared.session.SessionModel;
import at.jku.dke.ida.shared.spring.BeanUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
    private final ObjectMapper objectMapper;

    /**
     * Instantiates a new instance of class {@linkplain QueryExecutor}.
     *
     * @param config       The endpoint configuration.
     * @param objectMapper The object mapper.
     */
    @Autowired
    public QueryExecutor(SCXMLConfig config, ObjectMapper objectMapper) {
        this.config = config;
        this.objectMapper = objectMapper;
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
        LOG.debug("Sending analysis situation");

        String json = objectMapper.writeValueAsString(model.getAnalysisSituation());
        String jsonLd = AnalysisSituationConverter.convertAnalysisSituationToJsonLD(model.getAnalysisSituation());

        // Build request
        String jsonBody = new StringBuilder()
                .append("{\"json-ld\": {")
                .append(jsonLd)
                .append("}, \"json\": {")
                .append(json)
                .append("}}")
                .toString();

        RequestBody requestBody = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(this.config.getQueryEndpoint())
                .post(requestBody)
                .addHeader("Accept", CSV.toString())
                .build();

        // Send request
        LOG.debug("Sending post request to {}.", request.url());
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful())
                throw new IOException("Unexpected HTTP status code: " + response);

            ResponseBody body = response.body();
            if (body == null)
                throw new IOException("Body is null");

            return body.string();
        }
    }
}
