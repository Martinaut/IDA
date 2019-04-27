package at.jku.dke.ida.app.nlp.drools;

import at.jku.dke.ida.app.nlp.models.WordGroupsServiceModel;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Locale;

class WordGroupsServiceTest {
    @Test
    void test() {
        // Prepare
        WordGroupsServiceModel model = new WordGroupsServiceModel(
                Locale.ENGLISH,
                new NonComparativeAnalysisSituation(),
                new SessionModel("test", "en"),
                "Show me the total costs per insurant and doctor district.");

        // Execute
        var result = new WordGroupsService().executeRules(model);

        // Assert
        Assertions.assertNotNull(result);
    }
}
