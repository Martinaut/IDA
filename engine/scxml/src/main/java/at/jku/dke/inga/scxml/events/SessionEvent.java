package at.jku.dke.inga.scxml.events;

import java.util.EventObject;
import java.util.StringJoiner;

/**
 * Base class for all session-dependent events containing a session id.
 */
public abstract class SessionEvent extends EventObject {

    /**
     * The session id.
     */
    protected final String sessionId;

    /**
     * Instantiates a new instance of class {@linkplain AnalysisSituationEvent}.
     *
     * @param source    The source of the event.
     * @param sessionId The session id this event belongs to.
     */
    protected SessionEvent(Object source, String sessionId) {
        super(source);
        this.sessionId = sessionId;
    }

    /**
     * Gets the session id this event belongs to.
     *
     * @return the session id
     */
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SessionEvent.class.getSimpleName() + "[", "]")
                .add("sessionId='" + sessionId + "'")
                .add("source=" + source)
                .toString();
    }
}
