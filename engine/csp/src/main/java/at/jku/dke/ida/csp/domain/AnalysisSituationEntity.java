package at.jku.dke.ida.csp.domain;

import at.jku.dke.ida.data.models.similarity.CubeSimilarity;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Supplier;

/**
 * The planning entity for constraint-satisfaction.
 * This class contains the possible elements of the analysis situation with the highest score.
 */
@PlanningEntity
public class AnalysisSituationEntity {

    private final boolean isComparative;
    private String cube;
    private AnalysisSituationElement aggregateMeasures;
    private AnalysisSituationElement aggregateMeasurePredicates;
    private AnalysisSituationElement baseMeasurePredicates;
    private AnalysisSituationElement levels;
    private AnalysisSituationElement levelPredicates;
    private AnalysisSituationElement comparativeMeasures;
    private AnalysisSituationElement comparativeMeasurePredicates;
    private AnalysisSituationElement joinConditionPredicates;

    /**
     * Instantiates a new Analysis situation entity.
     */
    public AnalysisSituationEntity() {
        this.isComparative = false;
    }

    /**
     * Instantiates a new Analysis situation entity.
     *
     * @param isComparative Flag, whether the user wants to create a comparative analysis situation or not.
     */
    AnalysisSituationEntity(boolean isComparative) {
        this.isComparative = isComparative;
    }

    /**
     * Gets cube.
     *
     * @return the cube
     */
    @PlanningVariable(valueRangeProviderRefs = {"cubes"})
    public String getCube() {
        return cube;
    }

    /**
     * Sets cube.
     *
     * @param cube the cube
     */
    public void setCube(String cube) {
        this.cube = cube;
    }

    /**
     * Gets aggregate measures.
     *
     * @return the aggregate measures
     */
    @PlanningVariable(valueRangeProviderRefs = {"aggregateMeasures"})
    public AnalysisSituationElement getAggregateMeasures() {
        return aggregateMeasures;
    }

    /**
     * Sets aggregate measures.
     *
     * @param aggregateMeasures the aggregate measures
     */
    public void setAggregateMeasures(AnalysisSituationElement aggregateMeasures) {
        this.aggregateMeasures = aggregateMeasures;
    }

    /**
     * Gets aggregate measure predicates.
     *
     * @return the aggregate measure predicates
     */
    @PlanningVariable(valueRangeProviderRefs = {"aggregateMeasurePredicates"})
    public AnalysisSituationElement getAggregateMeasurePredicates() {
        return aggregateMeasurePredicates;
    }

    /**
     * Sets aggregate measure predicates.
     *
     * @param aggregateMeasurePredicates the aggregate measure predicates
     */
    public void setAggregateMeasurePredicates(AnalysisSituationElement aggregateMeasurePredicates) {
        this.aggregateMeasurePredicates = aggregateMeasurePredicates;
    }

    /**
     * Gets base measure predicates.
     *
     * @return the base measure predicates
     */
    @PlanningVariable(valueRangeProviderRefs = {"baseMeasurePredicates"})
    public AnalysisSituationElement getBaseMeasurePredicates() {
        return baseMeasurePredicates;
    }

    /**
     * Sets base measure predicates.
     *
     * @param baseMeasurePredicates the base measure predicates
     */
    public void setBaseMeasurePredicates(AnalysisSituationElement baseMeasurePredicates) {
        this.baseMeasurePredicates = baseMeasurePredicates;
    }

    /**
     * Gets levels.
     *
     * @return the levels
     */
    @PlanningVariable(valueRangeProviderRefs = {"levels"})
    public AnalysisSituationElement getLevels() {
        return levels;
    }

    /**
     * Sets levels.
     *
     * @param levels the levels
     */
    public void setLevels(AnalysisSituationElement levels) {
        this.levels = levels;
    }

    /**
     * Gets level predicates.
     *
     * @return the level predicates
     */
    @PlanningVariable(valueRangeProviderRefs = {"levelPredicates"})
    public AnalysisSituationElement getLevelPredicates() {
        return levelPredicates;
    }

    /**
     * Sets level predicates.
     *
     * @param levelPredicates the level predicates
     */
    public void setLevelPredicates(AnalysisSituationElement levelPredicates) {
        this.levelPredicates = levelPredicates;
    }

    /**
     * Gets comparative measures.
     *
     * @return the comparative measures
     */
    @PlanningVariable(valueRangeProviderRefs = {"comparativeMeasures"})
    public AnalysisSituationElement getComparativeMeasures() {
        return comparativeMeasures;
    }

    /**
     * Sets comparative measures.
     *
     * @param comparativeMeasures the comparative measures
     */
    public void setComparativeMeasures(AnalysisSituationElement comparativeMeasures) {
        this.comparativeMeasures = comparativeMeasures;
    }

