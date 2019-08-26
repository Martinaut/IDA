package at.jku.dke.ida.app.ruleset.helpers;

import at.jku.dke.ida.shared.ResourceBundleHelper;

import java.util.Locale;

/**
 * Used to represent keywords and their expression in a language.
 */
public enum Keyword {
    LEFT("left"),
    RIGHT("right"),
    OPTION("Option"),
    COMPARE("compare");

    private final String resourceKey;

    /**
     * Creates a new instance of enum Keyword.
     *
     * @param resourceKey The key in the resource file.
     */
    Keyword(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    /**
     * Returns the value from the resource bundle for the specified locale.
     *
     * @param locale The locale.
     * @return The value from the resource bundle.
     */
    public String getKeywordText(Locale locale) {
        return ResourceBundleHelper.getResourceString("ruleset.Keywords", locale, this.resourceKey);
    }
}