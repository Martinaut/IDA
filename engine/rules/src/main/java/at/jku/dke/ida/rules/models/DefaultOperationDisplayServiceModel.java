package at.jku.dke.ida.rules.models;

import at.jku.dke.ida.rules.interfaces.OperationDisplayServiceModel;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.IRIConstants;
import at.jku.dke.ida.shared.models.DimensionQualification;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;
import com.google.common.graph.Graph;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Default model used by {@link at.jku.dke.ida.rules.services.OperationDisplayService}.
 */
public class DefaultOperationDisplayServiceModel extends AbstractServiceModel implements OperationDisplayServiceModel {

    private final Collection<String> notSelectedMeasures;
    private final Collection<String> notSelectedFilters;
    private final Collection<String> notSelectedBaseMeasureConditions;
    private final Graph<String> granularityLevelHierarchy;
    private final Graph<String> sliceConditionHierarchy;
    private final Set<Pair<String, String>> sliceConditions;
    private final Collection<Triple<String, String, String>> diceNodes;
    private final Collection<String> notSelectedJoinConditions;
    private final Collection<String> notSelectedScores;
    private final Collection<String> notSelectedScoreFilters;

    /**
     * Instantiates a new instance of class {@link AbstractServiceModel}.
     *
     * @param currentState                     The current state of the state machine.
     * @param sessionModel                     The session model.
     * @param notSelectedMeasures              The not selected measures.
     * @param notSelectedFilters               The not selected filters.
     * @param notSelectedBaseMeasureConditions The not selected base measure conditions.
     * @param granularityLevelHierarchy        The granularity level hierarchy.
     * @param sliceConditionHierarchy          The slice condition hierarchy.
     * @param sliceConditions                  The slice conditions.
     * @param diceNodes                        The dice nodes.
     * @param notSelectedJoinConditions        The not selected join conditions.
     * @param notSelectedScores                The not selected scores.
     * @param notSelectedScoreFilters          The not selected score filters.
     * @throws IllegalArgumentException If the any of the parameters is {@code null} or empty.
     */
    public DefaultOperationDisplayServiceModel(String currentState, SessionModel sessionModel, Collection<String> notSelectedMeasures,
                                               Collection<String> notSelectedFilters, Collection<String> notSelectedBaseMeasureConditions,
                                               Graph<String> granularityLevelHierarchy, Graph<String> sliceConditionHierarchy,
                                               Set<Pair<String, String>> sliceConditions, Collection<Triple<String, String, String>> diceNodes,
                                               Collection<String> notSelectedJoinConditions, Collection<String> notSelectedScores,
                                               Collection<String> notSelectedScoreFilters) {
        super(currentState, sessionModel);

        if (notSelectedFilters == null) throw new IllegalArgumentException("notSelectedFilters must not be null");
        if (granularityLevelHierarchy == null)
            throw new IllegalArgumentException("granularityLevelHierarchy must not be null");
        if (sliceConditionHierarchy == null)
            throw new IllegalArgumentException("sliceConditionHierarchy must not be null");
        if (sliceConditions == null)
            throw new IllegalArgumentException("sliceConditions must not be null");
        if (notSelectedBaseMeasureConditions == null)
            throw new IllegalArgumentException("notSelectedBaseMeasureConditions must not be null");
        if (diceNodes == null)
            throw new IllegalArgumentException("diceNodes must not be null");
        if (notSelectedJoinConditions == null)
            throw new IllegalArgumentException("notSelectedJoinConditions must not be null");
        if (notSelectedScores == null)
            throw new IllegalArgumentException("notSelectedScores must not be null");
        if (notSelectedScoreFilters == null)
            throw new IllegalArgumentException("notSelectedScoreFilters must not be null");

        this.notSelectedMeasures = notSelectedMeasures;
        this.notSelectedFilters = notSelectedFilters;
        this.notSelectedBaseMeasureConditions = notSelectedBaseMeasureConditions;
        this.granularityLevelHierarchy = granularityLevelHierarchy;
        this.sliceConditionHierarchy = sliceConditionHierarchy;
        this.sliceConditions = sliceConditions;
        this.diceNodes = diceNodes;
        this.notSelectedJoinConditions = notSelectedJoinConditions;
        this.notSelectedScores = notSelectedScores;
        this.notSelectedScoreFilters = notSelectedScoreFilters;
    }

