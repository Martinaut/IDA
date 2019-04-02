package at.jku.dke.inga.rules.models;

import at.jku.dke.inga.shared.models.AnalysisSituation;
import at.jku.dke.inga.shared.models.DimensionQualification;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import com.google.common.graph.Graph;
import com.google.common.graph.ImmutableGraph;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Model required by {@link at.jku.dke.inga.rules.services.OperationDisplayService}.
 */
public class OperationDisplayServiceModel extends DroolsServiceModel {

    private final Collection<String> measures;
    private final Graph<String> granularityLevelHierarchy;
    private final Collection<Pair<String, String>> sliceConditions;

    /**
     * Instantiates a new instance of class {@link OperationDisplayServiceModel}.
     *
     * @param currentState              The current state of the state machine.
     * @param locale                    The display locale.
     * @param analysisSituation         The analysis situation.
     * @param operation                 The operation the user wants to perform.
     * @param additionalData            Additional data.
     * @param measures                  The measures of the selected cube.
     * @param granularityLevelHierarchy The granularity level hierarchy.
     * @throws IllegalArgumentException If any of the parameters is {@code null} (except {@code locale} and {@code additionalData}).
     */
    public OperationDisplayServiceModel(String currentState, Locale locale, AnalysisSituation analysisSituation, String operation, Map<String, Object> additionalData,
                                        Collection<String> measures, Graph<String> granularityLevelHierarchy, Collection<Pair<String, String>> sliceConditions) {
        super(currentState, locale, analysisSituation, operation, additionalData);

        if (measures == null) throw new IllegalArgumentException("measures must not be null");
        if (granularityLevelHierarchy == null)
            throw new IllegalArgumentException("granularityLevelHierarchy must not be null");
        if (sliceConditions == null)
            throw new IllegalArgumentException("sliceConditions must not be null");

        this.measures = Collections.unmodifiableCollection(measures);
        this.granularityLevelHierarchy = ImmutableGraph.copyOf(granularityLevelHierarchy);
        this.sliceConditions = Collections.unmodifiableCollection(sliceConditions);
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
        if (measures.isEmpty()) return Collections.emptySet();
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
    public Collection<String> getNextDrillDownLevels() { // TODO:
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

    // region --- SLICE CONDITIONS ---

    /**
     * Gets the slice conditions.
     *
     * @return the slice conditions
     */
    public Collection<Pair<String, String>> getSliceConditions() {
        return sliceConditions;
    }

    /**
     * Returns all level predicates which are already selected in the analysis situation.
     *
     * @return already selected level predicates
     */
    public Collection<Pair<String, String>> getSelectedSliceConditions() {
        if (!(getAnalysisSituation() instanceof NonComparativeAnalysisSituation)) return Collections.emptySet();
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) getAnalysisSituation();

        return as.getDimensionQualifications()
                .stream()
                .map(x -> new ImmutablePair<>(x.getDimension(), x.getSliceConditions()))
                .flatMap(x -> x.getRight().stream()
                        .map(p -> new ImmutablePair<>(x.getLeft(), p)))
                .collect(Collectors.toSet());

    }

    /**
     * Returns all level predicates which are not already selected in the analysis situation.
     *
     * @return not already selected level predicates
     */
    public Collection<Pair<String, String>> getNotSelectedSliceConditions() {
        if (sliceConditions.isEmpty()) return Collections.emptySet();
        if (!(getAnalysisSituation() instanceof NonComparativeAnalysisSituation)) return Collections.emptySet();
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) getAnalysisSituation();

        return sliceConditions.stream()
                .filter(x -> {
                    DimensionQualification dq = as.getDimensionQualification(x.getLeft());
                    if (dq == null) return false;
                    return !dq.getSliceConditions().contains(x.getRight());
                }).collect(Collectors.toSet());
    }

    /**
     * Returns all dimensions where a refocus of level predicates is possible.
     *
     * @return dimensions where refocus is possible
     */
    public Collection<String> getSliceConditionRefocusDimensions() {
        Set<String> addDims = getNotSelectedSliceConditions().stream().map(Pair::getLeft).collect(Collectors.toSet());
        Set<String> dropDims = getSelectedSliceConditions().stream().map(Pair::getLeft).collect(Collectors.toSet());
        addDims.retainAll(dropDims);
        return addDims;
    }
    // endregion

    // region --- TODO FILTERS ---
    // endregion

    // region --- TODO BASE MEASURE CONDITIONS ---
    // endregion
}
