package at.jku.dke.ida.app.nlp.drools;

import at.jku.dke.ida.app.nlp.models.StringSimilarityServiceModel;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.ida.shared.operations.Operation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

class StringSimilarityServiceTest {
    @Test
    void test() {
        // Prepare
        Set<Displayable> options = new HashSet<>();
        options.add(new Operation("addMeasure", "Add Measure", 1));
        options.add(new Operation("dropMeasure", "Drop Measure", 1));
        options.add(new Operation("refocusMeasure", "Refocus Measure", 1));
        options.add(new Operation("addBMC", "Add Base Measure Condition", 1));
        options.add(new Operation("execute", "Execute Query", 1));
        options.add(new Operation("exit", "Exit", 1));

        StringSimilarityServiceModel model = new StringSimilarityServiceModel(
                Locale.ENGLISH,
                new NonComparativeAnalysisSituation(),
                new HashMap<>(),
                "add a measure",
                options);


        // Execute
        var sims = new StringSimilarityService().executeRules(model).stream()
                .sorted()
                .collect(Collectors.toList());
        // Assert
        Assertions.assertNotNull(sims);
        sims.forEach(System.out::println);
    }
}
