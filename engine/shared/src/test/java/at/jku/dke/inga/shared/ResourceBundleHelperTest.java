package at.jku.dke.inga.shared;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ResourceBundleHelperTest {
    @BeforeAll
    static void setEnglishLocale() {
        Locale.setDefault(Locale.ENGLISH);
    }

    // region --- Without Locale ---
    @Test
    void testGetStringWithoutLocale_BundleNameNull() {
        // Execute
        Exception ex = assertThrows(NullPointerException.class, () -> ResourceBundleHelper.getResourceString(null, "addMeasure"));

        // Assert
        assertNotNull(ex);
    }

    @Test
    void testGetStringWithoutLocale_ResourceNameNull() {
        // Execute
        Exception ex = assertThrows(NullPointerException.class, () -> ResourceBundleHelper.getResourceString("DisplayMessages", null));

        // Assert
        assertNotNull(ex);
    }

    @Test
    void testGetStringWithoutLocale_BothNull() {
        // Execute
        Exception ex = assertThrows(NullPointerException.class, () -> ResourceBundleHelper.getResourceString(null, null));

        // Assert
        assertNotNull(ex);
    }

    @Test
    void testGetStringWithoutLocale_InvalidBundleName() {
        // Prepare
        final String bundleName = "AnInvalidBundle";
        final String resourceName = "addMeasure";

        // Execute
        String result = ResourceBundleHelper.getResourceString(bundleName, resourceName);

        // Assert
        assertEquals(resourceName, result);
    }

    @Test
    void testGetStringWithoutLocale_InvalidResourceName() {
        // Prepare
        final String bundleName = "DisplayMessages";
        final String resourceName = "InvalidResource";

        // Execute
        String result = ResourceBundleHelper.getResourceString(bundleName, resourceName);

        // Assert
        assertEquals(resourceName, result);
    }

    @Test
    void testGetStringWithoutLocale_Valid() {
        // Prepare
        final String bundleName = "DisplayMessages";
        final String resourceName = "addMeasure";

        // Execute
        String result = ResourceBundleHelper.getResourceString(bundleName, resourceName);

        // Assert
        assertEquals("Please choose one of the following measures to add:", result);
    }
    // endregion

    // region --- With Locale ---
    @Test
    void testGetStringWithLocale_BundleNameNull() {
        // Execute
        Exception ex = assertThrows(NullPointerException.class, () -> ResourceBundleHelper.getResourceString(null, Locale.GERMAN, "addMeasure"));

        // Assert
        assertNotNull(ex);
    }

    @Test
    void testGetStringWithLocale_ResourceNameNull() {
        // Execute
        Exception ex = assertThrows(NullPointerException.class, () -> ResourceBundleHelper.getResourceString("DisplayMessages", Locale.GERMAN, null));

        // Assert
        assertNotNull(ex);
    }

    @Test
    void testGetStringWithLocale_LocaleNameNull() {
        // Execute
        Exception ex = assertThrows(NullPointerException.class, () -> ResourceBundleHelper.getResourceString("DisplayMessages", null, "addMeasure"));

        // Assert
        assertNotNull(ex);
    }

    @Test
    void testGetStringWithLocale_AllNull() {
        // Execute
        Exception ex = assertThrows(NullPointerException.class, () -> ResourceBundleHelper.getResourceString(null, null, null));

        // Assert
        assertNotNull(ex);
    }

    @Test
    void testGetStringWithLocale_InvalidBundleName() {
        // Prepare
        final String bundleName = "AnInvalidBundle";
        final String resourceName = "addMeasure";

        // Execute
        String result = ResourceBundleHelper.getResourceString(bundleName, Locale.GERMAN, resourceName);

        // Assert
        assertEquals(resourceName, result);
    }

    @Test
    void testGetStringWithLocale_InvalidResourceName() {
        // Prepare
        final String bundleName = "DisplayMessages";
        final String resourceName = "InvalidResource";

        // Execute
        String result = ResourceBundleHelper.getResourceString(bundleName, Locale.GERMAN, resourceName);

        // Assert
        assertEquals(resourceName, result);
    }

    @Test
    void testGetStringWithLocale_Valid() {
        // Prepare
        final String bundleName = "DisplayMessages";
        final String resourceName = "addMeasure";

        // Execute
        String result = ResourceBundleHelper.getResourceString(bundleName, Locale.GERMAN, resourceName);

        // Assert
        assertEquals("Bitte wählen Sie eine der folgenden Measures aus, um diese hinzuzufügen:", result);
    }
    // endregion
}
