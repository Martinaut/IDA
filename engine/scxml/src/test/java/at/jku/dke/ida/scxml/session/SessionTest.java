package at.jku.dke.ida.scxml.session;

import at.jku.dke.ida.scxml.events.AnalysisSituationListener;
import at.jku.dke.ida.scxml.events.DisplayListener;
import at.jku.dke.ida.scxml.exceptions.StateMachineInstantiationException;
import at.jku.dke.ida.shared.EventNames;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {
    @Test
    void testConstructorWithNullSessionId() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> new Session(null, "en", null, null));
        assertTrue(ex.getMessage().contains("sessionId"));
    }

    @Test
    void testConstructorWithEmptySessionId() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> new Session("", "en", null, null));
        assertTrue(ex.getMessage().contains("sessionId"));
    }

    @Test
    void testConstructorWithWhitespaceSessionId() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> new Session("  ", "en", null, null));
        assertTrue(ex.getMessage().contains("sessionId"));
    }

    @Test
    void testConstructorWithNullLocale() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> new Session("ABC", null, null, null));
        assertTrue(ex.getMessage().contains("locale"));
    }

    @Test
    void testConstructorWithEmptyLocale() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> new Session("ABC", "", null, null));
        assertTrue(ex.getMessage().contains("locale"));
    }

    @Test
    void testConstructorWithWhitespaceLocale() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> new Session("ABC", "  ", null, null));
        assertTrue(ex.getMessage().contains("locale"));
    }

    @Test
    void testConstructor() throws StateMachineInstantiationException {
        // Prepare
        final String sessionId = "asdfjweqr";
        final String locale = "en";
        final DisplayListener dListener = evt -> {
        };
        final AnalysisSituationListener asListener = evt -> {
        };

        // Execute
        Session s = new Session(sessionId, locale, dListener, asListener);

        // Assert
        assertEquals(sessionId, s.getSessionId());
        assertNotNull(s.getSessionContextModel());
        assertFalse(s.isInFinalState());
        assertEquals(locale, s.getSessionContextModel().getLocale().getLanguage());
        assertEquals(dListener, s.getSessionContextModel().getDisplayListener());
        assertEquals(asListener, s.getSessionContextModel().getAnalysisSituationListener());
    }

    @Test
    void testTriggerUserInputEventWithNullInput() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> new Session("ABC", "de", null, null).triggerUserInputEvent(null));
        assertTrue(ex.getMessage().contains("userInput"));
    }

    @Test
    void testSetCubeSetFlagWithTrueValue() throws StateMachineInstantiationException {
        // Prepare
        Session s = new Session("ABC", "en", null, null);

        // Execute
        s.setCubeSetFlag(true);

        // Assert
        assertNull(s.getSessionContextModel().getOperation());
    }

    @Test
    void testSetCubeSetFlagWithFalseValue() throws StateMachineInstantiationException {
        // Prepare
        Session s = new Session("ABC", "en", null, null);

        // Execute
        s.setCubeSetFlag(false);

        // Assert
        assertEquals(EventNames.NAVIGATE_CUBE_SELECT, s.getSessionContextModel().getOperation());
    }
}
