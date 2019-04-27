package at.jku.dke.ida.web.listeners;

import at.jku.dke.ida.scxml.session.SessionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * Listens to changes of web socket connections.
 */
@Component
public class WebSocketEventListener {

    private static final Logger LOGGER = LogManager.getLogger(WebSocketEventListener.class);

    /**
     * This method gets called when a new websocket session connected.
     *
     * @param evt The event data.
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent evt) {
        LOGGER.info("Received a new web socket connection. Timestamp: {}", evt.getTimestamp());
    }

    /**
     * This method gets called when the session of a WebSocket client using STOMP as the WebSocket sub-protocol is closed.
     *
     * @param evt The event data.
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent evt) {
        LOGGER.info("Web socket connection disconnected. Session: {}, Timestamp: {}", evt.getSessionId(), evt.getTimestamp());
        SessionManager.getInstance().deleteSession(evt.getSessionId());
    }
}
