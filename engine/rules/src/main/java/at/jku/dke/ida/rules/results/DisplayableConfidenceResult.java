package at.jku.dke.ida.rules.results;

import at.jku.dke.ida.shared.display.Displayable;

/**
 * A confidence result where the result type is {@linkplain Displayable}.
 */
public class DisplayableConfidenceResult extends GenericConfidenceResult<Displayable> {

    /**
     * Instantiates a new instance of class {@link DisplayableConfidenceResult} with confidence 1.
     *
     * @param value The value.
     */
    public DisplayableConfidenceResult(Displayable value) {
        super(value);
    }

    /**
     * Instantiates a new instance of class {@link DisplayableConfidenceResult}.
     *
     * @param value      The value.
     * @param confidence The confidence.
     */
    public DisplayableConfidenceResult(Displayable value, double confidence) {
        super(value, confidence);
    }

    /**
     * Instantiates a new instance of class {@link DisplayableConfidenceResult} with confidence 1.
     *
     * @param displayable The value.
     * @param term        The term.
     */
    public DisplayableConfidenceResult(Displayable displayable, String term) {
        super(displayable, term);
    }

    /**
     * Instantiates a new instance of class {@link DisplayableConfidenceResult}.
     *
     * @param displayable The value.
     * @param confidence  The confidence.
     * @param term        The term.
     */
    public DisplayableConfidenceResult(Displayable displayable, double confidence, String term) {
        super(displayable, confidence, term);
    }
}
