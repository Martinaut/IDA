package at.jku.dke.inga.app.ruleset.csp.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.Objects;
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

        double score = 1;
        if (measures != null)
            score = score * measures.getScore();
        if (sliceConditions != null)
            score = score * sliceConditions.getScore();
        if (granularityLevels != null)
            score = score * granularityLevels.getScore();
        if (baseMeasureConditions != null)
            score = score * baseMeasureConditions.getScore();
        if (filterConditions != null)
            score = score * filterConditions.getScore();

        return (int) (score * 10000);
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
        return Objects.hash(cube, measures, granularityLevels, sliceConditions,baseMeasureConditions, filterConditions);
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
