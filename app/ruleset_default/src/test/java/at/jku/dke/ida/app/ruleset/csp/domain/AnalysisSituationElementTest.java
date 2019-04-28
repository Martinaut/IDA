package at.jku.dke.ida.app.ruleset.csp.domain;

import at.jku.dke.ida.data.models.CubeSimilarity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnalysisSituationElementTest {
    @Test
    void testNullElements() {
        // Prepare
        var ase = new AnalysisSituationElement(null);

        // Execute
        double score = ase.getScore();

        // Assert
        assertEquals(0, score, 0.0000001);
    }

    @Test
    void testEmptyElements() {
        // Prepare
        var ase = new AnalysisSituationElement();

        // Execute
        double score = ase.getScore();

        // Assert
        assertEquals(0, score, 0.0000001);
    }

    @Test
    void testScoreShouldBe0() {
        // Prepare
        var ase = new AnalysisSituationElement();
        ase.getElements().add(new CubeSimilarity("term", "testcube", "testelement1", "measure", 0));
        ase.getElements().add(new CubeSimilarity("count", "testcube", "testelement2", "measure", 0));

        // Execute
        double score = ase.getScore();

        // Assert
        assertEquals(0, score, 0.0000001);
    }

    @Test
    void testScore() {
        // Prepare
        var ase = new AnalysisSituationElement();
        ase.getElements().add(new CubeSimilarity("term", "testcube", "testelement1", "measure", 0.53));
        ase.getElements().add(new CubeSimilarity("count", "testcube", "testelement2", "measure", 0.85));

        // Execute
        double score = ase.getScore();

        // Assert
        assertEquals(0.4505, score, 0.0000001);
    }
}
