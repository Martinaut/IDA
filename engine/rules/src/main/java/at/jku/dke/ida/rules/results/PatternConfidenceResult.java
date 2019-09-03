package at.jku.dke.ida.rules.results;

import at.jku.dke.ida.shared.operations.Pattern;

/**
 * A confidence result where the result type is {@linkplain Pattern}.
 */
public class PatternConfidenceResult extends GenericConfidenceResult<Pattern> {

    /**
     * Instantiates a new instance of class {@link PatternConfidenceResult} with confidence 1.
     *
     * @param evt The pattern.
     */
    public PatternConfidenceResult(Pattern evt) {
        super(evt);
    }

    /**
     * Instantiates a new instance of class {@link PatternConfidenceResult}.
     *
     * @param evt        The pattern.
     * @param confidence The confidence.
     */
    public PatternConfidenceResult(Pattern evt, double confidence) {
        super(evt, confidence);
    }

    /**
     * Instantiates a new instance of class {@link PatternConfidenceResult} with confidence 1.
     *
     * @param pattern The value.
     * @param term  The term.
     */
    public PatternConfidenceResult(Pattern pattern, String term) {
        super(pattern, term);
    }

    /**
     * Instantiates a new instance of class {@link PatternConfidenceResult}.
     *
     * @param pattern      The value.
     * @param confidence The confidence.
     * @param term       The term.
     */
    public PatternConfidenceResult(Pattern pattern, double confidence, String term) {
        super(pattern, confidence, term);
    }
}
