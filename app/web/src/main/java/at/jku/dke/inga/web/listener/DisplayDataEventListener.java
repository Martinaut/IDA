package at.jku.dke.inga.web.listener;

import at.jku.dke.inga.scxml.events.DisplayEventData;
import at.jku.dke.inga.scxml.events.DisplayListener;
import at.jku.dke.inga.web.models.DisplayResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Listens to available display data and sends them to the specified session.
 */
@Component()
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DisplayDataEventListener implements DisplayListener {

    private SimpMessagingTemplate template;

    /**
     * Instantiates a new instance of class {@linkplain DisplayDataEventListener}.
     *
     * @param template The messaging template used to send messages.
     */
    @Autowired
    public DisplayDataEventListener(SimpMessagingTemplate template) {
        this.template = template;
    }

    /**
     * Invoked when new display data are available.
     *
     * @param sessionId The session identifier the data belong to.
     * @param evt       The event to be processed.
     */
    @Override
    public void displayDataAvailable(String sessionId, DisplayEventData evt) {
        var headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);

        template.convertAndSendToUser(
                sessionId,
                "/queue/result",
                new DisplayResult(evt.getDisplay()),
                headerAccessor.getMessageHeaders());
    }
}
