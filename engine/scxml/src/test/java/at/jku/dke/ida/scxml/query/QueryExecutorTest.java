package at.jku.dke.ida.scxml.query;

import at.jku.dke.ida.scxml.configuration.SCXMLConfig;
import at.jku.dke.ida.shared.models.DimensionQualification;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class QueryExecutorTest {
    @Test
    void testExecuteQuery() {
        NonComparativeAnalysisSituation as = new NonComparativeAnalysisSituation();
        as.setCube("http://www.example.org/jku/dke/foodmart#CubeSales");
        as.addMeasure("http://www.example.org/jku/dke/foodmart#CubeSalesMeasureUnitSales");
        as.addMeasure("http://www.example.org/jku/dke/foodmart#CubeSalesMeasureSalesCount");

        DimensionQualification dqProduct = new DimensionQualification("http://www.example.org/jku/dke/foodmart#DimensionProduct");
        dqProduct.setGranularityLevel("http://www.example.org/jku/dke/foodmart#DimensionProductLevelProductCategory");
        dqProduct.setDiceLevel("http://www.example.org/jku/dke/foodmart#DimensionProductLevelProductFamily");
        dqProduct.setDiceNode("http://www.example.org/jku/dke/foodmart#ProductFamilyLevelMemberDrink");
        as.addDimensionQualification(dqProduct);

        DimensionQualification dqStore = new DimensionQualification("http://www.example.org/jku/dke/foodmart#DimensionStore");
        dqStore.setGranularityLevel("http://www.example.org/jku/dke/foodmart#DimensionStoreLevelStoreState");
        as.addDimensionQualification(dqStore);

        SessionModel sessionModel = new SessionModel("a", "en");
        sessionModel.setAnalysisSituation(as);

        SCXMLConfig config = new SCXMLConfig();
        config.setQueryEndpoint("http://localhost:8080/");
        QueryExecutor executor = new QueryExecutor(config, new ObjectMapper());
        System.out.println(executor.executeQuery(sessionModel));
    }


    @Test
    void testDeserialization() throws JsonProcessingException {
        // Arrange
        NonComparativeAnalysisSituation as = new NonComparativeAnalysisSituation();
        as.setCube("http://www.example.org/jku/dke/foodmart#CubeSales");
        as.addMeasure("http://www.example.org/jku/dke/foodmart#CubeSalesMeasureUnitSales");
        as.addMeasure("http://www.example.org/jku/dke/foodmart#CubeSalesMeasureSalesCount");

        DimensionQualification dqProduct = new DimensionQualification("http://www.example.org/jku/dke/foodmart#DimensionProduct");
        dqProduct.setGranularityLevel("http://www.example.org/jku/dke/foodmart#DimensionProductLevelProductCategory");
        dqProduct.setDiceLevel("http://www.example.org/jku/dke/foodmart#DimensionProductLevelProductFamily");
        dqProduct.setDiceNode("http://www.example.org/jku/dke/foodmart#ProductFamilyLevelMemberDrink");
        as.addDimensionQualification(dqProduct);

        DimensionQualification dqStore = new DimensionQualification("http://www.example.org/jku/dke/foodmart#DimensionStore");
        dqStore.setGranularityLevel("http://www.example.org/jku/dke/foodmart#DimensionStoreLevelStoreState");
        as.addDimensionQualification(dqStore);

        ObjectMapper om = new ObjectMapper();

        // Act
        String json = om.writeValueAsString(as);
        NonComparativeAnalysisSituation deserialized = om.readValue(json, NonComparativeAnalysisSituation.class);

        // Assert
        Assertions.assertNotNull(deserialized);
        Assertions.assertEquals(as.getCube(), deserialized.getCube());
        Assertions.assertEquals(as.getMeasures(), deserialized.getMeasures());
        Assertions.assertEquals(as.getFilterConditions(), deserialized.getFilterConditions());
        Assertions.assertEquals(as.getBaseMeasureConditions(), deserialized.getBaseMeasureConditions());
        Assertions.assertEquals(as.getDimensionQualifications(), deserialized.getDimensionQualifications());
    }
}
