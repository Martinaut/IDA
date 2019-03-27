package at.jku.dke.inga.rules.results;

import at.jku.dke.inga.shared.display.Displayable;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * The value result contains the value and its calculated confidence.
 */
public class DualValueConfidenceResult extends ConfidenceResult<Pair<Displayable, Displayable>> {

    /**
     * Instantiates a new instance of class {@link DualValueConfidenceResult}.
     *
     * @param stringStringPair the value
     */
    public DualValueConfidenceResult(Pair<Displayable, Displayable> stringStringPair) {
        super(stringStringPair);
    }

    /**
     * Instantiates a new instance of class {@link DualValueConfidenceResult}.
     *
     * @param stringStringPair the value
     * @param confidence       the confidence
     */
    public DualValueConfidenceResult(Pair<Displayable, Displayable> stringStringPair, double confidence) {
        super(stringStringPair, confidence);
    }

    /**
     * Instantiates a new instance of class {@link DualValueConfidenceResult}.
     *
     * @param valueLeft  the left value
     * @param valueRight the right value
     */
    public DualValueConfidenceResult(Displayable valueLeft, Displayable valueRight) {
        super(new ImmutablePair<>(valueLeft, valueRight));
    }

    /**
     * Instantiates a new instance of class {@link DualValueConfidenceResult}.
     *
     * @param valueLeft  the left value
     * @param valueRight the right value
     * @param confidence the confidence
     */
    public DualValueConfidenceResult(Displayable valueLeft, Displayable valueRight, double confidence) {
        super(new ImmutablePair<>(valueLeft, valueRight), confidence);
    }
}
