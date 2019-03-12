package at.jku.dke.inga.rules.helpers;

/**
 * The value result contains the value and its calculated confidence.
 */
public class ConfidenceResult implements Comparable<ConfidenceResult> {

    private String value;
    private double confidence;

    /**
     * Instantiates a new Intent result.
     *
     * @param value the value
     */
    public ConfidenceResult(String value) {
        this.value = value;
        this.confidence = 1;
    }

    /**
     * Instantiates a new Intent result.
     *
     * @param value     the value
     * @param confidence the confidence
     */
    public ConfidenceResult(String value, double confidence) {
        this.value = value;
        this.confidence = confidence;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value the value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the confidence.
     *
     * @return the confidence
     */
    public double getConfidence() {
        return confidence;
    }

    /**
     * Sets the confidence.
     *
     * @param confidence the confidence
     */
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    /**
     * Compares this object with the specified object for order. Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * @param other the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     */
    @Override
    public int compareTo(ConfidenceResult other) {
        return Double.compare(other.confidence, confidence);
    }
}
