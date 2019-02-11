package at.jku.dke.inga.rules;

import at.jku.dke.inga.rules.models.ResolveOperationInputModel;
import at.jku.dke.inga.rules.services.OperationInputResolver;
import at.jku.dke.inga.shared.EventNames;
import at.jku.dke.inga.shared.SharedSpringConfiguration;
import at.jku.dke.inga.shared.display.Display;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {DroolsSpringConfiguration.class, SharedSpringConfiguration.class})
@ExtendWith(SpringExtension.class)
class OperationInputResolverTest {

    @Autowired
    private OperationInputResolver inputResolver;

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
    void testResolveExecuteQueryKeyword() {
        String result = inputResolver.resolveOperationInput(new ResolveOperationInputModel(
                "ResolvingOperationInput",
                new NonComparativeAnalysisSituation(),
                Locale.ENGLISH,
                "execute the query, please",
                operations));
        assertEquals(EventNames.EXECUTE_QUERY, result);
    }

    @Test
    void testResolveExecuteMeasureAddNumber() {
        String result = inputResolver.resolveOperationInput(new ResolveOperationInputModel(
                "ResolvingOperationInput",
                new NonComparativeAnalysisSituation(),
                Locale.ENGLISH,
                "0",
                operations));
        assertEquals(EventNames.NAVIGATE, result);
    }

    @Test
    void testResolveExecuteExitKeyword() {
        String result = inputResolver.resolveOperationInput(new ResolveOperationInputModel(
                "ResolvingOperationInput",
                new NonComparativeAnalysisSituation(),
                Locale.ENGLISH,
                "exit",
                operations));
        assertEquals(EventNames.EXIT, result);
    }

    @Test
    void testResolveExecuteExitNumber() {
        String result = inputResolver.resolveOperationInput(new ResolveOperationInputModel(
                "ResolvingOperationInput",
                new NonComparativeAnalysisSituation(),
                Locale.ENGLISH,
                "4",
                operations));
        assertEquals(EventNames.NAVIGATE, result);
    }
}
