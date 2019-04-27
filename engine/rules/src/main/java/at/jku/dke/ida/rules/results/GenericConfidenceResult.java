package at.jku.dke.ida.rules.results;

import java.util.StringJoiner;

/**
 * A generic confidence result implementing {@link ConfidenceResult}.
 *
 * @param <TValue> The type of the result value.
 */
public class GenericConfidenceResult<TValue> implements ConfidenceResult<TValue> {

    private TValue value;
    private double confidence;

    /**
     * Instantiates a new instance of class {@link ConfidenceResult} with confidence 1.
     *
     * @param value The value.
     */
    public GenericConfidenceResult(TValue value) {
        this.value = value;
        this.confidence = 1;
    }

    /**
     * Instantiates a new instance of class {@link ConfidenceResult}.
     *
     * @param value      The value.
     * @param confidence The confidence.
     */
    public GenericConfidenceResult(TValue value, double confidence) {
        this.value = value;
        this.confidence = confidence;
    }

    @Override
    public TValue getValue() {
        return value;
    }

    @Override
    public double getConfidence() {
        return confidence;
    }

    @Override
    public int compareTo(ConfidenceResult<TValue> other) {
        return Double.compare(other.getConfidence(), getConfidence());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GenericConfidenceResult.class.getSimpleName() + "[", "]")
                .add("value=" + value)
                .add("confidence=" + confidence)
                .toString();
    }
}
