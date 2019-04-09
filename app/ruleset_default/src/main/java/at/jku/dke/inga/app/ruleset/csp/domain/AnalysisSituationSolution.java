package at.jku.dke.inga.app.ruleset.csp.domain;

import at.jku.dke.inga.data.models.Similarity;
import com.google.common.collect.Sets;
import org.optaplanner.core.api.domain.solution.PlanningEntityProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@PlanningSolution
public class AnalysisSituationSolution {

    private Set<String> cubes;
    private Set<AnalysisSituationElement> measures;
    private Set<AnalysisSituationElement> levels;
    private Set<AnalysisSituationElement> sliceConditions;
    private Set<AnalysisSituationElement> baseMeasureConditions;
    private Set<AnalysisSituationElement> filterConditions;
    private AnalysisSituation analysisSituation;
    private HardSoftScore score;

    public AnalysisSituationSolution() {
        this.cubes = Collections.emptySet();
        this.measures = Collections.emptySet();
        this.levels = Collections.emptySet();
        this.baseMeasureConditions = Collections.emptySet();
        this.filterConditions = Collections.emptySet();
        this.analysisSituation = new AnalysisSituation();
        this.score = HardSoftScore.ZERO;
    }

    public AnalysisSituationSolution(Set<String> cubes, Set<Similarity> measures, Set<Similarity> levels, Set<Similarity> sliceConditions,
                                     Set<Similarity> baseMeasureConditions, Set<Similarity> filterConditions) {
        this.cubes = cubes;
        this.measures = createAllPossibleCombinations(measures).stream().map(AnalysisSituationElement::new).collect(Collectors.toSet());
        this.levels = createAllPossibleCombinations(levels).stream().map(AnalysisSituationElement::new).collect(Collectors.toSet());
        this.baseMeasureConditions = createAllPossibleCombinations(baseMeasureConditions).stream().map(AnalysisSituationElement::new).collect(Collectors.toSet());
        this.filterConditions = createAllPossibleCombinations(filterConditions).stream().map(AnalysisSituationElement::new).collect(Collectors.toSet());
        this.sliceConditions = createAllPossibleCombinations(sliceConditions).stream().map(AnalysisSituationElement::new).collect(Collectors.toSet());
        this.analysisSituation = new AnalysisSituation();
        this.score = HardSoftScore.ZERO;
    }

    private <T> Set<Set<T>> createAllPossibleCombinations(Set<T> data) {
        Set<Set<T>> result = new HashSet<>();
        for (int i = 0; i <= data.size(); i++) {
            result.addAll(Sets.combinations(data, i));
        }
        return result;
    }

    @ValueRangeProvider(id = "cubes")
    @ProblemFactCollectionProperty
    public Set<String> getCubes() {
        return cubes;
    }

    public void setCubes(Set<String> cubes) {
        this.cubes = cubes;
    }

    @ValueRangeProvider(id = "measures")
    @ProblemFactCollectionProperty
    public Set<AnalysisSituationElement> getMeasures() {
        return measures;
    }

    public void setMeasures(Set<AnalysisSituationElement> measures) {
        this.measures = measures;
    }

    @ValueRangeProvider(id = "granularityLevels")
    @ProblemFactCollectionProperty
    public Set<AnalysisSituationElement> getLevels() {
        return levels;
    }

    public void setLevels(Set<AnalysisSituationElement> levels) {
        this.levels = levels;
    }

    @ValueRangeProvider(id = "baseMeasureConditions")
    @ProblemFactCollectionProperty
    public Set<AnalysisSituationElement> getBaseMeasureConditions() {
        return baseMeasureConditions;
    }

    public void setBaseMeasureConditions(Set<AnalysisSituationElement> baseMeasureConditions) {
        this.baseMeasureConditions = baseMeasureConditions;
    }

    @ValueRangeProvider(id = "filterConditions")
    @ProblemFactCollectionProperty
    public Set<AnalysisSituationElement> getFilterConditions() {
        return filterConditions;
    }

    public void setFilterConditions(Set<AnalysisSituationElement> filterConditions) {
        this.filterConditions = filterConditions;
    }

    @ValueRangeProvider(id = "sliceConditions")
    @ProblemFactCollectionProperty
    public Set<AnalysisSituationElement> getSliceConditions() {
        return sliceConditions;
    }

    public void setSliceConditions(Set<AnalysisSituationElement> sliceConditions) {
        this.sliceConditions = sliceConditions;
    }

    @PlanningEntityProperty
    public AnalysisSituation getAnalysisSituation() {
        return analysisSituation;
    }

    public void setAnalysisSituation(AnalysisSituation analysisSituation) {
        this.analysisSituation = analysisSituation;
    }

    @PlanningScore
    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AnalysisSituationSolution.class.getSimpleName() + "[", "]")
                .add("cubes=" + cubes)
                .add("measures=" + measures)
                .add("levels=" + levels)
                .add("sliceConditions=" + sliceConditions)
                .add("baseMeasureConditions=" + baseMeasureConditions)
                .add("filterConditions=" + filterConditions)
                .add("analysisSituation=" + analysisSituation)
                .add("score=" + score)
                .toString();
    }
}
