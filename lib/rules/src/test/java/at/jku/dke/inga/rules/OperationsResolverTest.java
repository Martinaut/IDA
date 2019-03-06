package at.jku.dke.inga.rules;

import at.jku.dke.inga.data.models.Cube;
import at.jku.dke.inga.data.models.CubeElement;
import at.jku.dke.inga.rules.models.ResolveOperationsDataModel;
import at.jku.dke.inga.rules.services.OperationsResolver;
import at.jku.dke.inga.shared.DefaultTypes;
import at.jku.dke.inga.shared.EventNames;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.inga.shared.operations.Operation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {DroolsSpringConfiguration.class})
@ExtendWith(SpringExtension.class)
class OperationsResolverTest {

    private static final String BASE_URI = "http://demo.org/drug#";

    @Autowired
    private OperationsResolver operationsResolver;

    @BeforeEach
    void setupTest() {
        operationsResolver.closeSession();
    }

    @Test
    void testResolveOperationsIfNoCubeSelected() {
        List<Operation> result = operationsResolver.resolveOperations(createModel(new NonComparativeAnalysisSituation(), null));
        alwaysAssert(result, false);
    }

    @Test
    void testResolveOperationsIfCubeSelected() {
        // Prepare
        Cube cube = new Cube(BASE_URI);
        NonComparativeAnalysisSituation as = new NonComparativeAnalysisSituation();
        as.setCube(BASE_URI);
        ResolveOperationsDataModel model = createModel(as, cube);

        // Execute
        List<Operation> result = operationsResolver.resolveOperations(model);

        // Assert
        alwaysAssert(result, true);
        // TODO
    }

    @Test
    void testResolveOperationsIfMeasureSelected() {
        // Prepare
        Cube cube = new Cube(BASE_URI);
        NonComparativeAnalysisSituation as = new NonComparativeAnalysisSituation();
        as.setCube(BASE_URI);
        as.addMeasure(BASE_URI + "costs");
        ResolveOperationsDataModel model = createModel(as, cube);

        // Execute
        List<Operation> result = operationsResolver.resolveOperations(model);

        // Assert
        alwaysAssert(result, true);
        assertTrue(result.stream().anyMatch(x -> x.getEventName().equals(EventNames.EXECUTE_QUERY)), "Result does not contain the execute query operation.");
        // TODO
    }

    // region --- Helpers ---
    private static ResolveOperationsDataModel createModel(NonComparativeAnalysisSituation as, Cube cube) {
        List<CubeElement> cubeElements = new ArrayList<>();
        cubeElements.add(new CubeElement(BASE_URI, BASE_URI + "quantity", DefaultTypes.TYPE_MEASURE));
        cubeElements.add(new CubeElement(BASE_URI, BASE_URI + "costs", DefaultTypes.TYPE_MEASURE));
        // TODO
        return new ResolveOperationsDataModel("DisplayingOperations", as, Locale.ENGLISH, cube, cubeElements);
    }

    private void alwaysAssert(List<Operation> operations, boolean cubeSelected) {
        assertTrue(operations.stream().anyMatch(x -> x.getEventName().equals(EventNames.EXIT)), "Result does not contain the exit operation.");
        if (cubeSelected) {
            // assertTrue(operations.stream().anyMatch(x -> x.getEventName().equals(EventNames.EXECUTE_QUERY)), "Result does not contain the execute query operation.");
            assertFalse(operations.stream().anyMatch(x -> x.getEventName().equals(EventNames.NAVIGATE_CUBE_SELECT)), "Result does contain the select cube operation.");
        } else {
            assertFalse(operations.stream().anyMatch(x -> x.getEventName().equals(EventNames.EXECUTE_QUERY)), "Result does contain the execute query operation.");
            assertTrue(operations.stream().anyMatch(x -> x.getEventName().equals(EventNames.NAVIGATE_CUBE_SELECT)), "Result does not contain the select cube operation.");
        }
    }
    // endregion
}
