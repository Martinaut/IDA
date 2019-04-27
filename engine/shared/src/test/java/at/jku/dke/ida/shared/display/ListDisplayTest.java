package at.jku.dke.ida.shared.display;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ListDisplayTest {
    @BeforeAll
    static void setEnglishLocale() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @Test
    void testConstructorWithNullList() {
        // Execute
        ListDisplay d = new ListDisplay("addMeasure", null);

        // Assert
        assertNotNull(d.getData());
        assertTrue(d.getData().isEmpty());
    }

    @Test
    void testConstructorWithNullIterable() {
        // Execute
        ListDisplay d = new ListDisplay("addMeasure", (Iterable)null);

        // Assert
        assertNotNull(d.getData());
        assertTrue(d.getData().isEmpty());
    }

    @Test
    void testConstructorWithLocaleAndNullList() {
        // Execute
        ListDisplay d = new ListDisplay("addMeasure", Locale.GERMAN, null);

        // Assert
        assertNotNull(d.getData());
        assertTrue(d.getData().isEmpty());
    }
}
