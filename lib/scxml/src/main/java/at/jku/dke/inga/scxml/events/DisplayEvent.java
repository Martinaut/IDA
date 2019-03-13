package at.jku.dke.inga.scxml.events;

import at.jku.dke.inga.shared.display.Display;

import java.util.EventObject;

/**
 * The event object with display data.
 */
public class DisplayEvent extends EventObject {

    private final Display display;

    /**
     * Instantiates a new instance of class {@linkplain DisplayEvent}.
     *
     * @param source  The source of the event.
     * @param display The display data.
     */
    public DisplayEvent(Object source, Display display) {
        super(source);
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
}
