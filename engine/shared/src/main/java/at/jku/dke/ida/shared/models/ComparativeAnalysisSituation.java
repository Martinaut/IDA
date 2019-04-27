package at.jku.dke.ida.shared.models;

import at.jku.dke.ida.shared.models.generic.GenericComparativeAnalysisSituation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * A comparative analysis situation allows to model comparison. It joins two analysis situations and relates
 * both by a score definition.
 */
public class ComparativeAnalysisSituation extends GenericComparativeAnalysisSituation<String, DimensionQualification, NonComparativeAnalysisSituation> {
    /**
     * Instantiates a new instance of class {@linkplain ComparativeAnalysisSituation}.
     */
    public ComparativeAnalysisSituation() {
        super();
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
}
