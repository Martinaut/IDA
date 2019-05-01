package at.jku.dke.ida.rules.results;

import java.util.StringJoiner;

/**
 * A generic confidence result implementing {@link ConfidenceResult}.
 *
 * @param <TValue> The type of the result value.
 */
public class GenericConfidenceResult<TValue> implements ConfidenceResult<TValue> {

    private final String term;
    private final TValue value;
    private final double confidence;

    /**
     * Instantiates a new instance of class {@link GenericConfidenceResult} with confidence 1.
     *
     * @param value The value.
     */
    public GenericConfidenceResult(TValue value) {
        this.value = value;
        this.confidence = 1;
        this.term = null;
    }

    /**
     * Instantiates a new instance of class {@link GenericConfidenceResult}.
     *
     * @param value      The value.
     * @param confidence The confidence.
     */
    public GenericConfidenceResult(TValue value, double confidence) {
        this.value = value;
        this.confidence = confidence;
        this.term = null;
    }

    /**
     * Instantiates a new instance of class {@link GenericConfidenceResult} with confidence 1.
     *
     * @param value The value.
     * @param term  The term.
     */
    public GenericConfidenceResult(TValue value, String term) {
        this.term = term;
        this.value = value;
        this.confidence = 1;
    }

    /**
     * Instantiates a new instance of class {@link GenericConfidenceResult}.
     *
     * @param value      The value.
     * @param confidence The confidence.
     * @param term       The term.
     */
    public GenericConfidenceResult(TValue value, double confidence, String term) {
        this.term = term;
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
    public String getTerm() {
        return term;
    }

    @Override
    public int compareTo(ConfidenceResult<TValue> other) {
        return Double.compare(other.getConfidence(), getConfidence());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GenericConfidenceResult.class.getSimpleName() + "[", "]")
                .add("term=" + term)
                .add("value=" + value)
                .add("confidence=" + confidence)
                .toString();
    }
}
