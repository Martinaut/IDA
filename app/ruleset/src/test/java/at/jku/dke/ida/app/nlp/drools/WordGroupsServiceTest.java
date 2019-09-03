package at.jku.dke.ida.app.nlp.drools;

import at.jku.dke.ida.nlp.drools.WordGroupsService;
import at.jku.dke.ida.nlp.models.WordGroupsServiceModel;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

class WordGroupsServiceTest {
    @Test
    void test() {
        // Prepare
        WordGroupsServiceModel model = new WordGroupsServiceModel(
                Locale.ENGLISH,
                new NonComparativeAnalysisSituation(),
                new SessionModel("test", "en"),
                "Compare the costs from this year with last year.");

        // Execute
        var result = new WordGroupsService().executeRules(model);

        // Assert
        System.out.println(result);
        Assertions.assertNotNull(result);
    }
}
