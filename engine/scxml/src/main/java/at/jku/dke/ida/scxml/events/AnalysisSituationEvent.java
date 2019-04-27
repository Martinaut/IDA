package at.jku.dke.ida.scxml.events;

import at.jku.dke.ida.shared.models.EngineAnalysisSituation;

import java.util.StringJoiner;

/**
 * The event object with analysis situation.
 */
public class AnalysisSituationEvent extends SessionEvent {

    private final EngineAnalysisSituation analysisSituation;
    private final String language;

    /**
     * Instantiates a new instance of class {@linkplain AnalysisSituationEvent}.
     *
     * @param source            The source of the event.
     * @param sessionId The session id this event belongs to.
     * @param analysisSituation The analysis situation.
     * @param language          The language.
     */
    public AnalysisSituationEvent(Object source, String sessionId, EngineAnalysisSituation analysisSituation, String language) {
        super(source, sessionId);
        this.analysisSituation = analysisSituation;
        this.language = language;
    }

    /**
     * Gets the analysis situation.
     *
     * @return the analysis situation
     */
    public EngineAnalysisSituation getAnalysisSituation() {
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

    @Override
    public String toString() {
        return new StringJoiner(", ", AnalysisSituationEvent.class.getSimpleName() + "[", "]")
                .add("analysisSituation=" + analysisSituation)
                .add("language='" + language + "'")
                .add("sessionId='" + sessionId + "'")
                .add("source=" + source)
                .toString();
    }
}
