package at.jku.dke.inga.rules;

import at.jku.dke.inga.shared.EventNames;
import at.jku.dke.inga.shared.SharedSpringConfiguration;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.inga.shared.operations.Operation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {DroolsSpringConfiguration.class, SharedSpringConfiguration.class})
@ExtendWith(SpringExtension.class)
class ActionResolverTest {

    @Autowired
    private ActionResolver actionResolver;

    private static final Map<Integer, Operation> operations = new HashMap<>();

    @BeforeAll
    static void initOperations(){
        operations.put(0, new Operation(EventNames.NAVIGATE_MEASURE_ADD, 0));
        operations.put(1, new Operation(EventNames.NAVIGATE_MEASURE_REFOCUS, 1));
        operations.put(2, new Operation(EventNames.NAVIGATE_MEASURE_DROP, 2));
        operations.put(3, new Operation(EventNames.EXECUTE_QUERY, 3));
        operations.put(4, new Operation(EventNames.EXIT, 4));
    }

    @Test
    void testResolveExecuteAddMeasure() {
        String result = actionResolver.resolveAction(new ResolveActionModel(
                "ResolvingAction",
                new NonComparativeAnalysisSituation(),
                Locale.ENGLISH,
                "0",
                operations));
        assertEquals(EventNames.NAVIGATE_MEASURE_ADD, result);
    }

    @Test
    void testResolveInvalidNumber() {
        String result = actionResolver.resolveAction(new ResolveActionModel(
                "ResolvingAction",
                new NonComparativeAnalysisSituation(),
                Locale.ENGLISH,
                "03asdf",
                operations));
        assertEquals(EventNames.INVALID_INPUT, result);
    }

    @Test
    void testResolveNotContainedNumber() {
        String result = actionResolver.resolveAction(new ResolveActionModel(
                "ResolvingAction",
                new NonComparativeAnalysisSituation(),
                Locale.ENGLISH,
                "7",
                operations));
        assertEquals(EventNames.INVALID_INPUT, result);
    }
}
