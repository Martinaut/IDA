package at.jku.dke.ida.shared.models;

import at.jku.dke.ida.shared.models.generic.GenericNonComparativeAnalysisSituation;

/**
 * Non-comparative analysis situations represent multi-dimensional queries on cube instances.
 */
public class NonComparativeAnalysisSituation extends GenericNonComparativeAnalysisSituation<String, DimensionQualification> implements EngineAnalysisSituation {

    /**
     * Instantiates a new Non comparative analysis situation.
     */
    public NonComparativeAnalysisSituation() {
        super();
    }

    @Override
    public boolean isCubeDefined() {
        return super.isCubeDefined() && !getCube().isBlank();
    }
}
