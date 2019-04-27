package at.jku.dke.ida.rules.results;

/**
 * The confidence result contains a value and the confidence of the value.
 *
 * @param <TValue> The type of the value.
 */
public interface ConfidenceResult<TValue> extends Comparable<ConfidenceResult<TValue>> {

    /**
     * Gets the value.
     *
     * @return the value
     */
    TValue getValue();

    /**
     * Gets the confidence.
     *
     * @return the confidence
     */
    double getConfidence();

    /**
     * Compares this result with the specified result for order. Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The calculation is performed based on the confidence in reverse order so that
     * the result with highest confidence is first.</p>
     *
     * @param other The result to be compared.
     * @return A negative integer, zero, or a positive integer as this result is less than, equal to, or greater than the specified object.
     * @throws NullPointerException Ff the specified result is null.
     */
    @Override
    int compareTo(ConfidenceResult<TValue> other);
}
