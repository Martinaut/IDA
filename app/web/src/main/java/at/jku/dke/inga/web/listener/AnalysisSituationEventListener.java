package at.jku.dke.inga.web.listener;

import at.jku.dke.inga.data.repositories.SimpleRepository;
import at.jku.dke.inga.scxml.events.AnalysisSituationEvent;
import at.jku.dke.inga.scxml.events.AnalysisSituationListener;
import at.jku.dke.inga.shared.models.AnalysisSituation;
import at.jku.dke.inga.shared.models.ComparativeAnalysisSituation;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Listens to analysis situation changes and sends them to the specified session.
 */
@Component()
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AnalysisSituationEventListener implements AnalysisSituationListener {

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
     * @param sessionId The session identifier.
     * @param evt       The event to be processed.
     */
    @Override
    public void changed(String sessionId, AnalysisSituationEvent evt) {
        var headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);

        AnalysisSituation as = null;
        if (evt.getAnalysisSituation() instanceof NonComparativeAnalysisSituation) {
            as = translate((NonComparativeAnalysisSituation) evt.getAnalysisSituation(), evt.getLanguage());
        } else {
            if (evt.getAnalysisSituation() instanceof ComparativeAnalysisSituation) {
                ComparativeAnalysisSituation evtAs = (ComparativeAnalysisSituation) evt.getAnalysisSituation();
                ComparativeAnalysisSituation newAs = new ComparativeAnalysisSituation();
                newAs.setName(evtAs.getName());
                newAs.setDescription(evtAs.getDescription());
                newAs.setContextOfInterest(translate(evtAs.getContextOfInterest(), evt.getLanguage()));
                newAs.setContextOfComparison(translate(evtAs.getContextOfComparison(), evt.getLanguage()));
                // TODO
                as = newAs;
            }
        }

        template.convertAndSendToUser(
                sessionId,
                "/queue/as",
                as,
                headerAccessor.getMessageHeaders());

    }

    private NonComparativeAnalysisSituation translate(NonComparativeAnalysisSituation evtAs, String lang) {
        NonComparativeAnalysisSituation newAs = new NonComparativeAnalysisSituation();
        newAs.setCube(simpleRepository.findLabelByUriAndLang(evtAs.getCube(), lang).getLabel());
        newAs.setBaseMeasureConditions(evtAs.getBaseMeasureConditions().stream().map(x -> simpleRepository.findLabelByUriAndLang(x, lang).getTitle()).collect(Collectors.toSet()));
        // TODO newAs.setDimensionQualifications(evtAs.getDimensionQualifications().stream().map(x -> simpleRepository.findLabelByUriAndLang(x, lang).getTitle()).collect(Collectors.toSet()));
        newAs.setFilterConditions(evtAs.getFilterConditions().stream().map(x -> simpleRepository.findLabelByUriAndLang(x, lang).getTitle()).collect(Collectors.toSet()));
        newAs.setMeasures(evtAs.getMeasures().stream().map(x -> simpleRepository.findLabelByUriAndLang(x, lang).getTitle()).collect(Collectors.toSet()));
        newAs.setName(evtAs.getName());
        newAs.setDescription(evtAs.getDescription());
        return newAs;
    }
}
