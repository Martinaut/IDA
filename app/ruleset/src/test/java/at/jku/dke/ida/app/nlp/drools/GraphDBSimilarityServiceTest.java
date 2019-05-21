package at.jku.dke.ida.app.nlp.drools;

import at.jku.dke.ida.data.configuration.DataSpringConfiguration;
import at.jku.dke.ida.nlp.drools.GraphDBSimilarityService;
import at.jku.dke.ida.nlp.drools.WordGroupsService;
import at.jku.dke.ida.nlp.models.GraphDBSimilarityServiceModel;
import at.jku.dke.ida.nlp.models.WordGroupsServiceModel;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;
import at.jku.dke.ida.shared.spring.SharedSpringConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;
import java.util.stream.Collectors;

@SpringBootTest(classes = {SharedSpringConfiguration.class, DataSpringConfiguration.class})
@ExtendWith(SpringExtension.class)
class GraphDBSimilarityServiceTest {
    @Test
    void test() {
        // Prepare
        WordGroupsServiceModel model = new WordGroupsServiceModel(
                Locale.ENGLISH,
                new NonComparativeAnalysisSituation(),
                new SessionModel("test", "en"),
                "Show me the total costs per insurant and doctor district.");
        var wgs = new WordGroupsService().executeRules(model);
        GraphDBSimilarityServiceModel simModel = new GraphDBSimilarityServiceModel(
                Locale.ENGLISH,
                new NonComparativeAnalysisSituation(),
                new SessionModel("test", "en"),
                wgs
        );

        // Execute
        var result = new GraphDBSimilarityService().executeRules(simModel).stream().sorted().collect(Collectors.toList());

        // Assert
        Assertions.assertNotNull(result);
    }
}
