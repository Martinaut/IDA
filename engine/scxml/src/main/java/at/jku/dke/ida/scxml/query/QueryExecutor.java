package at.jku.dke.ida.scxml.query;

import at.jku.dke.ida.scxml.configuration.SCXMLConfig;
import at.jku.dke.ida.shared.display.ErrorDisplay;
import at.jku.dke.ida.shared.display.MessageDisplay;
import at.jku.dke.ida.shared.session.SessionModel;
import at.jku.dke.ida.shared.spring.BeanUtil;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * A service that sends requests to the query interface to retrieve the result for the current analysis situation.
 */
@Service
public class QueryExecutor {

    private static final Logger LOG = LogManager.getLogger(QueryExecutor.class);
    private static final MediaType CSV = MediaType.get("text/csv");
    private static final MediaType JSON = MediaType.get("application/json");

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
        RequestBody body = RequestBody.create(JSON, "{}"); // TODO: which format??
        Request request = new Request.Builder()
                .url(config.getQueryEndpoint())
                .post(body)
                .addHeader("Accept", CSV.toString())
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
