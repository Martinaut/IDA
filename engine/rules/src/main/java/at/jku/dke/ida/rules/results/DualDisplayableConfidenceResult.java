package at.jku.dke.ida.rules.results;

import at.jku.dke.ida.shared.display.Displayable;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * A confidence result where the result type is {@code Pair<Displayable, Displayable>}.
 */
public class DualDisplayableConfidenceResult extends GenericConfidenceResult<Pair<Displayable, Displayable>> {

    /**
     * Instantiates a new instance of class {@link DualDisplayableConfidenceResult} with confidence 1.
     *
     * @param value The value.
     */
    public DualDisplayableConfidenceResult(Pair<Displayable, Displayable> value) {
        super(value);
    }

    /**
     * Instantiates a new instance of class {@link DualDisplayableConfidenceResult}.
     *
     * @param value      The value.
     * @param confidence The confidence.
     */
    public DualDisplayableConfidenceResult(Pair<Displayable, Displayable> value, double confidence) {
        super(value, confidence);
    }


    /**
     * Instantiates a new instance of class {@link DualDisplayableConfidenceResult}.
     *
     * @param valueLeft  the left value
     * @param valueRight the right value
     */
    public DualDisplayableConfidenceResult(Displayable valueLeft, Displayable valueRight) {
        super(new ImmutablePair<>(valueLeft, valueRight));
    }

    /**
     * Instantiates a new instance of class {@link DualDisplayableConfidenceResult}.
     *
     * @param valueLeft  the left value
     * @param valueRight the right value
     * @param confidence the confidence
     */
    public DualDisplayableConfidenceResult(Displayable valueLeft, Displayable valueRight, double confidence) {
        super(new ImmutablePair<>(valueLeft, valueRight), confidence);
    }

    /**
     * Instantiates a new instance of class {@link DualDisplayableConfidenceResult} with confidence 1.
     *
     * @param displayableDisplayablePair The value.
     * @param term                       The term.
     */
    public DualDisplayableConfidenceResult(Pair<Displayable, Displayable> displayableDisplayablePair, String term) {
        super(displayableDisplayablePair, term);
    }

    /**
     * Instantiates a new instance of class {@link DualDisplayableConfidenceResult}.
     *
     * @param displayableDisplayablePair The value.
     * @param confidence                 The confidence.
     * @param term                       The term.
     */
    public DualDisplayableConfidenceResult(Pair<Displayable, Displayable> displayableDisplayablePair, double confidence, String term) {
        super(displayableDisplayablePair, confidence, term);
    }
}
