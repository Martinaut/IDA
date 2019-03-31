package at.jku.dke.inga.rules.models;

import at.jku.dke.inga.shared.models.AnalysisSituation;
import at.jku.dke.inga.shared.models.DimensionQualification;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import com.google.common.graph.Graph;
import com.google.common.graph.ImmutableGraph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Model required by {@link at.jku.dke.inga.rules.services.OperationDisplayService}.
 */
public class OperationDisplayServiceModel extends DroolsServiceModel {

    private final Collection<String> measures;
    private final Graph<String> granularityLevelHierarchy;

    /**
     * Instantiates a new instance of class {@link OperationDisplayServiceModel}.
     *
     * @param currentState              The current state of the state machine.
     * @param analysisSituation         The analysis situation.
     * @param locale                    The display locale.
     * @param measures                  The measures of the selected cube.
     * @param granularityLevelHierarchy The granularity level hierarchy.
     * @throws IllegalArgumentException If any of the parameters is {@code null} (except {@code locale}).
     */
    public OperationDisplayServiceModel(String currentState, AnalysisSituation analysisSituation, Locale locale,
                                        Collection<String> measures, Graph<String> granularityLevelHierarchy) {
        super(currentState, analysisSituation, locale);

        if (measures == null) throw new IllegalArgumentException("measures must not be null");
        if (granularityLevelHierarchy == null)
            throw new IllegalArgumentException("granularityLevelHierarchy must not be null");

        this.measures = Collections.unmodifiableCollection(measures);
        this.granularityLevelHierarchy = ImmutableGraph.copyOf(granularityLevelHierarchy);
    }

    // region --- MEASURES ---

    /**
     * Returns all measures of the selected cube.
     *
     * @return All measures of the cube
     */
    public Collection<String> getMeasures() {
        return measures;
    }

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
     * Returns the granularity level hierarchy graph.
     *
     * @return the granularity level hierarchy graph
     */
    public Graph<String> getGranularityLevelHierarchy() {
        return granularityLevelHierarchy;
    }

    /**
     * Returns those levels to which you can drill down.
     *
     * @return Collection with more specific levels
     */
    @SuppressWarnings("Duplicates")
    public Collection<String> getNextDrillDownLevels() {
        if (granularityLevelHierarchy.nodes().isEmpty())
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
        if (granularityLevelHierarchy.nodes().isEmpty())
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

    // region --- TODO DICE NODES ---
    // endregion

    // region --- TODO SLICE CONDITIONS ---
    // endregion

    // region --- TODO FILTERS ---
    // endregion

    // region --- TODO BASE MEASURE CONDITIONS ---
    // endregion
}
