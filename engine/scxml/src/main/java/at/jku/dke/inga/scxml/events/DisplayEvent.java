package at.jku.dke.inga.scxml.events;

import at.jku.dke.inga.shared.display.Display;

import java.util.StringJoiner;

/**
 * The event object with display data.
 */
public class DisplayEvent extends SessionEvent {

    private final Display display;

    /**
     * Instantiates a new instance of class {@linkplain DisplayEvent}.
     *
     * @param source  The source of the event.
     * @param sessionId The session id this event belongs to.
     * @param display The display data.
     */
    public DisplayEvent(Object source, String sessionId, Display display) {
        super(source, sessionId);
        this.display = display;
    }

    /**
     * Gets the display data.
     *
     * @return The display data.
     */
    public Display getDisplay() {
        return display;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DisplayEvent.class.getSimpleName() + "[", "]")
                .add("display=" + display)
                .add("sessionId='" + sessionId + "'")
                .add("source=" + source)
                .toString();
    }
}
