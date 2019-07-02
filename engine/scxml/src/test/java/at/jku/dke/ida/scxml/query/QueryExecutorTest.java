package at.jku.dke.ida.scxml.query;

import at.jku.dke.ida.scxml.configuration.SCXMLConfig;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;
import org.junit.jupiter.api.Test;

class QueryExecutorTest {
    @Test
    void testExecuteQuery() {
        NonComparativeAnalysisSituation as = new NonComparativeAnalysisSituation();
        as.setCube("http://CUBE");
        as.addMeasure("http://MEASURE/1");
        as.addMeasure("http://MEASURE/2");

        SessionModel sessionModel = new SessionModel("a", "en");
        sessionModel.setAnalysisSituation(as);

        SCXMLConfig config = new SCXMLConfig();
        config.setQueryEndpoint("http://localhost:8080/inga-olap/");
        QueryExecutor executor = new QueryExecutor(config);
        executor.executeQuery(sessionModel);
    }
}
