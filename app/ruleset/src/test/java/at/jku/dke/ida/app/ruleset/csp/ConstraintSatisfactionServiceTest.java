package at.jku.dke.ida.app.ruleset.csp;

import at.jku.dke.ida.csp.ConstraintSatisfactionSpringConfiguration;
import at.jku.dke.ida.csp.service.ConstraintSatisfactionService;
import at.jku.dke.ida.data.configuration.DataSpringConfiguration;
import at.jku.dke.ida.data.models.CubeSimilarity;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
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
    void fillAnalysisSituationNonCompSimple() {
        // Prepare
        var similarities = new HashSet<CubeSimilarity>();
        similarities.add(new CubeSimilarity("store cost", "http://www.example.org/jku/dke/foodmart#CubeSales", "http://www.example.org/jku/dke/foodmart#CubeSalesMeasureStoreCostSum", "http://dke.jku.at/inga/cubes#AggregateMeasure", 0.4961757791410231));
        similarities.add(new CubeSimilarity("store cost", "http://www.example.org/jku/dke/foodmart#CubeSales2", "http://www.example.org/jku/dke/foodmart#CubeSalesMeasureStoreCostSum", "http://dke.jku.at/inga/cubes#AggregateMeasure", 0.4961757791410231));

        var as = new NonComparativeAnalysisSituation();

        var sessionModel = new SessionModel(UUID.randomUUID().toString(), "en");
        sessionModel.setAnalysisSituation(as);

        // Execute
        new ConstraintSatisfactionService().fillAnalysisSituation(sessionModel, similarities);

        // Assert
        assertEquals(1, as.getMeasures().size());
        assertEquals("http://www.example.org/jku/dke/foodmart#CubeSalesMeasureStoreCostSum", as.getMeasures().iterator().next());
    }

}