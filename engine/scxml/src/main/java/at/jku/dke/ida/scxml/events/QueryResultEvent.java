package at.jku.dke.ida.scxml.events;

import java.util.StringJoiner;

/**
 * The event object with a query result.
 */
public class QueryResultEvent extends SessionEvent {

    private final String result;

    /**
     * Instantiates a new instance of class {@linkplain QueryResultEvent}.
     *
     * @param source    The source of the event.
     * @param sessionId The session id this event belongs to.
     * @param result    The query result.
     */
    public QueryResultEvent(Object source, String sessionId, String result) {
        super(source, sessionId);
        this.result = result;
    }

    /**
     * Gets the result.
     *
     * @return the result
     */
    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", QueryResultEvent.class.getSimpleName() + "[", "]")
                .add("result=" + result)
                .add("sessionId='" + sessionId + "'")
                .add("source=" + source)
                .toString();
    }
}
