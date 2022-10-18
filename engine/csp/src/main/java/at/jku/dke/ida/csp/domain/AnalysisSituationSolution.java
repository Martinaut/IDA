package at.jku.dke.ida.csp.domain;

import at.jku.dke.ida.data.models.similarity.CubeSimilarity;
import com.google.common.collect.Sets;
import org.optaplanner.core.api.domain.solution.PlanningEntityProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This is the OptaPlanner Solution class.
 * This class holds all possible values for cubes, measures, ... as well as the resulting analysis situation
 * and the score.
 */
@PlanningSolution
public class AnalysisSituationSolution {

    private Set<String> cubes;
    private Set<AnalysisSituationElement> aggregateMeasures;
    private Set<AnalysisSituationElement> aggregateMeasurePredicates;
    private Set<AnalysisSituationElement> baseMeasurePredicates;
    private Set<AnalysisSituationElement> levels;
    private Set<AnalysisSituationElement> levelPredicates;
    private Set<AnalysisSituationElement> comparativeMeasures;
    private Set<AnalysisSituationElement> comparativeMeasurePredicates;
    private Set<AnalysisSituationElement> joinConditionPredicates;
    private AnalysisSituationEntity analysisSituation;
    private HardSoftBigDecimalScore score;

    /**
     * Instantiates a new instance of class {@linkplain AnalysisSituationSolution}.
     */
    public AnalysisSituationSolution() {
        this.cubes = Collections.emptySet();
        this.aggregateMeasures = Collections.emptySet();
        this.aggregateMeasurePredicates = Collections.emptySet();
        this.baseMeasurePredicates = Collections.emptySet();
        this.levels = Collections.emptySet();
        this.levelPredicates = Collections.emptySet();
        this.comparativeMeasures = Collections.emptySet();
        this.comparativeMeasurePredicates = Collections.emptySet();
        this.joinConditionPredicates = Collections.emptySet();
        this.analysisSituation = new AnalysisSituationEntity(false);
        this.score = HardSoftBigDecimalScore.ZERO;
    }

    /**
     * Instantiates a new Analysis situation solution.
     *
     * @param isComparative                Flag, whether the user wants to create a comparative analysis situation or not.
     * @param cubes                        The cubes.
     * @param aggregateMeasures            The aggregate measures.
     * @param aggregateMeasurePredicates   The aggregate measure predicates.
     * @param baseMeasurePredicates        The base measure predicates.
     * @param levels                       The levels.
     * @param levelPredicates              The level predicates.
     * @param comparativeMeasures          The comparative measures.
     * @param comparativeMeasurePredicates The comparative measure predicates.
     * @param joinConditionPredicates      The join condition predicates.
     */
    public AnalysisSituationSolution(boolean isComparative,
                                     Set<String> cubes,
                                     Set<CubeSimilarity> aggregateMeasures,
                                     Set<CubeSimilarity> aggregateMeasurePredicates,
                                     Set<CubeSimilarity> baseMeasurePredicates,
                                     Set<CubeSimilarity> levels,
                                     Set<CubeSimilarity> levelPredicates,
                                     Set<CubeSimilarity> comparativeMeasures,
                                     Set<CubeSimilarity> comparativeMeasurePredicates,
                                     Set<CubeSimilarity> joinConditionPredicates) {
        this.cubes = cubes;
        this.aggregateMeasures = createAllPossibleCombinations(aggregateMeasures);
        this.aggregateMeasurePredicates = createAllPossibleCombinations(aggregateMeasurePredicates);
        this.baseMeasurePredicates = createAllPossibleCombinations(baseMeasurePredicates);
        this.levels = createAllPossibleCombinations(levels);
        this.levelPredicates = createAllPossibleCombinations(levelPredicates);
        this.comparativeMeasures = createAllPossibleCombinations(comparativeMeasures);
        this.comparativeMeasurePredicates = createAllPossibleCombinations(comparativeMeasurePredicates);
        this.joinConditionPredicates = createAllPossibleCombinations(joinConditionPredicates);
        this.analysisSituation = new AnalysisSituationEntity(isComparative);
        this.score = HardSoftBigDecimalScore.ZERO;
    }

    /**
     * Creates all possible combinations of the given data.
     * <p>
     * All possible combinations of {@code [1, 2, 3]} would be:
     * [], [1], [2], [3], [1, 2], [1, 3], [2, 3], [1, 2, 3]
     *
     * @param data The data.
     * @return The set with elements of all possible combinations.
     */
    private static Set<AnalysisSituationElement> createAllPossibleCombinations(Set<CubeSimilarity> data) {
        Set<Set<CubeSimilarity>> result = new HashSet<>();

        // Group by cube
        Map<String, List<CubeSimilarity>> grouped = data.stream().collect(Collectors.groupingBy(CubeSimilarity::getCube));

        // Create combinations
        for (List<CubeSimilarity> sims : grouped.values()) {
            for (int i = 0; i <= sims.size(); i++) {
                result.addAll(Sets.combinations(new HashSet<>(sims), i));
            }
        }
        if (result.isEmpty()) result.add(Collections.emptySet());

        // Return result
        return result.stream()
                .map(AnalysisSituationElement::new)
                .collect(Collectors.toSet());
    }

    /**
     * Gets cubes.
     *
     * @return the cubes
     */
    @ValueRangeProvider(id = "cubes")
    @ProblemFactCollectionProperty
    public Set<String> getCubes() {
        return cubes;
    }

