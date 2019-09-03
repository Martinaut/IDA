package at.jku.dke.ida.shared.operations;

/**
 * Represents a pattern.
 */
public enum Pattern {
    /**
     * A simple query.
     */
    NON_COMPARATIVE,

    /**
     * A query comparing values of two different sets.
     */
    COMPARATIVE;

    /**
     * The constant for additional data containing a comparative analysis situation.
     */
    public static final String ADDITIONAL_DATA_COMPARATIVE = "comparativeAS";
}
