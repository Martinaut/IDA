package at.jku.dke.ida.web.models.analysissituation;

import at.jku.dke.ida.data.models.Label;
import at.jku.dke.ida.shared.models.generic.GenericNonComparativeAnalysisSituation;

import java.util.TreeSet;

/**
 * Used to display the non comparative AS.
 */
public class NonComparativeAnalysisSituationDisplay extends GenericNonComparativeAnalysisSituation<Label, Label, DimensionQualificationDisplay> {
    /**
     * Instantiates a new instance of class {@linkplain NonComparativeAnalysisSituationDisplay}.
     */
    public NonComparativeAnalysisSituationDisplay() {
        super();
        setMeasures(new TreeSet<>());
        setBaseMeasureConditions(new TreeSet<>());
        setFilterConditions(new TreeSet<>());
        setDimensionQualifications(new TreeSet<>());
    }
}
