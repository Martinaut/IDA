package at.jku.dke.inga.rules;

import at.jku.dke.inga.shared.EventNames;
import at.jku.dke.inga.shared.SharedSpringConfiguration;
import at.jku.dke.inga.shared.display.Display;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {DroolsSpringConfiguration.class, SharedSpringConfiguration.class})
@ExtendWith(SpringExtension.class)
class ValuesResolverTest {

    @Autowired
    private ValuesResolver valuesResolver;

//    @Autowired
//    private CubeRepository cubeLabelRepository;
//    @Autowired
//    private CubeElementLabelRepository cubeElementLabelRepository;
//    @Autowired
//    private CubeElementRepository cubeElementRepository;

    @BeforeEach
    void fillDatabase() {
//        cubeLabelRepository.deleteAll();
//        cubeElementLabelRepository.deleteAll();
//        cubeElementRepository.deleteAll();
//
//        // Cubes
//        cubeLabelRepository.save(new CubeLabel("http://demo.com/cube1", "en", "Demo-Cube 1", "This is a description."));
//        cubeLabelRepository.save(new CubeLabel("http://demo.com/cube1", "de", "Demo-Würfel 1", "Das ist eine Beschreibung."));
//        cubeLabelRepository.save(new CubeLabel("http://demo.com/cube2", "en", "Demo-Cube 2"));
//        cubeLabelRepository.save(new CubeLabel("http://demo.com/cube2", "de", "Demo-Würfel 2"));
//
//        // Cube Elements
//        cubeElementRepository.save(new CubeElement("http://demo.com/cube1", "http://demo.com/cube1#m1", "http://purl.org/linked-data/cube#MeasureProperty"));
//        cubeElementRepository.save(new CubeElement("http://demo.com/cube1", "http://demo.com/cube1#m2", "http://purl.org/linked-data/cube#MeasureProperty"));
//        cubeElementRepository.save(new CubeElement("http://demo.com/cube1", "http://demo.com/cube1#m3", "http://purl.org/linked-data/cube#MeasureProperty"));
//
//        // Cube Element Labels
//        cubeElementLabelRepository.save(new CubeElementLabel("http://demo.com/cube1#m1", "en", "Quantity"));
//        cubeElementLabelRepository.save(new CubeElementLabel("http://demo.com/cube1#m1", "de", "Menge"));
//        cubeElementLabelRepository.save(new CubeElementLabel("http://demo.com/cube1#m2", "en", "Price"));
//        cubeElementLabelRepository.save(new CubeElementLabel("http://demo.com/cube1#m2", "de", "Preis"));
//        cubeElementLabelRepository.save(new CubeElementLabel("http://demo.com/cube1#m3", "en", "Sum"));
//        cubeElementLabelRepository.save(new CubeElementLabel("http://demo.com/cube1#m3", "de", "Summe"));
    }

    @Test
    void testResolveValuesCube() {
        Display disp = valuesResolver.resolveValues(new ResolveValuesDataModel("DisplayingValues", new NonComparativeAnalysisSituation(), Locale.ENGLISH, EventNames.NAVIGATE_CUBE_SELECT));
        assertNotNull(disp);
    }

    @Test
    void testResolveValuesMeasureAdd() {
        NonComparativeAnalysisSituation as = new NonComparativeAnalysisSituation();
        as.setCube("http://demo.com/cube1");
        as.addMeasure("http://demo.com/cube1#m2");

        Display disp = valuesResolver.resolveValues(new ResolveValuesDataModel("DisplayingValues", as, Locale.ENGLISH, EventNames.NAVIGATE_MEASURE_ADD));
        assertNotNull(disp);
    }

    @Test
    void testResolveValuesMeasureDrop() {
        NonComparativeAnalysisSituation as = new NonComparativeAnalysisSituation();
        as.setCube("http://demo.com/cube1");
        as.addMeasure("http://demo.com/cube1#m2");

        Display disp = valuesResolver.resolveValues(new ResolveValuesDataModel("DisplayingValues", as, Locale.ENGLISH, EventNames.NAVIGATE_MEASURE_DROP));
        assertNotNull(disp);
    }

    @Test
    void testResolveValuesMeasureRefocus() {
        NonComparativeAnalysisSituation as = new NonComparativeAnalysisSituation();
        as.setCube("http://demo.com/cube1");
        as.addMeasure("http://demo.com/cube1#m2");

        Display disp = valuesResolver.resolveValues(new ResolveValuesDataModel("DisplayingValues", as, Locale.ENGLISH, EventNames.NAVIGATE_MEASURE_REFOCUS));
        assertNotNull(disp);
    }
}