    /**
     * Instantiates a new instance of class {@link AbstractServiceModel}.
     *
     * @param currentState                     The current state of the state machine.
     * @param locale                           The display locale.
     * @param analysisSituation                The analysis situation.
     * @param operation                        The operation the user wants to perform.
     * @param sessionModel                     The session model.
     * @param notSelectedMeasures              The not selected measures.
     * @param notSelectedFilters               The not selected filters.
     * @param notSelectedBaseMeasureConditions The not selected base measure conditions.
     * @param granularityLevelHierarchy        The granularity level hierarchy.
     * @param sliceConditionHierarchy          The slice condition hierarchy.
     * @param sliceConditions                  The slice conditions.
     * @param diceNodes                        The dice nodes.
     * @param notSelectedJoinConditions        The not selected join conditions.
     * @param notSelectedScores                The not selected scores.
     * @param notSelectedScoreFilters          The not selected score filters.
     * @throws IllegalArgumentException If the any of the parameters (except {@code locale} and {@code operation}) is {@code null} or empty.
     */
    public DefaultOperationDisplayServiceModel(String currentState, Locale locale, EngineAnalysisSituation analysisSituation,
                                               Event operation, SessionModel sessionModel, Collection<String> notSelectedMeasures,
                                               Collection<String> notSelectedFilters, Collection<String> notSelectedBaseMeasureConditions,
                                               Graph<String> granularityLevelHierarchy, Graph<String> sliceConditionHierarchy,
                                               Set<Pair<String, String>> sliceConditions, Collection<Triple<String, String, String>> diceNodes,
                                               Collection<String> notSelectedJoinConditions, Collection<String> notSelectedScores,
                                               Collection<String> notSelectedScoreFilters) {
        super(currentState, locale, analysisSituation, operation, sessionModel);

        if (notSelectedFilters == null) throw new IllegalArgumentException("notSelectedFilters must not be null");
        if (granularityLevelHierarchy == null)
            throw new IllegalArgumentException("granularityLevelHierarchy must not be null");
        if (sliceConditionHierarchy == null)
            throw new IllegalArgumentException("sliceConditionHierarchy must not be null");
        if (sliceConditions == null)
            throw new IllegalArgumentException("sliceConditions must not be null");
        if (notSelectedBaseMeasureConditions == null)
            throw new IllegalArgumentException("notSelectedBaseMeasureConditions must not be null");
        if (diceNodes == null)
            throw new IllegalArgumentException("diceNodes must not be null");
        if (notSelectedJoinConditions == null)
            throw new IllegalArgumentException("notSelectedJoinConditions must not be null");
        if (notSelectedScores == null)
            throw new IllegalArgumentException("notSelectedScores must not be null");
        if (notSelectedScoreFilters == null)
            throw new IllegalArgumentException("notSelectedScoreFilters must not be null");

        this.notSelectedMeasures = notSelectedMeasures;
        this.notSelectedFilters = notSelectedFilters;
        this.notSelectedBaseMeasureConditions = notSelectedBaseMeasureConditions;
        this.granularityLevelHierarchy = granularityLevelHierarchy;
        this.sliceConditionHierarchy = sliceConditionHierarchy;
        this.sliceConditions = sliceConditions;
        this.diceNodes = diceNodes;
        this.notSelectedJoinConditions = notSelectedJoinConditions;
        this.notSelectedScores = notSelectedScores;
        this.notSelectedScoreFilters = notSelectedScoreFilters;
    }

