package at.jku.dke.ida.shared.models;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class ComparativeAnalysisSituationTest {

    @Test
    void testSetJoinConditionsNullThrowsException() {
        // Prepare
        var as = new ComparativeAnalysisSituation();

        // Execute and Assert
        assertThrows(IllegalArgumentException.class, () -> as.setJoinConditions(null));
    }

    @Test
    void testSetJoinConditionsEmptyThrowsException() {
        // Prepare
        var as = new ComparativeAnalysisSituation();

        // Execute and Assert
        assertThrows(IllegalArgumentException.class, () -> as.setJoinConditions(new HashSet<>()));
    }

    @Test
    void testAddJoinCondition() {
        // Prepare
        String val = "jc";
        var as = new ComparativeAnalysisSituation();

        // Execute
        boolean result = as.addJoinCondition(val);

        // Assert
        assertTrue(result);
        assertEquals(1, as.getJoinConditions().size());
        assertTrue(as.getJoinConditions().contains(val));
    }

    @Test
    void testRemoveJoinCondition() {
        // Prepare
        String val = "jc";
        var as = new ComparativeAnalysisSituation();
        as.addJoinCondition(val);

        // Execute
        boolean result = as.removeJoinCondition(val);

        // Assert
        assertTrue(result);
        assertEquals(0, as.getJoinConditions().size());
        assertFalse(as.getJoinConditions().contains(val));
    }

    @Test
    void testSetScoresNullThrowsException() {
        // Prepare
        var as = new ComparativeAnalysisSituation();

        // Execute and Assert
        assertThrows(IllegalArgumentException.class, () -> as.setScores(null));
    }

    @Test
    void testSetScoresEmptyThrowsException() {
        // Prepare
        var as = new ComparativeAnalysisSituation();

        // Execute and Assert
        assertThrows(IllegalArgumentException.class, () -> as.setScores(new HashSet<>()));
    }

    @Test
    void testAddScore() {
        // Prepare
        String val = "score";
        var as = new ComparativeAnalysisSituation();

        // Execute
        boolean result = as.addScore(val);

        // Assert
        assertTrue(result);
        assertEquals(1, as.getScores().size());
        assertTrue(as.getScores().contains(val));
    }

    @Test
    void testRemoveScore() {
        // Prepare
        String val = "score";
        var as = new ComparativeAnalysisSituation();
        as.addScore(val);

        // Execute
        boolean result = as.removeScore(val);

        // Assert
        assertTrue(result);
        assertEquals(0, as.getScores().size());
        assertFalse(as.getScores().contains(val));
    }

    @Test
    void testSetScoreFiltersNull() {
        // Prepare
        var as = new ComparativeAnalysisSituation();

        // Execute
        as.setScoreFilters(null);

        // Assert
        assertNotNull(as.getScoreFilters());
        assertEquals(0, as.getScoreFilters().size());
    }

    @Test
    void testAddScoreFilter() {
        // Prepare
        String val = "ScoreFilter";
        var as = new ComparativeAnalysisSituation();

        // Execute
        boolean result = as.addScoreFilter(val);

        // Assert
        assertTrue(result);
        assertEquals(1, as.getScoreFilters().size());
        assertTrue(as.getScoreFilters().contains(val));
    }

    @Test
    void testRemoveScoreFilter() {
        // Prepare
        String val = "ScoreFilter";
        var as = new ComparativeAnalysisSituation();
        as.addScoreFilter(val);

        // Execute
        boolean result = as.removeScoreFilter(val);

        // Assert
        assertTrue(result);
        assertEquals(0, as.getScoreFilters().size());
        assertFalse(as.getScoreFilters().contains(val));
    }
}
