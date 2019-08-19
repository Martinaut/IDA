package at.jku.dke.ida.shared.models;

import at.jku.dke.ida.shared.models.generic.GenericComparativeAnalysisSituation;

/**
 * A comparative analysis situation allows to model comparison. It joins two analysis situations and relates
 * both by a score definition.
 */
public class ComparativeAnalysisSituation extends GenericComparativeAnalysisSituation<String, DimensionQualification, NonComparativeAnalysisSituation> implements EngineAnalysisSituation {

    private PatternType patternType;

    /**
     * Instantiates a new instance of class {@linkplain ComparativeAnalysisSituation}.
     */
    public ComparativeAnalysisSituation() {
        super(new NonComparativeAnalysisSituation(), new NonComparativeAnalysisSituation());
    }

    /**
     * Instantiates a new instance of class {@linkplain ComparativeAnalysisSituation}.
     *
     * @param contextOfInterest   The context of interest.
     * @param contextOfComparison The context of comparison.
     */
    public ComparativeAnalysisSituation(NonComparativeAnalysisSituation contextOfInterest, NonComparativeAnalysisSituation contextOfComparison) {
        super(contextOfInterest, contextOfComparison);
    }

    /**
     * Instantiates a new instance of class {@linkplain ComparativeAnalysisSituation}.
     *
     * @param patternType The pattern type.
     */
    public ComparativeAnalysisSituation(PatternType patternType) {
        super(new NonComparativeAnalysisSituation(), new NonComparativeAnalysisSituation());
        this.patternType = patternType;
    }

    /**
     * Instantiates a new instance of class {@linkplain ComparativeAnalysisSituation}.
     *
     * @param contextOfInterest   The context of interest.
     * @param contextOfComparison The context of comparison.
     * @param patternType         The pattern type.
     */
    public ComparativeAnalysisSituation(NonComparativeAnalysisSituation contextOfInterest, NonComparativeAnalysisSituation contextOfComparison, PatternType patternType) {
        super(contextOfInterest, contextOfComparison);
        this.patternType = patternType;
    }

    /**
     * Gets the pattern type.
     *
     * @return the pattern type
     */
    public PatternType getPatternType() {
        return patternType;
    }

    /**
     * Sets the pattern type.
     *
     * @param patternType the pattern type
     */
    public void setPatternType(PatternType patternType) {
        this.patternType = patternType;
    }
}
