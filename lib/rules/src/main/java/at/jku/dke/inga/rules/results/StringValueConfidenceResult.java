package at.jku.dke.inga.rules.results;

/**
 * The value result contains the value and its calculated confidence.
 */
public class StringValueConfidenceResult extends ConfidenceResult<String> {

    /**
     * Instantiates a new instance of class {@link StringValueConfidenceResult}.
     *
     * @param s the value
     */
    public StringValueConfidenceResult(String s) {
        super(s);
    }

    /**
     * Instantiates a new instance of class {@link StringValueConfidenceResult}.
     *
     * @param s          the value
     * @param confidence the confidence
     */
    public StringValueConfidenceResult(String s, double confidence) {
        super(s, confidence);
    }

}
