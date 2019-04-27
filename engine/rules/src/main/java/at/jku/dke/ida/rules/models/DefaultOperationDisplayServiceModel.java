package at.jku.dke.ida.rules.models;

import at.jku.dke.ida.rules.interfaces.OperationDisplayServiceModel;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.models.AnalysisSituation;
import at.jku.dke.ida.shared.models.DimensionQualification;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;
import com.google.common.graph.Graph;
import com.google.common.graph.ImmutableGraph;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Default model used by {@link at.jku.dke.ida.rules.services.OperationDisplayService}.
 */
public class DefaultOperationDisplayServiceModel extends AbstractServiceModel implements OperationDisplayServiceModel {

    private final Collection<String> measures;// TODO: all needed?
    private final Graph<String> granularityLevelHierarchy;
    private final Collection<Pair<String, String>> sliceConditions;
    private final Collection<Triple<String, String, String>> diceNodes;
    private final Collection<String> baseMeasureConditions;
    private final Collection<String> filters;

    /**
     * Instantiates a new instance of class {@link AbstractServiceModel}.
     *
     * @param currentState              The current state of the state machine.
     * @param sessionModel              The session model.
     * @param measures                  The measures of the selected cube.
     * @param granularityLevelHierarchy The granularity level hierarchy.
     * @param sliceConditions           The slice conditions.
     * @param baseMeasureConditions     The base measure conditions.
     * @param filters                   The filters.
     * @param diceNodes                 The dice nodes.
     * @throws IllegalArgumentException If the any of the parameters is {@code null} or empty.
     */
    public DefaultOperationDisplayServiceModel(String currentState, SessionModel sessionModel, Collection<String> measures, Graph<String> granularityLevelHierarchy,
                                               Collection<Pair<String, String>> sliceConditions, Collection<Triple<String, String, String>> diceNodes,
                                               Collection<String> baseMeasureConditions, Collection<String> filters) {
        super(currentState, sessionModel);

        if (measures == null) throw new IllegalArgumentException("measures must not be null");
        if (granularityLevelHierarchy == null)
            throw new IllegalArgumentException("granularityLevelHierarchy must not be null");
        if (sliceConditions == null)
            throw new IllegalArgumentException("sliceConditions must not be null");
        if (baseMeasureConditions == null)
            throw new IllegalArgumentException("baseMeasureConditions must not be null");
        if (filters == null)
            throw new IllegalArgumentException("filters must not be null");

        this.measures = Collections.unmodifiableCollection(measures);
        this.granularityLevelHierarchy = ImmutableGraph.copyOf(granularityLevelHierarchy);
        this.sliceConditions = Collections.unmodifiableCollection(sliceConditions);
        this.baseMeasureConditions = Collections.unmodifiableCollection(baseMeasureConditions);
        this.filters = Collections.unmodifiableCollection(filters);
        this.diceNodes = Collections.unmodifiableCollection(diceNodes);
    }

    /**
     * Instantiates a new instance of class {@link AbstractServiceModel}.
     *
     * @param currentState              The current state of the state machine.
     * @param locale                    The display locale.
     * @param analysisSituation         The analysis situation.
     * @param operation                 The operation the user wants to perform.
     * @param sessionModel              The session model.
     * @param measures                  The measures of the selected cube.
     * @param granularityLevelHierarchy The granularity level hierarchy.
     * @param sliceConditions           The slice conditions.
     * @param baseMeasureConditions     The base measure conditions.
     * @param filters                   The filters.
     * @param diceNodes                 The dice nodes.
     * @throws IllegalArgumentException If the any of the parameters (except {@code locale} and {@code operation}) is {@code null} or empty.
     */
    public DefaultOperationDisplayServiceModel(String currentState, Locale locale, EngineAnalysisSituation analysisSituation, Event operation, SessionModel sessionModel,
                                               Collection<String> measures, Graph<String> granularityLevelHierarchy, Collection<Pair<String, String>> sliceConditions,
                                               Collection<Triple<String, String, String>> diceNodes, Collection<String> baseMeasureConditions, Collection<String> filters) {
        super(currentState, locale, analysisSituation, operation, sessionModel);

        if (measures == null) throw new IllegalArgumentException("measures must not be null");
        if (granularityLevelHierarchy == null)
            throw new IllegalArgumentException("granularityLevelHierarchy must not be null");
        if (sliceConditions == null)
            throw new IllegalArgumentException("sliceConditions must not be null");
        if (baseMeasureConditions == null)
            throw new IllegalArgumentException("baseMeasureConditions must not be null");
        if (filters == null)
            throw new IllegalArgumentException("filters must not be null");

        this.measures = Collections.unmodifiableCollection(measures);
        this.granularityLevelHierarchy = ImmutableGraph.copyOf(granularityLevelHierarchy);
        this.sliceConditions = Collections.unmodifiableCollection(sliceConditions);
        this.baseMeasureConditions = Collections.unmodifiableCollection(baseMeasureConditions);
        this.filters = Collections.unmodifiableCollection(filters);
        this.diceNodes = Collections.unmodifiableCollection(diceNodes);
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

    // region --- DICE NODES ---

    /**
     * Gets the dice nodes.
     *
     * @return the dice nodes
     */
    public Collection<Triple<String, String, String>> getDiceNodes() {
        return diceNodes;
    }

    /**
     * Returns all dice nodes which are already selected in the analysis situation.
     *
     * @return already selected dice nodes
     */
    public Collection<Triple<String, String, String>> getSelectedDiceNodes() {
        if (!(getAnalysisSituation() instanceof NonComparativeAnalysisSituation)) return Collections.emptySet();
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) getAnalysisSituation();

        return diceNodes.stream()
                .filter(x -> {
                    DimensionQualification dq = as.getDimensionQualification(x.getLeft());
                    if (dq == null || dq.getDiceNode() == null) return false;
                    return dq.getDiceNode().equals(x.getRight());
                }).collect(Collectors.toSet());
    }

