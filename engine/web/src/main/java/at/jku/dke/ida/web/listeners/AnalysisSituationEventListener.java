package at.jku.dke.ida.web.listeners;

import at.jku.dke.ida.data.repositories.SimpleRepository;
import at.jku.dke.ida.scxml.events.AnalysisSituationEvent;
import at.jku.dke.ida.scxml.events.AnalysisSituationListener;
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
 * Listens to analysis situation changes and sends them to the specified session.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AnalysisSituationEventListener implements AnalysisSituationListener {

    private static final Logger LOGGER = LogManager.getLogger(AnalysisSituationEventListener.class);
    private SimpMessagingTemplate template;
    private SimpleRepository simpleRepository;

    /**
     * Instantiates a new instance of class {@linkplain AnalysisSituationEventListener}.
     *
     * @param template         The messaging template used to send messages.
     * @param simpleRepository The simple repository.
     */
    @Autowired
    public AnalysisSituationEventListener(SimpMessagingTemplate template, SimpleRepository simpleRepository) {
        this.template = template;
        this.simpleRepository = simpleRepository;
    }

    /**
     * Invoked when the analysis situation changed
     *
     * @param evt The event to be processed.
     */
    @Override
    public void changed(AnalysisSituationEvent evt) {
        LOGGER.info("AnalysisSituationEvent fired: {}", evt);

        var headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(evt.getSessionId());
        headerAccessor.setLeaveMutable(true);

        template.convertAndSendToUser(
                evt.getSessionId(),
                "/queue/as",
                AnalysisSituationConverter.convert(evt.getLanguage(), this.simpleRepository, evt.getAnalysisSituation()),
                headerAccessor.getMessageHeaders());
    }
}
