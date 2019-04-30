package at.jku.dke.ida.web.listeners;

import at.jku.dke.ida.scxml.events.QueryResultEvent;
import at.jku.dke.ida.scxml.events.QueryResultListener;
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
 * Listens to available query results and sends them to the specified session.
 */
@Component()
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class QueryResultEventListener implements QueryResultListener {

    private static final Logger LOGGER = LogManager.getLogger(QueryResultEventListener.class);
    private SimpMessagingTemplate template;

    /**
     * Instantiates a new instance of class {@linkplain QueryResultEventListener}.
     *
     * @param template The messaging template used to send messages.
     */
    @Autowired
    public QueryResultEventListener(SimpMessagingTemplate template) {
        this.template = template;
    }

    /**
     * Invoked when the query result changed.
     *
     * @param evt The event to be processed.
     */
    @Override
    public void changed(QueryResultEvent evt) {
        LOGGER.info("QueryResultEvent fired: {}", evt);

        var headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(evt.getSessionId());
        headerAccessor.setLeaveMutable(true);

        template.convertAndSendToUser(
                evt.getSessionId(),
                "/queue/result",
                evt.getResult(),
                headerAccessor.getMessageHeaders()
        );
    }
}