    /**
     * Sets cubes.
     *
     * @param cubes the cubes
     */
    public void setCubes(Set<String> cubes) {
        this.cubes = cubes;
    }

    /**
     * Gets aggregate measures.
     *
     * @return the aggregate measures
     */
    @ValueRangeProvider(id = "aggregateMeasures")
    @ProblemFactCollectionProperty
    public Set<AnalysisSituationElement> getAggregateMeasures() {
        return aggregateMeasures;
    }

    /**
     * Sets aggregate measures.
     *
     * @param aggregateMeasures the aggregate measures
     */
    public void setAggregateMeasures(Set<AnalysisSituationElement> aggregateMeasures) {
        this.aggregateMeasures = aggregateMeasures;
    }

    /**
     * Gets aggregate measure predicates.
     *
     * @return the aggregate measure predicates
     */
    @ValueRangeProvider(id = "aggregateMeasurePredicates")
    @ProblemFactCollectionProperty
    public Set<AnalysisSituationElement> getAggregateMeasurePredicates() {
        return aggregateMeasurePredicates;
    }

    /**
     * Sets aggregate measure predicates.
     *
     * @param aggregateMeasurePredicates the aggregate measure predicates
     */
    public void setAggregateMeasurePredicates(Set<AnalysisSituationElement> aggregateMeasurePredicates) {
        this.aggregateMeasurePredicates = aggregateMeasurePredicates;
    }

    /**
     * Gets base measure predicates.
     *
     * @return the base measure predicates
     */
    @ValueRangeProvider(id = "baseMeasurePredicates")
    @ProblemFactCollectionProperty
    public Set<AnalysisSituationElement> getBaseMeasurePredicates() {
        return baseMeasurePredicates;
    }

    /**
     * Sets base measure predicates.
     *
     * @param baseMeasurePredicates the base measure predicates
     */
    public void setBaseMeasurePredicates(Set<AnalysisSituationElement> baseMeasurePredicates) {
        this.baseMeasurePredicates = baseMeasurePredicates;
    }

    /**
     * Gets levels.
     *
     * @return the levels
     */
    @ValueRangeProvider(id = "levels")
    @ProblemFactCollectionProperty
    public Set<AnalysisSituationElement> getLevels() {
        return levels;
    }

    /**
     * Sets levels.
     *
     * @param levels the levels
     */
    public void setLevels(Set<AnalysisSituationElement> levels) {
        this.levels = levels;
    }

    /**
     * Gets level predicates.
     *
     * @return the level predicates
     */
    @ValueRangeProvider(id = "levelPredicates")
    @ProblemFactCollectionProperty
    public Set<AnalysisSituationElement> getLevelPredicates() {
        return levelPredicates;
    }

    /**
     * Sets level predicates.
     *
     * @param levelPredicates the level predicates
     */
    public void setLevelPredicates(Set<AnalysisSituationElement> levelPredicates) {
        this.levelPredicates = levelPredicates;
    }

    /**
     * Gets comparative measures.
     *
     * @return the comparative measures
     */
    @ValueRangeProvider(id = "comparativeMeasures")
    @ProblemFactCollectionProperty
    public Set<AnalysisSituationElement> getComparativeMeasures() {
        return comparativeMeasures;
    }

    /**
     * Sets comparative measures.
     *
     * @param comparativeMeasures the comparative measures
     */
    public void setComparativeMeasures(Set<AnalysisSituationElement> comparativeMeasures) {
        this.comparativeMeasures = comparativeMeasures;
    }

    /**
     * Gets comparative measure predicates.
     *
     * @return the comparative measure predicates
     */
    @ValueRangeProvider(id = "comparativeMeasurePredicates")
    @ProblemFactCollectionProperty
    public Set<AnalysisSituationElement> getComparativeMeasurePredicates() {
        return comparativeMeasurePredicates;
    }

    /**
     * Sets comparative measure predicates.
     *
     * @param comparativeMeasurePredicates the comparative measure predicates
     */
    public void setComparativeMeasurePredicates(Set<AnalysisSituationElement> comparativeMeasurePredicates) {
        this.comparativeMeasurePredicates = comparativeMeasurePredicates;
    }

    /**
     * Gets join condition predicates.
     *
     * @return the join condition predicates
     */
    @ValueRangeProvider(id = "joinConditionPredicates")
    @ProblemFactCollectionProperty
    public Set<AnalysisSituationElement> getJoinConditionPredicates() {
        return joinConditionPredicates;
    }

    /**
     * Sets join condition predicates.
     *
     * @param joinConditionPredicates the join condition predicates
     */
    public void setJoinConditionPredicates(Set<AnalysisSituationElement> joinConditionPredicates) {
        this.joinConditionPredicates = joinConditionPredicates;
    }

    /**
     * Gets the analysis situation.
     *
     * @return the analysis situation
     */
    @PlanningEntityProperty
    public AnalysisSituationEntity getAnalysisSituation() {
        return analysisSituation;
    }

    /**
     * Sets the analysis situation.
     *
     * @param analysisSituation the analysis situation
     */
    public void setAnalysisSituation(AnalysisSituationEntity analysisSituation) {
        this.analysisSituation = analysisSituation;
    }

    /**
     * Gets the score.
     *
     * @return the score
     */
    @PlanningScore
    public HardSoftBigDecimalScore getScore() {
        return score;
    }

    /**
     * Sets the score.
     *
     * @param score the score
     */
    public void setScore(HardSoftBigDecimalScore score) {
        this.score = score;
    }
}
