package at.jku.dke.ida.app.ruleset.csp;

import at.jku.dke.ida.csp.ConstraintSatisfactionSpringConfiguration;
import at.jku.dke.ida.csp.service.ConstraintSatisfactionService;
import at.jku.dke.ida.data.configuration.DataSpringConfiguration;
import at.jku.dke.ida.data.models.WordGroup;
import at.jku.dke.ida.data.models.similarity.ComparativeMeasureSimilarity;
import at.jku.dke.ida.data.models.similarity.ComparativeSimilarity;
import at.jku.dke.ida.data.models.similarity.CubeSimilarity;
import at.jku.dke.ida.shared.models.ComparativeAnalysisSituation;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.ida.shared.operations.Pattern;
import at.jku.dke.ida.shared.operations.PatternPart;
import at.jku.dke.ida.shared.session.SessionModel;
import at.jku.dke.ida.shared.spring.SharedSpringConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {SharedSpringConfiguration.class, DataSpringConfiguration.class, ConstraintSatisfactionSpringConfiguration.class})
@ExtendWith(SpringExtension.class)
class ConstraintSatisfactionServiceTest {

    @Test
    void fillAnalysisSituationComp() {
        // compare the change of costs from this year with the previous year

        // Prepare
        var similarities = new HashSet<CubeSimilarity>();
        similarities.add(new ComparativeSimilarity(new WordGroup("change from year"), "http://www.example.org/jku/dke/foodmart#CubeHR", "http://www.example.org/jku/dke/foodmart#CubeSalesJCPYearOnYear", "http://dke.jku.at/inga/cubes#JoinConditionPredicate", 0.5, "setOfComparison"));
        similarities.add(new ComparativeSimilarity(new WordGroup("year with year"), "http://www.example.org/jku/dke/foodmart#CubeWarehouse", "http://www.example.org/jku/dke/foodmart#CubeSalesJCPYearOnYear", "http://dke.jku.at/inga/cubes#JoinConditionPredicate", 0.7142857142857143, "setOfInterest"));
        similarities.add(new ComparativeSimilarity(new WordGroup("year with year"), "http://www.example.org/jku/dke/foodmart#CubeHR", "http://www.example.org/jku/dke/foodmart#CubeSalesJCPYearOnYear", "http://dke.jku.at/inga/cubes#JoinConditionPredicate", 0.7142857142857143, "setOfInterest"));
        similarities.add(new ComparativeSimilarity(new WordGroup("change from year"), "http://www.example.org/jku/dke/foodmart#CubeSales", "http://www.example.org/jku/dke/foodmart#CubeSalesJCPYearOnYear", "http://dke.jku.at/inga/cubes#JoinConditionPredicate", 0.5, "setOfInterest"));
        similarities.add(new ComparativeSimilarity(new WordGroup("change from year"), "http://www.example.org/jku/dke/foodmart#CubeWarehouse", "http://www.example.org/jku/dke/foodmart#CubeSalesJCPYearOnYear", "http://dke.jku.at/inga/cubes#JoinConditionPredicate", 0.5, "setOfComparison"));
        similarities.add(new ComparativeSimilarity(new WordGroup("year with year"), "http://www.example.org/jku/dke/foodmart#CubeWarehouse", "http://www.example.org/jku/dke/foodmart#CubeSalesJCPYearOnYear", "http://dke.jku.at/inga/cubes#JoinConditionPredicate", 0.7142857142857143, "setOfComparison"));
        similarities.add(new ComparativeSimilarity(new WordGroup("change from year"), "http://www.example.org/jku/dke/foodmart#CubeSales", "http://www.example.org/jku/dke/foodmart#CubeSalesJCPYearOnYear", "http://dke.jku.at/inga/cubes#JoinConditionPredicate", 0.5, "setOfComparison"));
        similarities.add(new ComparativeSimilarity(new WordGroup("change from year"), "http://www.example.org/jku/dke/foodmart#CubeWarehouse", "http://www.example.org/jku/dke/foodmart#CubeSalesJCPYearOnYear", "http://dke.jku.at/inga/cubes#JoinConditionPredicate", 0.5, "setOfInterest"));
        similarities.add(new ComparativeSimilarity(new WordGroup("change from year"), "http://www.example.org/jku/dke/foodmart#CubeSales2", "http://www.example.org/jku/dke/foodmart#CubeSalesJCPYearOnYear", "http://dke.jku.at/inga/cubes#JoinConditionPredicate", 0.5, "setOfInterest"));
        similarities.add(new ComparativeSimilarity(new WordGroup("year with year"), "http://www.example.org/jku/dke/foodmart#CubeSales2", "http://www.example.org/jku/dke/foodmart#CubeSalesJCPYearOnYear", "http://dke.jku.at/inga/cubes#JoinConditionPredicate", 0.7142857142857143, "setOfComparison"));
        similarities.add(new ComparativeSimilarity(new WordGroup("year with year"), "http://www.example.org/jku/dke/foodmart#CubeSales", "http://www.example.org/jku/dke/foodmart#CubeSalesJCPYearOnYear", "http://dke.jku.at/inga/cubes#JoinConditionPredicate", 0.7142857142857143, "setOfInterest"));
        similarities.add(new ComparativeSimilarity(new WordGroup("year with year"), "http://www.example.org/jku/dke/foodmart#CubeSales2", "http://www.example.org/jku/dke/foodmart#CubeSalesJCPYearOnYear", "http://dke.jku.at/inga/cubes#JoinConditionPredicate", 0.7142857142857143, "setOfInterest"));
        similarities.add(new ComparativeSimilarity(new WordGroup("year with year"), "http://www.example.org/jku/dke/foodmart#CubeHR", "http://www.example.org/jku/dke/foodmart#CubeSalesJCPYearOnYear", "http://dke.jku.at/inga/cubes#JoinConditionPredicate", 0.7142857142857143, "setOfComparison"));
        similarities.add(new ComparativeSimilarity(new WordGroup("change from year"), "http://www.example.org/jku/dke/foodmart#CubeHR", "http://www.example.org/jku/dke/foodmart#CubeSalesJCPYearOnYear", "http://dke.jku.at/inga/cubes#JoinConditionPredicate", 0.5, "setOfInterest"));
        similarities.add(new ComparativeSimilarity(new WordGroup("year with year"), "http://www.example.org/jku/dke/foodmart#CubeSales", "http://www.example.org/jku/dke/foodmart#CubeSalesJCPYearOnYear", "http://dke.jku.at/inga/cubes#JoinConditionPredicate", 0.7142857142857143, "setOfComparison"));
        similarities.add(new ComparativeSimilarity(new WordGroup("change from year"), "http://www.example.org/jku/dke/foodmart#CubeSales2", "http://www.example.org/jku/dke/foodmart#CubeSalesJCPYearOnYear", "http://dke.jku.at/inga/cubes#JoinConditionPredicate", 0.5, "setOfComparison"));
        similarities.add(new ComparativeMeasureSimilarity(new WordGroup("change from year"), "http://www.example.org/jku/dke/foodmart#CubeSales", "http://www.example.org/jku/dke/foodmart#CubeSalesCMChangeOfCosts", "http://dke.jku.at/inga/cubes#ComparativeMeasure", 0.5, "setOfComparison", "http://www.example.org/jku/dke/foodmart#CubeSalesMeasureStoreCostSum"));
        similarities.add(new ComparativeMeasureSimilarity(new WordGroup("change of costs"), "http://www.example.org/jku/dke/foodmart#CubeSales", "http://www.example.org/jku/dke/foodmart#CubeSalesCMPSignificantChangeOfCosts", "http://dke.jku.at/inga/cubes#ComparativeMeasurePredicate", 0.5555555555555556, "setOfInterest", "http://www.example.org/jku/dke/foodmart#CubeSalesMeasureStoreCostSum"));
        similarities.add(new ComparativeMeasureSimilarity(new WordGroup("change from year"), "http://www.example.org/jku/dke/foodmart#CubeSales", "http://www.example.org/jku/dke/foodmart#CubeSalesCMChangeOfCosts", "http://dke.jku.at/inga/cubes#ComparativeMeasure", 0.5, "setOfInterest", "http://www.example.org/jku/dke/foodmart#CubeSalesMeasureStoreCostSum"));
        similarities.add(new ComparativeMeasureSimilarity(new WordGroup("change of costs"), "http://www.example.org/jku/dke/foodmart#CubeSales", "http://www.example.org/jku/dke/foodmart#CubeSalesCMChangeOfCosts", "http://dke.jku.at/inga/cubes#ComparativeMeasure", 1.0, "setOfComparison", "http://www.example.org/jku/dke/foodmart#CubeSalesMeasureStoreCostSum"));
        similarities.add(new ComparativeMeasureSimilarity(new WordGroup("change of costs"), "http://www.example.org/jku/dke/foodmart#CubeSales", "http://www.example.org/jku/dke/foodmart#CubeSalesCMChangeOfCosts", "http://dke.jku.at/inga/cubes#ComparativeMeasure", 1.0, "setOfInterest", "http://www.example.org/jku/dke/foodmart#CubeSalesMeasureStoreCostSum"));
        similarities.add(new ComparativeMeasureSimilarity(new WordGroup("change of costs"), "http://www.example.org/jku/dke/foodmart#CubeSales", "http://www.example.org/jku/dke/foodmart#CubeSalesCMPSignificantChangeOfCosts", "http://dke.jku.at/inga/cubes#ComparativeMeasurePredicate", 0.5555555555555556, "setOfComparison", "http://www.example.org/jku/dke/foodmart#CubeSalesMeasureStoreCostSum"));

        var as = new ComparativeAnalysisSituation();

        var sessionModel = new SessionModel(UUID.randomUUID().toString(), "en");
        sessionModel.setAnalysisSituation(as);
        sessionModel.setComparativeActiveAS(PatternPart.PARENT);
        sessionModel.getAdditionalData().put(Pattern.ADDITIONAL_DATA_COMPARATIVE, as);

        // Execute
        new ConstraintSatisfactionService().fillAnalysisSituation(sessionModel, similarities);

        // Assert
//        assertEquals(1, as.getMeasures().size());
//        assertEquals("http://www.example.org/jku/dke/foodmart#CubeSalesMeasureStoreCostSum", as.getMeasures().iterator().next());
    }

}