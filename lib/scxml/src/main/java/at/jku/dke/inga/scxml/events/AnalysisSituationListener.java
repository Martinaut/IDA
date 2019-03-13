package at.jku.dke.inga.scxml.events;

import java.util.EventListener;

/**
 * The listener interface for receiving events when the analysis situation changed.
 */
public interface AnalysisSituationListener extends EventListener {
    /**
     * Invoked when the analysis situation changed
     *
     * @param sessionId The session identifier.
     * @param evt       The event to be processed.
     */
    void changed(String sessionId, AnalysisSituationEvent evt);
}