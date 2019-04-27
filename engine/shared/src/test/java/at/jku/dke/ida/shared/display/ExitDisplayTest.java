package at.jku.dke.ida.shared.display;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExitDisplayTest {
    @BeforeAll
    static void setEnglishLocale() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @Test
    void testConstructorWithoutLocale() {
        // Execute
        ExitDisplay d = new ExitDisplay();

        // Assert
        assertEquals("Goodbye.", d.getDisplayMessage());
    }

    @Test
    void testConstructorWithLocale() {
        // Execute
        ExitDisplay d = new ExitDisplay(Locale.GERMAN);

        // Assert
        assertEquals("Auf Wiedersehen.", d.getDisplayMessage());
    }
}
