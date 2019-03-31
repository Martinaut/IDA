package at.jku.dke.inga.scxml.events;

import at.jku.dke.inga.shared.models.AnalysisSituation;

import java.util.StringJoiner;

/**
 * The event object with analysis situation.
 */
public class AnalysisSituationEvent extends SessionEvent {

    private final AnalysisSituation analysisSituation;
    private final String language;

    /**
     * Instantiates a new instance of class {@linkplain AnalysisSituationEvent}.
     *
     * @param source            The source of the event.
     * @param sessionId The session id this event belongs to.
     * @param analysisSituation The analysis situation.
     * @param language          The language.
     */
    public AnalysisSituationEvent(Object source, String sessionId, AnalysisSituation analysisSituation, String language) {
        super(source, sessionId);
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
