package at.jku.dke.inga.scxml.session;

import at.jku.dke.inga.scxml.events.AnalysisSituationListener;
import at.jku.dke.inga.scxml.events.DisplayListener;
import at.jku.dke.inga.shared.EventNames;
import at.jku.dke.inga.shared.display.Display;
import at.jku.dke.inga.shared.display.ErrorDisplay;
import at.jku.dke.inga.shared.models.AnalysisSituation;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionContextModelTest {
    @Test
    void testConstructorWithNullSessionId() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> new SessionContextModel(null, "en", null, null));
        assertTrue(ex.getMessage().contains("sessionId"));
    }

    @Test
    void testConstructorWithEmptySessionId() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> new SessionContextModel("", "en", null, null));
        assertTrue(ex.getMessage().contains("sessionId"));
    }

    @Test
    void testConstructorWithWhitespaceSessionId() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> new SessionContextModel("  ", "en", null, null));
        assertTrue(ex.getMessage().contains("sessionId"));
    }

    @Test
    void testConstructorWithNullLocale() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> new SessionContextModel("ABC", null, null, null));
        assertTrue(ex.getMessage().contains("locale"));
    }

    @Test
    void testConstructorWithEmptyLocale() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> new SessionContextModel("ABC", "", null, null));
        assertTrue(ex.getMessage().contains("locale"));
    }

    @Test
    void testConstructorWithWhitespaceLocale() {
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> new SessionContextModel("ABC", "  ", null, null));
        assertTrue(ex.getMessage().contains("locale"));
    }

    @Test
    void testConstructor() {
        // Prepare
        final String sessionId = "asdfjweqr";
        final String locale = "en";
        final DisplayListener dListener = evt -> {
        };
        final AnalysisSituationListener asListener = evt -> {
        };

        // Execute
        SessionContextModel s = new SessionContextModel(sessionId, locale, dListener, asListener);

        // Assert
        assertEquals(sessionId, s.getSessionId());
        assertEquals(locale, s.getLocale().getLanguage());
        assertEquals(dListener, s.getDisplayListener());
        assertEquals(asListener, s.getAnalysisSituationListener());
        assertEquals(EventNames.NAVIGATE_CUBE_SELECT, s.getOperation());
        assertNotNull(s.getAnalysisSituation());
        assertNotNull(s.getAdditionalData());
        assertNull(s.getDisplayData());
        assertNull(s.getUserInput());
    }

    @Test
    void testTriggeringAnalysisSituationEvent() {
        // Prepare
        final String sessionId = "evtlistenertest";
        final String locale = "en";
        final AnalysisSituation as = new NonComparativeAnalysisSituation();
        final AnalysisSituationListener asListener = evt -> {
            assertNotNull(evt);
            assertEquals(as, evt.getAnalysisSituation());
            assertEquals(locale, evt.getLanguage());
            assertEquals(sessionId, evt.getSessionId());
        };
        final SessionContextModel s = new SessionContextModel(sessionId, locale, null, asListener);

        // Execute
        s.setAnalysisSituation(as);
    }

    @Test
    void testTriggeringDisplayDataEvent() {
        // Prepare
        final String sessionId = "evtlistenertest";
        final String locale = "en";
        final Display d = new ErrorDisplay("Das war ein Fehler.");
        final DisplayListener dListener = evt -> {
            assertNotNull(evt);
            assertEquals(d, evt.getDisplay());
            assertEquals(sessionId, evt.getSessionId());
        };
        final SessionContextModel s = new SessionContextModel(sessionId, locale, dListener, null);

        // Execute
        s.setDisplayData(d);
    }
}
