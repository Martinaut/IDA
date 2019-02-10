package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.models.CubeLabel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DbConfiguration.class})
public class CubeLabelRepositoryTest {

    @Autowired
    private CubeLabelRepository cubeLabelRepository;

    @BeforeEach
    public void clearDatabase(){
        cubeLabelRepository.deleteAll();
    }

    @Test
    public void testFindByCubeUriAndLangWithExisitingEntry() {
        // prepare
        CubeLabel labelDE = new CubeLabel("http://example.org/cube1", "de", "Alter", "Besagt, wie lange schon jemand lebt.");
        CubeLabel labelEN = new CubeLabel(labelDE.getCubeUri(), "en", "Age");
        cubeLabelRepository.save(labelDE);
        cubeLabelRepository.save(labelEN);

        // execute
        Optional<CubeLabel> found = cubeLabelRepository.findByCubeUriAndLang(labelDE.getCubeUri(), labelDE.getLang());

        // assert
        assertTrue(found.isPresent());
        assertNotNull(found.get());
        assertEquals(labelDE.getCubeUri(), found.get().getCubeUri());
        assertEquals(labelDE.getLabel(), found.get().getLabel());
        assertEquals(labelDE.getDescription(), found.get().getDescription());
        assertEquals(labelDE.getLang(), found.get().getLang());
    }

    @Test
    public void testFindByCubeUriAndLangWithNotExisitingEntry() {
        // prepare
        CubeLabel labelDE = new CubeLabel("http://example.org/cube1", "de", "Alter", "Besagt, wie lange schon jemand lebt.");
        CubeLabel labelEN = new CubeLabel(labelDE.getCubeUri(), "en", "Age");
        cubeLabelRepository.save(labelDE);
        cubeLabelRepository.save(labelEN);

        // execute
        Optional<CubeLabel> found = cubeLabelRepository.findByCubeUriAndLang(labelDE.getCubeUri(), "es");

        // assert
        assertTrue(found.isEmpty());
    }

    @Test
    public void testFindByCubeUri() {
        // prepare
        CubeLabel labelDE = new CubeLabel("http://example.org/cube1", "de", "Alter", "Besagt, wie lange schon jemand lebt.");
        CubeLabel labelEN = new CubeLabel(labelDE.getCubeUri(), "en", "Age");
        cubeLabelRepository.save(labelDE);
        cubeLabelRepository.save(labelEN);

        // execute
        List<CubeLabel> found = cubeLabelRepository.findByCubeUri(labelDE.getCubeUri());

        // assert
        assertEquals(2, found.size());
    }
}
