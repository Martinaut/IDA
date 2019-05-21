package at.jku.dke.ida.app.nlp.drools;

import at.jku.dke.ida.nlp.drools.StringSimilarityService;
import at.jku.dke.ida.nlp.models.StringSimilarityServiceModel;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.ida.shared.operations.Operation;
import at.jku.dke.ida.shared.session.SessionModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

class StringSimilarityServiceTest {
    @Test
    void test() {
        // Prepare
        Set<Displayable> options = new HashSet<>();
        options.add(new Operation(Event.NAVIGATE_MEASURE_ADD, "Add Measure", 1));
        options.add(new Operation(Event.NAVIGATE_MEASURE_DROP, "Drop Measure", 1));
        options.add(new Operation(Event.NAVIGATE_MEASURE_REFOCUS, "Refocus Measure", 1));
        options.add(new Operation(Event.NAVIGATE_BMC_NARROW, "Add Base Measure Condition", 1));
        options.add(new Operation(Event.EXECUTE_QUERY, "Execute Query", 1));
        options.add(new Operation(Event.EXIT, "Exit", 1));

        var tmp = new SessionModel("tet", "en");
        tmp.setUserInput("add a measure");
        StringSimilarityServiceModel model = new StringSimilarityServiceModel(
                "NONE",
                Locale.ENGLISH,
                new NonComparativeAnalysisSituation(),
                Event.MORE_INFORMATION,
                tmp,
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
