package at.jku.dke.inga.rules.results;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * The value result contains the value and its calculated confidence.
 */
public class TwoStringValueConfidenceResult extends ConfidenceResult<Pair<String, String>> {

    /**
     * Instantiates a new instance of class {@link TwoStringValueConfidenceResult}.
     *
     * @param stringStringPair the value
     */
    public TwoStringValueConfidenceResult(Pair<String, String> stringStringPair) {
        super(stringStringPair);
    }

    /**
     * Instantiates a new instance of class {@link TwoStringValueConfidenceResult}.
     *
     * @param stringStringPair the value
     * @param confidence       the confidence
     */
    public TwoStringValueConfidenceResult(Pair<String, String> stringStringPair, double confidence) {
        super(stringStringPair, confidence);
    }

    /**
     * Instantiates a new instance of class {@link TwoStringValueConfidenceResult}.
     *
     * @param valueLeft  the left value
     * @param valueRight the right value
     */
    public TwoStringValueConfidenceResult(String valueLeft, String valueRight) {
        super(new ImmutablePair<>(valueLeft, valueRight));
    }

    /**
     * Instantiates a new instance of class {@link TwoStringValueConfidenceResult}.
     *
     * @param valueLeft  the left value
     * @param valueRight the right value
     * @param confidence the confidence
     */
    public TwoStringValueConfidenceResult(String valueLeft, String valueRight, double confidence) {
        super(new ImmutablePair<>(valueLeft, valueRight), confidence);
    }
}
