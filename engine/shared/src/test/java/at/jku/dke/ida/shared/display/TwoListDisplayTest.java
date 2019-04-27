package at.jku.dke.ida.shared.display;

import at.jku.dke.ida.shared.PojoTestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TwoListDisplayTest {
    @BeforeAll
    static void setEnglishLocale() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @Test
    void testOperationAccessors() {
        PojoTestUtils.validateAccessors(TwoListDisplay.class);
    }

    @Test
    void testConstructorWithNullLists() {
        // Execute
        TwoListDisplay d = new TwoListDisplay("addMeasure", null, null);

        // Assert
        assertNotNull(d.getDataLeft());
        assertTrue(d.getDataLeft().isEmpty());

        assertNotNull(d.getDataRight());
        assertTrue(d.getDataRight().isEmpty());
    }

    @Test
    void testConstructorWithLocaleAndNullLists() {
        // Execute
        TwoListDisplay d = new TwoListDisplay("addMeasure", Locale.GERMAN, null, null);

        // Assert
        assertNotNull(d.getDataLeft());
        assertTrue(d.getDataLeft().isEmpty());

        assertNotNull(d.getDataRight());
        assertTrue(d.getDataRight().isEmpty());
    }
}