    /**
     * Gets comparative measure predicates.
     *
     * @return the comparative measure predicates
     */
    @PlanningVariable(valueRangeProviderRefs = {"comparativeMeasurePredicates"})
    public AnalysisSituationElement getComparativeMeasurePredicates() {
        return comparativeMeasurePredicates;
    }

    /**
     * Sets comparative measure predicates.
     *
     * @param comparativeMeasurePredicates the comparative measure predicates
     */
    public void setComparativeMeasurePredicates(AnalysisSituationElement comparativeMeasurePredicates) {
        this.comparativeMeasurePredicates = comparativeMeasurePredicates;
    }

    /**
     * Gets join condition predicates.
     *
     * @return the join condition predicates
     */
    @PlanningVariable(valueRangeProviderRefs = {"joinConditionPredicates"})
    public AnalysisSituationElement getJoinConditionPredicates() {
        return joinConditionPredicates;
    }

    /**
     * Sets join condition predicates.
     *
     * @param joinConditionPredicates the join condition predicates
     */
    public void setJoinConditionPredicates(AnalysisSituationElement joinConditionPredicates) {
        this.joinConditionPredicates = joinConditionPredicates;
    }

    /**
     * Calculates the overall score of the current analysis situation.
     * <p>
     * For this, the scores of all elements are summarized.
     * <p>
     * Then the result will be converted to an {@link BigDecimal}, as this type is used in the OptaPlanner Score.
     * OptaPlanner does not recommend the usage of double scores.
     * If the cube is null or empty, 0 will be returned.
     *
     * @return The calculated score.
     */
    public BigDecimal getScore() {
        if (cube == null || cube.isBlank()) return BigDecimal.ZERO;

        double score = calcScore(0, () -> aggregateMeasures);
        score = calcScore(score, () -> aggregateMeasurePredicates);
        score = calcScore(score, () -> baseMeasurePredicates);
        score = calcScore(score, () -> levels);
        score = calcScore(score, () -> levelPredicates);
        score = calcScore(score, () -> comparativeMeasures);
        score = calcScore(score, () -> comparativeMeasurePredicates);
        score = calcScore(score, () -> joinConditionPredicates);

        return BigDecimal.valueOf(score);
    }

    /**
     * Adds the score of the element supplied by the valSupplier to the score.
     *
     * @param score       The score.
     * @param valSupplier The supplier which returns an element with a score to add to the given score.
     * @return New calculated score.
     */
    private double calcScore(double score, Supplier<AnalysisSituationElement> valSupplier) {
        if (valSupplier.get() != null) {
            double tmp = valSupplier.get().getScore();
            if (tmp > 0) {
                score = Math.abs(score - 0d) < 0.00000001 ? tmp : score + tmp;
            }
        }
        return score;
    }

    /**
     * Gets all similarities.
     *
     * @return all similarity objects of the analysis situation.
     */
    public Set<CubeSimilarity> getAllSimilarities() {
        Set<CubeSimilarity> similarities = new HashSet<>();
        if (getAggregateMeasures() != null)
            similarities.addAll(getAggregateMeasures().getElements());
        if (getAggregateMeasurePredicates() != null)
            similarities.addAll(getAggregateMeasurePredicates().getElements());
        if (getBaseMeasurePredicates() != null)
            similarities.addAll(getBaseMeasurePredicates().getElements());
        if (getLevels() != null)
            similarities.addAll(getLevels().getElements());
        if (getLevelPredicates() != null)
            similarities.addAll(getLevelPredicates().getElements());
        if (getComparativeMeasures() != null)
            similarities.addAll(getComparativeMeasures().getElements());
        if (getComparativeMeasurePredicates() != null)
            similarities.addAll(getComparativeMeasurePredicates().getElements());
        if (getJoinConditionPredicates() != null)
            similarities.addAll(getJoinConditionPredicates().getElements());
        return similarities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnalysisSituationEntity that = (AnalysisSituationEntity) o;
        return isComparative == that.isComparative &&
                Objects.equals(cube, that.cube) &&
                Objects.equals(aggregateMeasures, that.aggregateMeasures) &&
                Objects.equals(aggregateMeasurePredicates, that.aggregateMeasurePredicates) &&
                Objects.equals(baseMeasurePredicates, that.baseMeasurePredicates) &&
                Objects.equals(levels, that.levels) &&
                Objects.equals(levelPredicates, that.levelPredicates) &&
                Objects.equals(comparativeMeasures, that.comparativeMeasures) &&
                Objects.equals(comparativeMeasurePredicates, that.comparativeMeasurePredicates) &&
                Objects.equals(joinConditionPredicates, that.joinConditionPredicates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isComparative, cube, aggregateMeasures, aggregateMeasurePredicates, baseMeasurePredicates, levels, levelPredicates, comparativeMeasures, comparativeMeasurePredicates, joinConditionPredicates);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AnalysisSituationEntity.class.getSimpleName() + "[", "]")
                .add("cube='" + cube + "'")
                .add("isComparative=" + isComparative)
                .toString();
    }
}
