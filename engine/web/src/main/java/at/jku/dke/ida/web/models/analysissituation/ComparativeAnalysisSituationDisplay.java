package at.jku.dke.ida.web.models.analysissituation;

import at.jku.dke.ida.data.models.Label;
import at.jku.dke.ida.shared.models.generic.GenericComparativeAnalysisSituation;

import java.util.TreeSet;

/**
 * Used to display the non comparative AS.
 */
public class ComparativeAnalysisSituationDisplay extends GenericComparativeAnalysisSituation<Label, Label, DimensionQualificationDisplay, NonComparativeAnalysisSituationDisplay> {
    /**
     * Instantiates a new instance of class {@linkplain ComparativeAnalysisSituationDisplay}.
     */
    public ComparativeAnalysisSituationDisplay() {
        super();
        this.setJoinConditions(new TreeSet<>());
        this.setScores(new TreeSet<>());
        this.setScoreFilters(new TreeSet<>());
    }

    /**
     * Instantiates a new instance of class {@linkplain ComparativeAnalysisSituationDisplay}.
     *
     * @param contextOfInterest   The context of interest.
     * @param contextOfComparison The context of comparison.
     */
    public ComparativeAnalysisSituationDisplay(NonComparativeAnalysisSituationDisplay contextOfInterest, NonComparativeAnalysisSituationDisplay contextOfComparison) {
        super(contextOfInterest, contextOfComparison);
        this.setJoinConditions(new TreeSet<>());
        this.setScores(new TreeSet<>());
        this.setScoreFilters(new TreeSet<>());
    }
}
