package at.jku.dke.ida.shared.models;

import at.jku.dke.ida.shared.models.generic.GenericNonComparativeAnalysisSituation;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
