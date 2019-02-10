package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.models.Cube;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DbConfiguration.class})
public class CubeRepositoryTest {

    @Autowired
    private CubeRepository cubeRepository;

    @Test
    public void testFindById() {
        // prepare
        Cube cube = new Cube("http://example.org/cube1");
        cubeRepository.save(cube);

        // execute
        Optional<Cube> found = cubeRepository.findById(cube.getUri());

        // assert
        assertTrue(found.isPresent());
        assertNotNull(found.get());
        assertEquals(cube.getUri(), found.get().getUri());
    }
}
