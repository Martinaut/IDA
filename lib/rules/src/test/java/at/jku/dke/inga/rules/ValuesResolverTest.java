package at.jku.dke.inga.rules;

import at.jku.dke.inga.data.models.CubeLabel;
import at.jku.dke.inga.data.repositories.CubeElementLabelRepository;
import at.jku.dke.inga.data.repositories.CubeLabelRepository;
import at.jku.dke.inga.rules.models.ResolveValuesDataModel;
import at.jku.dke.inga.rules.services.ValuesResolver;
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

@SpringBootTest(classes = {DroolsSpringConfiguration.class, DbConfiguration.class, SharedSpringConfiguration.class})
@ExtendWith(SpringExtension.class)
class ValuesResolverTest {

    @Autowired
    private ValuesResolver valuesResolver;

    @Autowired
    private CubeLabelRepository cubeLabelRepository;

    @Autowired
    private CubeElementLabelRepository cubeElementLabelRepository;

    @BeforeEach
    void fillDatabase() {
        cubeLabelRepository.deleteAll();

        cubeLabelRepository.save(new CubeLabel("http://demo.com/cube1", "en", "Demo-Cube 1", "This is a description."));
        cubeLabelRepository.save(new CubeLabel("http://demo.com/cube1", "de", "Demo-Würfel 1", "Das ist eine Beschreibung."));
        cubeLabelRepository.save(new CubeLabel("http://demo.com/cube2", "en", "Demo-Cube 2"));
        cubeLabelRepository.save(new CubeLabel("http://demo.com/cube2", "de", "Demo-Würfel 2"));
    }

    @Test
    void testResolveValues() {
        Display disp = valuesResolver.resolveValues(new ResolveValuesDataModel("DisplayingValues", new NonComparativeAnalysisSituation(), Locale.ENGLISH, EventNames.NAVIGATE_CUBE_SELECT));
        assertNotNull(disp);
    }
}
