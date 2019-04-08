package at.jku.dke.inga.app.ruleset.drools;

import at.jku.dke.inga.app.ruleset.models.WordGroupsServiceModel;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
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
                new HashMap<>(),
                "Show me the total costs per insurant and doctor district.");

        // Execute
        var result = new WordGroupsService().executeRules(model);

        // Assert
        Assertions.assertNotNull(result);
    }
}
