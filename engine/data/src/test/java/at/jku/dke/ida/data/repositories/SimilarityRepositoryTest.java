package at.jku.dke.ida.data.repositories;

import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.configuration.DataSpringConfiguration;
import at.jku.dke.ida.data.models.CubeSimilarity;
import at.jku.dke.ida.shared.spring.SharedSpringConfiguration;
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
        List<CubeSimilarity> labels = repository.getTermSimilarity("en", "amount");
        System.out.println(labels);

        // Assert
        assertFalse(labels.isEmpty());
    }

    @Test
    void testFindByLang2() throws QueryException {
        // Prepare

        // Execute
        List<CubeSimilarity> labels = repository.getWordSimilarity("en", "doctor district");
        System.out.println(labels);

        // Assert
        assertFalse(labels.isEmpty());
    }
}
