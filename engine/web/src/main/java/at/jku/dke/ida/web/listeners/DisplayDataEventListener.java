package at.jku.dke.ida.web.listeners;

import at.jku.dke.ida.scxml.events.DisplayEvent;
import at.jku.dke.ida.scxml.events.DisplayListener;
import at.jku.dke.ida.web.models.DisplayResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger LOGGER = LogManager.getLogger(DisplayDataEventListener.class);
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
     * @param evt The event to be processed.
     */
    @Override
    public void displayDataAvailable(DisplayEvent evt) {
        LOGGER.info("DisplayEvent fired: {}", evt);

        var headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(evt.getSessionId());
        headerAccessor.setLeaveMutable(true);

        template.convertAndSendToUser(
                evt.getSessionId(),
                "/queue/result",
                new DisplayResult(evt.getDisplay()),
                headerAccessor.getMessageHeaders());
    }
}
