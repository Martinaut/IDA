package at.jku.dke.ida.scxml.events;

import java.util.EventListener;

/**
 * The listener interface for receiving display data events.
 */
public interface DisplayListener extends EventListener {
    /**
     * Invoked when new display data are available.
     *
     * @param evt The event to be processed.
     */
    void displayDataAvailable(DisplayEvent evt);
}