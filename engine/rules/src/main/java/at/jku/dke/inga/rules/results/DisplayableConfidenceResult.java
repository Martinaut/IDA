package at.jku.dke.inga.rules.results;

import at.jku.dke.inga.shared.display.Displayable;

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

}
