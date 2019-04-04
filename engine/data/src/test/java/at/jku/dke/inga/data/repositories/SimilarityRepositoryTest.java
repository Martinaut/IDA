package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.QueryException;
import at.jku.dke.inga.data.configuration.DataSpringConfiguration;
import at.jku.dke.inga.data.models.Similarity;
import at.jku.dke.inga.shared.spring.SharedSpringConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {SharedSpringConfiguration.class, DataSpringConfiguration.class})
@ExtendWith(SpringExtension.class)
class SimilarityRepositoryTest {
    @Autowired
    private SimilarityRepository repository;

    @Test
    void testFindByLang() throws QueryException {
        // Prepare

        // Execute
        List<Similarity> labels = repository.getSimilarity("en", "amount");
        System.out.println(labels);

        // Assert
        assertFalse(labels.isEmpty());
    }
}
