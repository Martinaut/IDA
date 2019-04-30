package at.jku.dke.ida.scxml.events;

import java.util.EventListener;

/**
 * The listener interface for receiving events when a query result is available.
 */
public interface QueryResultListener extends EventListener {
    /**
     * Invoked when the query result changed.
     *
     * @param evt The event to be processed.
     */
    void changed(QueryResultEvent evt);
}