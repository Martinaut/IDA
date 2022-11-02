package at.jku.dke.ida.shared.models;

import at.jku.dke.ida.shared.PojoTestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NonComparativeAnalysisSituationTest {

    @Test
    void testOperationAccessors() {
        PojoTestUtils.validateAccessors(NonComparativeAnalysisSituation.class);
    }

    @Test
    void testIsCubeDefinedTrue() {
        // Prepare
        var as = new NonComparativeAnalysisSituation();
        as.setCube("test");

        // Execute
        boolean result = as.isCubeDefined();

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsCubeDefinedFalse() {
        // Prepare
        var as = new NonComparativeAnalysisSituation();

        // Execute
        boolean result = as.isCubeDefined();

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsExecutableTrue() {
        // Prepare
        var as = new NonComparativeAnalysisSituation();
        as.setCube("test");
        as.addMeasure("mymeasure");
        as.addDimensionQualification(new DimensionQualification("mydim"));

        // Execute
        boolean result = as.isCubeDefined();

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsExecutableFalse() {
        // Prepare
        var as = new NonComparativeAnalysisSituation();

        // Execute
        boolean result = as.isExecutable();

        // Assert
        assertFalse(result);
    }

    @Test
    void testAddBaseMeasureCondition() {
        // Prepare
        String val = "bmc";
        var as = new NonComparativeAnalysisSituation();

        // Execute
        boolean result = as.addBaseMeasureCondition(val);

        // Assert
        assertTrue(result);
        assertEquals(1, as.getBaseMeasureConditions().size());
        assertTrue(as.getBaseMeasureConditions().contains(val));
    }

    @Test
    void testRemoveBaseMeasureCondition() {
        // Prepare
        String val = "bmc";
        var as = new NonComparativeAnalysisSituation();
        as.addBaseMeasureCondition(val);

        // Execute
        boolean result = as.removeBaseMeasureCondition(val);

        // Assert
        assertTrue(result);
        assertEquals(0, as.getBaseMeasureConditions().size());
        assertFalse(as.getBaseMeasureConditions().contains(val));
    }

    @Test
    void testAddMeasure() {
        // Prepare
        String val = "m";
        var as = new NonComparativeAnalysisSituation();

        // Execute
        boolean result = as.addMeasure(val);

        // Assert
        assertTrue(result);
        assertEquals(1, as.getMeasures().size());
        assertTrue(as.getMeasures().contains(val));
    }

    @Test
    void testRemoveMeasure() {
        // Prepare
        String val = "m";
        var as = new NonComparativeAnalysisSituation();
        as.addMeasure(val);

        // Execute
        boolean result = as.removeMeasure(val);

        // Assert
        assertTrue(result);
        assertEquals(0, as.getMeasures().size());
        assertFalse(as.getMeasures().contains(val));
    }

    @Test
    void testAddDimensionQualification() {
        // Prepare
        DimensionQualification dq = new DimensionQualification();
        var as = new NonComparativeAnalysisSituation();

        // Execute
        as.addDimensionQualification(dq);

        // Assert
        assertEquals(1, as.getDimensionQualifications().size());
        assertTrue(as.getDimensionQualifications().contains(dq));
    }

    @Test
    void testRemoveDimensionQualification() {
        // Prepare
        DimensionQualification dq = new DimensionQualification();
        var as = new NonComparativeAnalysisSituation();
        as.addDimensionQualification(dq);

        // Execute
        as.removeDimensionQualification(dq);

        // Assert
        assertEquals(0, as.getDimensionQualifications().size());
        assertFalse(as.getDimensionQualifications().contains(dq));
    }

    @Test
    void testAddFilterCondition() {
        // Prepare
        String val = "fc";
        var as = new NonComparativeAnalysisSituation();

        // Execute
        boolean result = as.addFilterCondition(val);

        // Assert
        assertTrue(result);
        assertEquals(1, as.getFilterConditions().size());
        assertTrue(as.getFilterConditions().contains(val));
    }

    @Test
    void testRemoveFilterCondition() {
        // Prepare
        String val = "fc";
        var as = new NonComparativeAnalysisSituation();
        as.addFilterCondition(val);

        // Execute
        boolean result = as.removeFilterCondition(val);

        // Assert
        assertTrue(result);
        assertEquals(0, as.getFilterConditions().size());
        assertFalse(as.getFilterConditions().contains(val));
    }
}
