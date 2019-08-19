package at.jku.dke.ida.shared.operations;

import at.jku.dke.ida.shared.display.SimpleDisplayable;

import java.util.Locale;

/**
 * Represents a pattern.
 */
public class Pattern extends SimpleDisplayable {

    // region --- STATIC ---
    private static String BUNDLE_NAME = "shared.Patterns";

    /**
     * Overrides the default bundle name ({@code shared.Patterns}) for pattern names.
     *
     * @param bundleName The bundle base name to use.
     */
    public static void setBundleName(String bundleName) {
        BUNDLE_NAME = bundleName;
    }

    /**
     * The constant COMPARATIVE.
     */
    public static final String COMPARATIVE = "comparative";

    @Deprecated
    public static final String COMPARATIVE_SETTOBASE = "comparative_settobase"; // TODO: comp
    @Deprecated
    public static final String COMPARATIVE_HOMOGENOUSINDEPENDENT = "comparative_homogen";
    @Deprecated
    public static final String COMPARATIVE_HETEROGENOUSINDEPENDENT = "comparative_heterogen";

    /**
     * The constant NON-COMPARATIVE.
     */
    public static final String NONCOMPARATIVE = "noncomparative";

    /**
     * The constant for additional data containing a comparative analysis situation.
     */
    public static final String ADD_DATA_COMPARATIVE = "comparativeAS";

    /**
     * The set of interest constant.
     */
    public static final String SI = "set-of-interest";

    /**
     * The set of interest comparison.
     */
    public static final String SC = "set-of-comparison";
    // endregion

    /**
     * Instantiates a new instance of class {@linkplain SimpleDisplayable}.
     *
     * @param pattern the displayable id
     */
    public Pattern(String pattern, Locale locale) {
        super(pattern, locale, BUNDLE_NAME, pattern);
        if (pattern == null || !pattern.equals(COMPARATIVE) && !pattern.equals(NONCOMPARATIVE) &&
                !pattern.equals(COMPARATIVE_SETTOBASE) && !pattern.equals(COMPARATIVE_HOMOGENOUSINDEPENDENT) && !pattern.equals(COMPARATIVE_HETEROGENOUSINDEPENDENT))  // TODO: comp
            throw new IllegalArgumentException("pattern is invalid");
    }

}
