package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.configuration.DataConfiguration;
import at.jku.dke.inga.data.models.Label;
import at.jku.dke.inga.shared.SharedSpringConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {SharedSpringConfiguration.class, DataConfiguration.class})
@ExtendWith(SpringExtension.class)
class MeasureRepositoryTest {
    @Autowired
    private MeasureRepository repository;

    @Test
    void testFindByLang() {
        // Prepare

        // Execute
        List<Label> labels = repository.findLabelsByCubeAndLangWithExclusions("http://www.example.org/drugs#DrugPrescriptionCube", "en", new HashSet<>(Collections.singletonList("http://www.example.org/drugs#QuantityMeasure")));

        // Assert
        assertEquals(1, labels.size());
    }
}
