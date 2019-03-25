package at.jku.dke.inga.rules.models;

import at.jku.dke.inga.shared.models.AnalysisSituation;
import at.jku.dke.inga.shared.models.DimensionQualification;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import com.google.common.graph.Graph;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Model required by {@link at.jku.dke.inga.rules.services.OperationDisplayDeterminationService}.
 */
public class OperationDisplayDeterminationServiceModel extends DroolsServiceModel {

    private final Collection<String> measures;
    private final Graph<String> granularityLevelHierarchy;

    /**
     * Instantiates a new instance of class {@link DroolsServiceModel}.
     *
     * @param currentState              The current state of the state machine.
     * @param analysisSituation         The analysis situation.
     * @param locale                    The display locale.
     * @param measures                  The measures of the selected cube.
     * @param granularityLevelHierarchy The granularity level hierarchy.
     * @throws IllegalArgumentException If {@code currentState} or {@code analysisSituation} are {@code null} (or empty).
     */
    public OperationDisplayDeterminationServiceModel(String currentState, AnalysisSituation analysisSituation, Locale locale,
                                                     Collection<String> measures, Graph<String> granularityLevelHierarchy) {
        super(currentState, analysisSituation, locale);
        this.measures = measures;
        this.granularityLevelHierarchy = granularityLevelHierarchy;
    }


    // region --- MEASURES ---

    /**
     * Returns all measures which are not already selected in the analysis situation.
     *
     * @return not already selected measures
     */
    public Collection<String> getNotSelectedMeasures() {
        if (measures == null || measures.isEmpty()) return Collections.emptySet();
        if (!(getAnalysisSituation() instanceof NonComparativeAnalysisSituation)) return Collections.emptySet();
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) getAnalysisSituation();

        return measures.stream()
                .filter(x -> !as.getMeasures().contains(x))
                .collect(Collectors.toSet());

    }
    // endregion

    // region --- GRANULARITY LEVELS ---

    /**
     * Returns those levels to which you can drill down.
     *
     * @return Collection with more specific levels
     */
    @SuppressWarnings("Duplicates")
    public Collection<String> getNextDrillDownLevels() {
        if (granularityLevelHierarchy == null || granularityLevelHierarchy.nodes().isEmpty())
            return Collections.emptySet();
        if (!(getAnalysisSituation() instanceof NonComparativeAnalysisSituation)) return Collections.emptySet();

        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) getAnalysisSituation();
        Collection<String> result = new HashSet<>();

        for (DimensionQualification dq : as.getDimensionQualifications()) {
            result.addAll(this.granularityLevelHierarchy.predecessors(dq.getGranularityLevel()));
        }

        return result;
    }

    /**
     * Returns those levels to which you can roll up.
     *
     * @return Collection with more general levels
     */
    @SuppressWarnings("Duplicates")
    public Collection<String> getNextRollUpLevels() {
        if (granularityLevelHierarchy == null || granularityLevelHierarchy.nodes().isEmpty())
            return Collections.emptySet();
        if (!(getAnalysisSituation() instanceof NonComparativeAnalysisSituation)) return Collections.emptySet();

        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) getAnalysisSituation();
        Collection<String> result = new HashSet<>();

        for (DimensionQualification dq : as.getDimensionQualifications()) {
            result.addAll(this.granularityLevelHierarchy.successors(dq.getGranularityLevel()));
        }

        return result;
    }

    // endregion

    // region --- DICE NODES ---
    // endregion

    // region --- SLICE CONDITIONS ---
    // endregion

    // region --- FILTERS ---
    // endregion

    // region --- BASE MEASURE CONDITIONS ---
    // endregion

}
