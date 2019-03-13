package at.jku.dke.inga.scxml.events;

import at.jku.dke.inga.shared.models.AnalysisSituation;

import java.util.EventObject;

/**
 * The event object with analysis situation.
 */
public class AnalysisSituationEvent extends EventObject {
    private final AnalysisSituation analysisSituation;

    /**
     * Instantiates a new instance of class {@linkplain AnalysisSituationEvent}.
     *
     * @param source            The source of the event.
     * @param analysisSituation The analysis situation.
     */
    public AnalysisSituationEvent(Object source, AnalysisSituation analysisSituation) {
        super(source);
        this.analysisSituation = analysisSituation;
    }

    /**
     * Gets the analysis situation.
     *
     * @return the analysis situation
     */
    public AnalysisSituation getAnalysisSituation() {
        return analysisSituation;
    }
}
