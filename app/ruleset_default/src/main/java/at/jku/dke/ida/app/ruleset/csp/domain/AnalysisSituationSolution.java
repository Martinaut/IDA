package at.jku.dke.ida.app.ruleset.csp.domain;

import at.jku.dke.ida.data.models.CubeSimilarity;
import at.jku.dke.ida.data.models.DimensionSimilarity;
import com.google.common.collect.Sets;
import org.optaplanner.core.api.domain.solution.PlanningEntityProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
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
    private Set<AnalysisSituationElement> measures;
    private Set<AnalysisSituationElement> levels;
    private Set<AnalysisSituationElement> sliceConditions;
    private Set<AnalysisSituationElement> baseMeasureConditions;
    private Set<AnalysisSituationElement> filterConditions;
    private AnalysisSituation analysisSituation;
    private HardSoftBigDecimalScore score;

    /**
     * Instantiates a new instance of class {@linkplain AnalysisSituationSolution}.
     */
    public AnalysisSituationSolution() {
        this.cubes = Collections.emptySet();
        this.measures = Collections.emptySet();
        this.levels = Collections.emptySet();
        this.baseMeasureConditions = Collections.emptySet();
        this.filterConditions = Collections.emptySet();
        this.analysisSituation = new AnalysisSituation();
        this.score = HardSoftBigDecimalScore.ZERO;
    }

    /**
     * Instantiates a new instance of class {@linkplain AnalysisSituationSolution}.
     *
     * @param cubes                 The available cubes.
     * @param measures              The available measures.
     * @param levels                The available levels.
     * @param sliceConditions       The available slice conditions.
     * @param baseMeasureConditions The available base measure conditions.
     * @param filterConditions      The available filter conditions.
     */
    public AnalysisSituationSolution(Set<String> cubes, Set<CubeSimilarity> measures, Set<CubeSimilarity> levels, Set<CubeSimilarity> sliceConditions,
                                     Set<CubeSimilarity> baseMeasureConditions, Set<CubeSimilarity> filterConditions) {
        this.cubes = cubes;
        this.measures = createAllPossibleCombinations(measures);
        this.levels = createAllPossibleCombinations(levels);
        this.sliceConditions = createAllPossibleCombinations(sliceConditions);
        this.baseMeasureConditions = createAllPossibleCombinations(baseMeasureConditions);
        this.filterConditions = createAllPossibleCombinations(filterConditions);
        this.analysisSituation = new AnalysisSituation();
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
    public static Set<AnalysisSituationElement> createAllPossibleCombinations(Set<CubeSimilarity> data) {
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
     * Gets the cubes.
     *
     * @return the cubes
     */
    @ValueRangeProvider(id = "cubes")
    @ProblemFactCollectionProperty
    public Set<String> getCubes() {
        return cubes;
    }

    /**
     * Sets the cubes.
     *
     * @param cubes the cubes
     */
    public void setCubes(Set<String> cubes) {
        this.cubes = cubes;
    }

    /**
     * Gets all possible measures combinations.
     *
     * @return the measures
     */
    @ValueRangeProvider(id = "measures")
    @ProblemFactCollectionProperty
    public Set<AnalysisSituationElement> getMeasures() {
        return measures;
    }

    /**
     * Sets all possible measures combinations.
     *
     * @param measures the measures
     */
    public void setMeasures(Set<AnalysisSituationElement> measures) {
        this.measures = measures;
    }

    /**
     * Gets all possible level combinations.
     *
     * @return the levels
     */
    @ValueRangeProvider(id = "granularityLevels")
    @ProblemFactCollectionProperty
    public Set<AnalysisSituationElement> getLevels() {
        return levels;
    }

    /**
     * Sets all possible level combinations.
     *
     * @param levels the levels
     */
    public void setLevels(Set<AnalysisSituationElement> levels) {
        this.levels = levels;
    }

    /**
     * Gets all possible base measure condition combinations.
     *
     * @return the base measure conditions
     */
    @ValueRangeProvider(id = "baseMeasureConditions")
    @ProblemFactCollectionProperty
    public Set<AnalysisSituationElement> getBaseMeasureConditions() {
        return baseMeasureConditions;
    }

    /**
     * Sets all possible base measure condition combinations.
     *
     * @param baseMeasureConditions the base measure conditions
     */
    public void setBaseMeasureConditions(Set<AnalysisSituationElement> baseMeasureConditions) {
        this.baseMeasureConditions = baseMeasureConditions;
    }

    /**
     * Gets all possible filter condition combinations.
     *
     * @return the filter conditions
     */
    @ValueRangeProvider(id = "filterConditions")
    @ProblemFactCollectionProperty
    public Set<AnalysisSituationElement> getFilterConditions() {
        return filterConditions;
    }

    /**
     * Sets all possible filter condition combinations.
     *
     * @param filterConditions the filter conditions
     */
    public void setFilterConditions(Set<AnalysisSituationElement> filterConditions) {
        this.filterConditions = filterConditions;
    }

    /**
     * Gets all possible slice condition combinations.
     *
     * @return the slice conditions
     */
    @ValueRangeProvider(id = "sliceConditions")
    @ProblemFactCollectionProperty
    public Set<AnalysisSituationElement> getSliceConditions() {
        return sliceConditions;
    }

    /**
     * Sets all possible slice condition combinations.
     *
     * @param sliceConditions the slice conditions
     */
    public void setSliceConditions(Set<AnalysisSituationElement> sliceConditions) {
        this.sliceConditions = sliceConditions;
    }

    /**
     * Gets the analysis situation.
     *
     * @return the analysis situation
     */
    @PlanningEntityProperty
    public AnalysisSituation getAnalysisSituation() {
        return analysisSituation;
    }

    /**
     * Sets the analysis situation.
     *
     * @param analysisSituation the analysis situation
     */
    public void setAnalysisSituation(AnalysisSituation analysisSituation) {
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
