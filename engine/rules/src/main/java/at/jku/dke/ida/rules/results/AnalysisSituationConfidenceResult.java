package at.jku.dke.ida.rules.results;

import at.jku.dke.ida.shared.models.AnalysisSituation;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;

/**
 * A confidence result where the result type is {@linkplain AnalysisSituation}.
 */
public class AnalysisSituationConfidenceResult extends GenericConfidenceResult<EngineAnalysisSituation> {

    /**
     * Instantiates a new instance of class {@link AnalysisSituationConfidenceResult} with confidence 1.
     *
     * @param s The value.
     */
    public AnalysisSituationConfidenceResult(EngineAnalysisSituation s) {
        super(s);
    }

    /**
     * Instantiates a new instance of class {@link AnalysisSituationConfidenceResult}.
     *
     * @param s          The value.
     * @param confidence The confidence.
     */
    public AnalysisSituationConfidenceResult(EngineAnalysisSituation s, double confidence) {
        super(s, confidence);
    }

}