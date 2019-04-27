package at.jku.dke.ida.shared.display;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ErrorDisplayTest {
    @BeforeAll
    static void setEnglishLocale() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @Test
    void testConstructorWithoutLocale() {
        // Prepare
        final String msg = "addMeasure";

        // Execute
        ErrorDisplay d = new ErrorDisplay(msg);

        // Assert
        assertEquals(msg, d.getDisplayMessage());
    }

    @Test
    void testConstructorWithLocale() {
        // Prepare
        final String msg = "addMeasure";

        // Execute
        ErrorDisplay d = new ErrorDisplay(msg, Locale.GERMAN);

        // Assert
        assertEquals("Bitte wählen Sie eine der folgenden Measures aus, um diese hinzuzufügen:", d.getDisplayMessage());
    }
}
