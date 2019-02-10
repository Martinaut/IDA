package at.jku.dke.inga.rules;

import at.jku.dke.inga.data.models.Cube;
import at.jku.dke.inga.data.models.CubeElement;
import at.jku.dke.inga.rules.models.ResolveOperationsDataModel;
import at.jku.dke.inga.rules.services.OperationsResolver;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.inga.shared.operations.Operation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {DroolsSpringConfiguration.class})
@ExtendWith(SpringExtension.class)
class OperationsResolverTest {

    @Autowired
    private OperationsResolver operationsResolver;

    @Test
    void testResolveOperations() {
        operationsResolver.closeSession();

        List<Operation> ops = operationsResolver.resolveOperations(new ResolveOperationsDataModel("asb", new NonComparativeAnalysisSituation(), Locale.ENGLISH));
        assertEquals(2, ops.size());
    }

    @Test
    void testResolveOperations2() {
        operationsResolver.closeSession();

        NonComparativeAnalysisSituation as = new NonComparativeAnalysisSituation();
        as.setCube("http://asdf");
        as.addMeasure("http://asdf#1");

        Cube cube = new Cube("http://asdf");
        List<CubeElement> elements = new ArrayList<>();
        elements.add(new CubeElement("http://asdf", "http://asdf#1", "http://purl.org/linked-data/cube#MeasureProperty"));
        elements.add(new CubeElement("http://asdf", "http://asdf#2", "http://purl.org/linked-data/cube#MeasureProperty"));
        elements.add(new CubeElement("http://asdf", "http://asdf#3", "http://purl.org/linked-data/cube#MeasureProperty"));

        List<Operation> ops = operationsResolver.resolveOperations(new ResolveOperationsDataModel("navigate", as, Locale.ENGLISH, cube, elements));
        assertEquals(5, ops.size());
    }
}
