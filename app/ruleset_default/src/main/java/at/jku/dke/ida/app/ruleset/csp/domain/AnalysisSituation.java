package at.jku.dke.ida.app.ruleset.csp.domain;

import at.jku.dke.ida.data.models.CubeSimilarity;
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
 * This class contains the elements of the non-comparative analysis situation with the highest score.
 */
@PlanningEntity
public class AnalysisSituation {

    private String cube;
    private AnalysisSituationElement measures;
    private AnalysisSituationElement sliceConditions;
    private AnalysisSituationElement granularityLevels;
    private AnalysisSituationElement baseMeasureConditions;
    private AnalysisSituationElement filterConditions;

    /**
     * Instantiates a new instance of class {@linkplain AnalysisSituation}.
     */
    public AnalysisSituation() {
    }

    /**
     * Gets the the cube.
     *
     * @return the cube
     */
    @PlanningVariable(valueRangeProviderRefs = {"cubes"})
    public String getCube() {
        return cube;
    }

    /**
     * Sets the cube.
     *
     * @param cube the cube
     */
    public void setCube(String cube) {
        this.cube = cube;
    }

    /**
     * Gets the measures.
     *
     * @return the measures
     */
    @PlanningVariable(valueRangeProviderRefs = {"measures"})
    public AnalysisSituationElement getMeasures() {
        return measures;
    }

    /**
     * Sets the measures.
     *
     * @param measures the measures
     */
    public void setMeasures(AnalysisSituationElement measures) {
        this.measures = measures;
    }

    /**
     * Gets the granularity levels.
     *
     * @return the granularity levels
     */
    @PlanningVariable(valueRangeProviderRefs = {"granularityLevels"})
    public AnalysisSituationElement getGranularityLevels() {
        return granularityLevels;
    }

    /**
     * Sets the granularity levels.
     *
     * @param granularityLevels the granularity levels
     */
    public void setGranularityLevels(AnalysisSituationElement granularityLevels) {
        this.granularityLevels = granularityLevels;
    }

    /**
     * Gets the base measure conditions.
     *
     * @return the base measure conditions
     */
    @PlanningVariable(valueRangeProviderRefs = {"baseMeasureConditions"})
    public AnalysisSituationElement getBaseMeasureConditions() {
        return baseMeasureConditions;
    }

    /**
     * Sets the base measure conditions.
     *
     * @param baseMeasureConditions the base measure conditions
     */
    public void setBaseMeasureConditions(AnalysisSituationElement baseMeasureConditions) {
        this.baseMeasureConditions = baseMeasureConditions;
    }

    /**
     * Gets the filter conditions.
     *
     * @return the filter conditions
     */
    @PlanningVariable(valueRangeProviderRefs = {"filterConditions"})
    public AnalysisSituationElement getFilterConditions() {
        return filterConditions;
    }

    /**
     * Sets the filter conditions.
     *
     * @param filterConditions the filter conditions
     */
    public void setFilterConditions(AnalysisSituationElement filterConditions) {
        this.filterConditions = filterConditions;
    }

    /**
     * Gets the slice conditions.
     *
     * @return the slice conditions
     */
    @PlanningVariable(valueRangeProviderRefs = {"sliceConditions"})
    public AnalysisSituationElement getSliceConditions() {
        return sliceConditions;
    }

    /**
     * Sets the slice conditions.
     *
     * @param sliceConditions the slice conditions
     */
    public void setSliceConditions(AnalysisSituationElement sliceConditions) {
        this.sliceConditions = sliceConditions;
    }

    /**
     * Calculates the overall score of the current analysis situation.
     * <p>
     * For this, the scores of all elements are multiplied. If one of the elements returns score 0,
     * the score will be omitted, as a multiplication with 0 would result in an overall 0 score.
     * <p>
     * Then the result will be converted to an {@link java.math.BigDecimal},
     * as this type is used in the OptaPlanner Score. OptaPlanner does not recommend the usage of
     * double scores.
     * If the cube is null or empty, 0 will be returned.
     *
     * @return the score
     */
    public BigDecimal getScore() {
        if (cube == null || cube.isBlank()) return BigDecimal.ZERO;

        double score = calcScore(0, () -> measures);
        score = calcScore(score, () -> sliceConditions);
        score = calcScore(score, () -> granularityLevels);
        score = calcScore(score, () -> baseMeasureConditions);
        score = calcScore(score, () -> filterConditions);

        return BigDecimal.valueOf(score);
    }

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
        if (getGranularityLevels() != null)
            similarities.addAll(getGranularityLevels().getElements());
        if (getSliceConditions() != null)
            similarities.addAll(getSliceConditions().getElements());
        if (getFilterConditions() != null)
            similarities.addAll(getFilterConditions().getElements());
        if (getBaseMeasureConditions() != null)
            similarities.addAll(getBaseMeasureConditions().getElements());
        if (getMeasures() != null)
            similarities.addAll(getMeasures().getElements());
        return similarities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnalysisSituation that = (AnalysisSituation) o;
        return Objects.equals(cube, that.cube) &&
                Objects.equals(measures, that.measures) &&
                Objects.equals(granularityLevels, that.granularityLevels) &&
                Objects.equals(sliceConditions, that.sliceConditions) &&
                Objects.equals(baseMeasureConditions, that.baseMeasureConditions) &&
                Objects.equals(filterConditions, that.filterConditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cube, measures, granularityLevels, sliceConditions, baseMeasureConditions, filterConditions);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AnalysisSituation.class.getSimpleName() + "[", "]")
                .add("cube='" + cube + "'")
                .add("measures=" + measures)
                .add("granularityLevels=" + granularityLevels)
                .add("sliceConditions=" + sliceConditions)
                .add("baseMeasureConditions=" + baseMeasureConditions)
                .add("filterConditions=" + filterConditions)
                .toString();
    }
}
