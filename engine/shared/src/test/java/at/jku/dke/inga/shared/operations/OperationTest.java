package at.jku.dke.inga.shared.operations;

import at.jku.dke.inga.shared.PojoTestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class OperationTest {

    private static final String DEFAULT_TEST_EVENT_NAME = "exit";
    private static final String DEFAULT_TEST_EVENT_TEXT = "Exit";
    private static final int DEFAULT_TEST_POSITION = 5;

    @BeforeAll
    static void setEnglishLocale() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @Test
    void testOperationAccessors() {
        PojoTestUtils.validateAccessors(Operation.class);
    }

    @Test
    void testGetDetails() {
        // Prepare
        final Operation op = new Operation(DEFAULT_TEST_EVENT_NAME, DEFAULT_TEST_POSITION);

        // Execute
        String result = op.getDetails();

        // Assert
        assertNull(result);
    }

    @Test
    void testGetTitle() {
        // Prepare
        final Operation op = new Operation(DEFAULT_TEST_EVENT_NAME, DEFAULT_TEST_POSITION);

        // Execute
        String result = op.getTitle();

        // Assert
        assertEquals(DEFAULT_TEST_EVENT_TEXT, result);
    }

    @Test
    void testGetDisplayableId() {
        // Prepare
        final Operation op = new Operation(DEFAULT_TEST_EVENT_NAME, DEFAULT_TEST_POSITION);

        // Execute
        String result = op.getDisplayableId();

        // Assert
        assertEquals(DEFAULT_TEST_EVENT_NAME, result);
    }

    @Test
    void testEventName() {
        // Prepare
        final Operation op = new Operation(DEFAULT_TEST_EVENT_NAME, DEFAULT_TEST_POSITION);

        // Execute
        String result = op.getEventName();

        // Assert
        assertEquals(DEFAULT_TEST_EVENT_NAME, result);
    }

    @Test
    void testDisplayName() {
        // Prepare
        final Operation op = new Operation(DEFAULT_TEST_EVENT_NAME, DEFAULT_TEST_POSITION);

        // Execute
        String result = op.getDisplayName();

        // Assert
        assertEquals(DEFAULT_TEST_EVENT_TEXT, result);
    }

    @Test
    void testPosition() {
        // Prepare
        final Operation op = new Operation(DEFAULT_TEST_EVENT_NAME, DEFAULT_TEST_POSITION);

        // Execute
        int result = op.getPosition();

        // Assert
        assertEquals(DEFAULT_TEST_POSITION, result);
    }

    @Test
    void testCompareToEquals() {
        // Prepare
        final Operation op1 = new Operation(DEFAULT_TEST_EVENT_NAME, DEFAULT_TEST_POSITION);
        final Operation op2 = new Operation(DEFAULT_TEST_EVENT_NAME, DEFAULT_TEST_POSITION);

        // Execute
        int result = op1.compareTo(op2);

        // Assert
        assertEquals(0, result);
    }

    @Test
    void testCompareToOtherBefore() {
        // Prepare
        final Operation op1 = new Operation(DEFAULT_TEST_EVENT_NAME, DEFAULT_TEST_POSITION);
        final Operation op2 = new Operation(DEFAULT_TEST_EVENT_NAME, DEFAULT_TEST_POSITION - 1);

        // Execute
        int result = op1.compareTo(op2);

        // Assert
        assertTrue(result > 0);
    }

    @Test
    void testCompareToOtherAfter() {
        // Prepare
        final Operation op1 = new Operation(DEFAULT_TEST_EVENT_NAME, DEFAULT_TEST_POSITION);
        final Operation op2 = new Operation(DEFAULT_TEST_EVENT_NAME, DEFAULT_TEST_POSITION + 1);

        // Execute
        int result = op1.compareTo(op2);

        // Assert
        assertTrue(result < 0);
    }

    @Test
    void testConstructor2() {
        // Prepare
        final Operation op = new Operation(DEFAULT_TEST_EVENT_NAME, Locale.GERMAN, DEFAULT_TEST_POSITION);

        // Execute
        int pos = op.getPosition();
        String evt = op.getEventName();
        String txt = op.getDisplayName();

        // Assert
        assertEquals(DEFAULT_TEST_POSITION, pos);
        assertEquals(DEFAULT_TEST_EVENT_NAME, evt);
        assertEquals("Beenden", txt);
    }

    @Test
    void testConstructor3() {
        // Prepare
        final Operation op = new Operation(DEFAULT_TEST_EVENT_NAME, "execute a Query", DEFAULT_TEST_POSITION);

        // Execute
        int pos = op.getPosition();
        String evt = op.getEventName();
        String txt = op.getDisplayName();

        // Assert
        assertEquals(DEFAULT_TEST_POSITION, pos);
        assertEquals(DEFAULT_TEST_EVENT_NAME, evt);
        assertEquals("execute a Query", txt);
    }

    @Test
    void testConstructor4() {
        // Prepare
        final Operation op = new Operation(DEFAULT_TEST_EVENT_NAME, "executeQuery", Locale.GERMAN, DEFAULT_TEST_POSITION);

        // Execute
        int pos = op.getPosition();
        String evt = op.getEventName();
        String txt = op.getDisplayName();

        // Assert
        assertEquals(DEFAULT_TEST_POSITION, pos);
        assertEquals(DEFAULT_TEST_EVENT_NAME, evt);
        assertEquals("Abfrage ausführen", txt);
    }
}
