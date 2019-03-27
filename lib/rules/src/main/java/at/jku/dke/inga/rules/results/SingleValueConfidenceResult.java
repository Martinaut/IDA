package at.jku.dke.inga.rules.results;

import at.jku.dke.inga.shared.display.Displayable;

/**
 * The value result contains the value and its calculated confidence.
 */
public class SingleValueConfidenceResult extends ConfidenceResult<Displayable> {

    /**
     * Instantiates a new instance of class {@link SingleValueConfidenceResult}.
     *
     * @param s the value
     */
    public SingleValueConfidenceResult(Displayable s) {
        super(s);
    }

    /**
     * Instantiates a new instance of class {@link SingleValueConfidenceResult}.
     *
     * @param s          the value
     * @param confidence the confidence
     */
    public SingleValueConfidenceResult(Displayable s, double confidence) {
        super(s, confidence);
    }

}
