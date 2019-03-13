package at.jku.dke.inga.web.listener;

import at.jku.dke.inga.scxml.events.AnalysisSituationEvent;
import at.jku.dke.inga.scxml.events.AnalysisSituationListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Listens to analysis situation changes and sends them to the specified session.
 */
@Component()
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AnalysisSituationEventListener implements AnalysisSituationListener {

    private SimpMessagingTemplate template;

    /**
     * Instantiates a new instance of class {@linkplain AnalysisSituationEventListener}.
     *
     * @param template The messaging template used to send messages.
     */
    @Autowired
    public AnalysisSituationEventListener(SimpMessagingTemplate template) {
        this.template = template;
    }

    /**
     * Invoked when the analysis situation changed
     *
     * @param sessionId The session identifier.
     * @param evt       The event to be processed.
     */
    @Override
    public void changed(String sessionId, AnalysisSituationEvent evt) {
        var headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);

        template.convertAndSendToUser(
                sessionId,
                "/queue/as",
                evt.getAnalysisSituation(),
                headerAccessor.getMessageHeaders());

    }
}
