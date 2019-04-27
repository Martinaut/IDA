package at.jku.dke.ida.shared.models;

import at.jku.dke.ida.shared.PojoTestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DimensionQualificationTest {

    @Test
    void testOperationAccessors() {
        PojoTestUtils.validateAccessors(DimensionQualification.class);
    }

    @Test
    void testConstructorDefaults() {
        // Execute
        DimensionQualification dq = new DimensionQualification();

        // Assert
        assertNull(dq.getDimension());
        assertEquals("top", dq.getGranularityLevel());
        assertEquals("top", dq.getDiceLevel());
        assertEquals("all", dq.getDiceNode());
        assertTrue(dq.getSliceConditions().isEmpty());
    }

    @Test
    void testConstructorDefaultsWithDimension() {
        // Prepare
        final String dimName = "demo";

        // Execute
        DimensionQualification dq = new DimensionQualification(dimName);

        // Assert
        assertEquals(dimName, dq.getDimension());
        assertEquals("top", dq.getGranularityLevel());
        assertEquals("top", dq.getDiceLevel());
        assertEquals("all", dq.getDiceNode());
        assertTrue(dq.getSliceConditions().isEmpty());
    }

    @Test
    void testIsFilledTrue() {
        // Prepare
        DimensionQualification dq = new DimensionQualification("demoDim");

        // Execute
        boolean result = dq.isFilled();

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsFilledFalse() {
        // Prepare
        DimensionQualification dq = new DimensionQualification();

        // Execute
        boolean result = dq.isFilled();

        // Assert
        assertFalse(result);
    }

    @Test
    void testAddSliceCond() {
        // Prepare
        String sc = "slicecond";
        DimensionQualification dq = new DimensionQualification();

        // Execute
        boolean result = dq.addSliceCondition(sc);

        // Assert
        assertTrue(result);
        assertEquals(1, dq.getSliceConditions().size());
        assertTrue(dq.getSliceConditions().contains(sc));
    }


    @Test
    void testRemoveSliceCond() {
        // Prepare
        String sc = "slicecond";
        DimensionQualification dq = new DimensionQualification();
        dq.addSliceCondition(sc);

        // Execute
        boolean result = dq.removeSliceCondition(sc);

        // Assert
        assertTrue(result);
        assertEquals(0, dq.getSliceConditions().size());
        assertFalse(dq.getSliceConditions().contains(sc));
    }
}
