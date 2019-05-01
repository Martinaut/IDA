package at.jku.dke.ida.rules.results;

/**
 * A confidence result where the result type is {@linkplain String}.
 */
public class StringConfidenceResult extends GenericConfidenceResult<String> {

    /**
     * Instantiates a new instance of class {@link StringConfidenceResult} with confidence 1.
     *
     * @param s The value.
     */
    public StringConfidenceResult(String s) {
        super(s);
    }

    /**
     * Instantiates a new instance of class {@link StringConfidenceResult}.
     *
     * @param s          The value.
     * @param confidence The confidence.
     */
    public StringConfidenceResult(String s, double confidence) {
        super(s, confidence);
    }

    /**
     * Instantiates a new instance of class {@link StringConfidenceResult} with confidence 1.
     *
     * @param s    The value.
     * @param term The term.
     */
    public StringConfidenceResult(String s, String term) {
        super(s, term);
    }

    /**
     * Instantiates a new instance of class {@link StringConfidenceResult}.
     *
     * @param s          The value.
     * @param confidence The confidence.
     * @param term       The term.
     */
    public StringConfidenceResult(String s, double confidence, String term) {
        super(s, confidence, term);
    }
}