    // region --- DICE NODES ---

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
        if (!(getAnalysisSituation() instanceof NonComparativeAnalysisSituation)) return Collections.emptySet();
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) getAnalysisSituation();

        return diceNodes.stream()
                .filter(x -> {
                    DimensionQualification dq = as.getDimensionQualification(x.getLeft());
                    if (dq == null) return false;
                    if (dq.getDiceNode() == null) return true;
                    if (dq.getDiceNode().equals(IRIConstants.RESOURCE_ALL_NODES)) return true;
                    return !dq.getDiceNode().equals(x.getRight());
                }).collect(Collectors.toSet());
    }
    // endregion

    @Override
    public boolean isNotSelectedMeasureAvailable() {
        return !notSelectedMeasures.isEmpty();
    }

    @Override
    @SuppressWarnings("Duplicates")
    public boolean isDrillDownRelationshipAvailable() {
        if (granularityLevelHierarchy.nodes().isEmpty())
            return false;
        if (!(getAnalysisSituation() instanceof NonComparativeAnalysisSituation)) return false;

        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) getAnalysisSituation();
        int count = 0;
        for (DimensionQualification dq : as.getDimensionQualifications()) {
            var preds = new HashSet<>(this.granularityLevelHierarchy.predecessors(dq.getGranularityLevel()));
            preds.remove(IRIConstants.RESOURCE_TOP_LEVEL);
            count += preds.size();
        }

        return count > 0;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public boolean isRollUpRelationshipAvailable() {
        if (granularityLevelHierarchy.nodes().isEmpty())
            return false;
        if (!(getAnalysisSituation() instanceof NonComparativeAnalysisSituation)) return false;

        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) getAnalysisSituation();
        int count = 0;
        for (DimensionQualification dq : as.getDimensionQualifications()) {
            count += this.granularityLevelHierarchy.successors(dq.getGranularityLevel()).size();
        }

        return count > 0;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public boolean isSubLevelRelationshipForDiceLevelAvailable() {
        if (!(getAnalysisSituation() instanceof NonComparativeAnalysisSituation)) return false;
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) getAnalysisSituation();

        return diceNodes.stream()
                .filter(x -> {
                    DimensionQualification dq = as.getDimensionQualification(x.getLeft());
                    if (dq == null) return false;
                    if (dq.getDiceNode() == null) return true;
                    if (dq.getDiceNode().equals(IRIConstants.RESOURCE_ALL_NODES)) return true;
                    return !dq.getDiceNode().equals(x.getRight());
                }).count() > 0;
    }

    @Override
    public boolean isParentLevelRelationshipForDiceLevelAvailable() {
        if (!(getAnalysisSituation() instanceof NonComparativeAnalysisSituation)) return false;
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) getAnalysisSituation();

        return diceNodes.stream()
                .filter(x -> {
                    DimensionQualification dq = as.getDimensionQualification(x.getLeft());
                    if (dq == null || dq.getDiceNode() == null) return false;
                    return dq.getDiceNode().equals(x.getRight());
                }).count() > 0;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public boolean isImplyRelationshipForSliceConditionAvailable() {
        if (sliceConditions.isEmpty()) return false;
        if (!(getAnalysisSituation() instanceof NonComparativeAnalysisSituation)) return false;
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) getAnalysisSituation();

        return sliceConditions.stream().anyMatch(x -> {
            DimensionQualification dq = as.getDimensionQualification(x.getLeft());
            if (dq == null) return false;
            return !dq.getSliceConditions().contains(x.getRight());
        });
    }

    @Override
    @SuppressWarnings("Duplicates")
    public boolean isSubsumeRelationshipForSliceConditionAvailable() {
        if (!(getAnalysisSituation() instanceof NonComparativeAnalysisSituation)) return false;
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) getAnalysisSituation();

        return as.getDimensionQualifications()
                .stream()
                .map(x -> new ImmutablePair<>(x.getDimension(), x.getSliceConditions()))
                .flatMap(x -> x.getRight().stream()
                        .map(p -> new ImmutablePair<>(x.getLeft(), p)))
                .count() > 0;
    }

    @Override
    public boolean isDimensionWithSelectedAndNotSelectedSliceConditionsAvailable() {
        if (!(getAnalysisSituation() instanceof NonComparativeAnalysisSituation)) return false;
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) getAnalysisSituation();

        var dropDims = as.getDimensionQualifications()
                .stream()
                .map(x -> new ImmutablePair<>(x.getDimension(), x.getSliceConditions()))
                .flatMap(x -> x.getRight().stream()
                        .map(p -> new ImmutablePair<>(x.getLeft(), p)))
                .collect(Collectors.toSet());
        var addDims = sliceConditions.stream()
                .filter(x -> {
                    DimensionQualification dq = as.getDimensionQualification(x.getLeft());
                    if (dq == null) return false;
                    return !dq.getSliceConditions().contains(x.getRight());
                }).collect(Collectors.toSet());

        addDims.retainAll(dropDims);

        return !addDims.isEmpty();
    }

    @Override
    public boolean isNotSelectedBaseMeasureConditionAvailable() {
        return !notSelectedBaseMeasureConditions.isEmpty();
    }

    @Override
    public boolean isNotSelectedFilterConditionAvailable() {
        return !notSelectedFilters.isEmpty();
    }

    @Override
    public boolean isNotSelectedJoinConditionAvailable() {
        return !notSelectedJoinConditions.isEmpty();
    }

    @Override
    public boolean isNotSelectedScoreAvailable() {
        return !notSelectedScores.isEmpty();
    }

    @Override
    public boolean isNotSelectedScoreFilterAvailable() {
        return !notSelectedScoreFilters.isEmpty();
    }
}
