package at.jku.dke.ida.app.ruleset.csp.domain;

import at.jku.dke.ida.data.models.CubeSimilarity;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

@PlanningEntity
public class AnalysisSituation {

    private String cube;
    private AnalysisSituationElement measures;
    private AnalysisSituationElement sliceConditions;
    private AnalysisSituationElement granularityLevels;
    private AnalysisSituationElement baseMeasureConditions;
    private AnalysisSituationElement filterConditions;

    public AnalysisSituation() {
    }

    @PlanningVariable(valueRangeProviderRefs = {"cubes"})
    public String getCube() {
        return cube;
    }

    public void setCube(String cube) {
        this.cube = cube;
    }

    @PlanningVariable(valueRangeProviderRefs = {"measures"})
    public AnalysisSituationElement getMeasures() {
        return measures;
    }

    public void setMeasures(AnalysisSituationElement measures) {
        this.measures = measures;
    }

    @PlanningVariable(valueRangeProviderRefs = {"granularityLevels"})
    public AnalysisSituationElement getGranularityLevels() {
        return granularityLevels;
    }

    public void setGranularityLevels(AnalysisSituationElement granularityLevels) {
        this.granularityLevels = granularityLevels;
    }

    @PlanningVariable(valueRangeProviderRefs = {"baseMeasureConditions"})
    public AnalysisSituationElement getBaseMeasureConditions() {
        return baseMeasureConditions;
    }

    public void setBaseMeasureConditions(AnalysisSituationElement baseMeasureConditions) {
        this.baseMeasureConditions = baseMeasureConditions;
    }

    @PlanningVariable(valueRangeProviderRefs = {"filterConditions"})
    public AnalysisSituationElement getFilterConditions() {
        return filterConditions;
    }

    public void setFilterConditions(AnalysisSituationElement filterConditions) {
        this.filterConditions = filterConditions;
    }

    @PlanningVariable(valueRangeProviderRefs = {"sliceConditions"})
    public AnalysisSituationElement getSliceConditions() {
        return sliceConditions;
    }

    public void setSliceConditions(AnalysisSituationElement sliceConditions) {
        this.sliceConditions = sliceConditions;
    }

    public int getScore() {
        if (cube == null) return 0;

        double score = 0;
        if (measures != null)
            score = measures.getScore();
        if (sliceConditions != null) {
            double tmp = sliceConditions.getScore();
            score = Double.compare(score, 0) == 0 ? tmp : score * tmp;
        }
        if (granularityLevels != null) {
            double tmp = granularityLevels.getScore();
            score = Double.compare(score, 0) == 0 ? tmp : score * tmp;
        }
        if (baseMeasureConditions != null) {
            double tmp = baseMeasureConditions.getScore();
            score = Double.compare(score, 0) == 0 ? tmp : score * tmp;
        }
        if (filterConditions != null) {
            double tmp = filterConditions.getScore();
            score = Double.compare(score, 0) == 0 ? tmp : score * tmp;
        }

        return (int) (score * 10_000d); // TODO: was wenn mehrere gleich in z. b. measures? --> regel
    }

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