    /**
     * Returns all dice nodes which are not already selected in the analysis situation.
     *
     * @return not already selected dice nodes
     */
    public Collection<Triple<String, String, String>> getNotSelectedDiceNodes() {
        if (sliceConditions.isEmpty()) return Collections.emptySet();
        if (!(getAnalysisSituation() instanceof NonComparativeAnalysisSituation)) return Collections.emptySet();
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) getAnalysisSituation();

        return diceNodes.stream()
                .filter(x -> {
                    DimensionQualification dq = as.getDimensionQualification(x.getLeft());
                    if (dq == null) return false;
                    if (dq.getDiceNode() == null) return true;
                    return !dq.getDiceNode().equals(x.getRight());
                }).collect(Collectors.toSet());
    }
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

    // region --- FILTERS ---

    /**
     * Returns all filters of the selected cube.
     *
     * @return All filters of the cube
     */
    public Collection<String> getFilters() {
        return filters;
    }

    /**
     * Returns all filters which are not already selected in the analysis situation.
     *
     * @return not already selected filters
     */
    public Collection<String> getNotSelectedFilters() {
        if (filters.isEmpty()) return Collections.emptySet();
        if (!(getAnalysisSituation() instanceof NonComparativeAnalysisSituation)) return Collections.emptySet();
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) getAnalysisSituation();

        return filters.stream()
                .filter(x -> !as.getFilterConditions().contains(x))
                .collect(Collectors.toSet());

    }
    // endregion

    // region --- BASE MEASURE CONDITIONS ---

    /**
     * Returns all base measure conditions of the selected cube.
     *
     * @return All base measure conditions of the cube
     */
    public Collection<String> getBaseMeasureConditions() {
        return baseMeasureConditions;
    }

    /**
     * Returns all base measure conditions which are not already selected in the analysis situation.
     *
     * @return not already selected base measure conditions
     */
    public Collection<String> getNotSelectedBaseMeasureConditions() {
        if (baseMeasureConditions.isEmpty()) return Collections.emptySet();
        if (!(getAnalysisSituation() instanceof NonComparativeAnalysisSituation)) return Collections.emptySet();
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) getAnalysisSituation();

        return baseMeasureConditions.stream()
                .filter(x -> !as.getBaseMeasureConditions().contains(x))
                .collect(Collectors.toSet());

    }
    // endregion

    /**
     * Returns whether there are measures in the cube which are not already selected in the analysis situation.
     *
     * @return {@code true} if there are measures available for selection; {@code false} otherwise.
     */
    @Override
    public boolean areNotSelectedMeasuresAvailable() {
        return !getNotSelectedMeasures().isEmpty();
    }

    /**
     * Returns whether there are filter conditions conditions in the cube which are not already selected in the analysis situation.
     *
     * @return {@code true} if there are filter conditions available for selection; {@code false} otherwise.
     */
    @Override
    public boolean areNotSelectedFilterConditionsAvailable() {
        return !getNotSelectedFilters().isEmpty();
    }

    /**
     * Returns whether there are base measure conditions in the cube which are not already selected in the analysis situation.
     *
     * @return {@code true} if there are base measure conditions available for selection; {@code false} otherwise.
     */
    @Override
    public boolean areNotSelectedBaseMeasureConditionsAvailable() {
        return !getNotSelectedBaseMeasureConditions().isEmpty();
    }
}
