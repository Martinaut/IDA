package at.jku.dke.inga.scxml.events;

import at.jku.dke.inga.shared.models.AnalysisSituation;

import java.util.EventObject;

/**
 * The event object with analysis situation.
 */
public class AnalysisSituationEvent extends EventObject {

    private final AnalysisSituation analysisSituation;
    private final String language;

    /**
     * Instantiates a new instance of class {@linkplain AnalysisSituationEvent}.
     *
     * @param source            The source of the event.
     * @param analysisSituation The analysis situation.
     * @param language          The language.
     */
    public AnalysisSituationEvent(Object source, AnalysisSituation analysisSituation, String language) {
        super(source);
        this.analysisSituation = analysisSituation;
        this.language = language;
    }

    /**
     * Gets the analysis situation.
     *
     * @return the analysis situation
     */
    public AnalysisSituation getAnalysisSituation() {
        return analysisSituation;
    }

    /**
     * Gets the language.
     *
     * @return the language
     */
    public String getLanguage() {
        return language;
    }
}
