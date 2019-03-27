package at.jku.dke.inga.web.listener;

import at.jku.dke.inga.data.models.Label;
import at.jku.dke.inga.data.repositories.SimpleRepository;
import at.jku.dke.inga.scxml.events.AnalysisSituationEvent;
import at.jku.dke.inga.scxml.events.AnalysisSituationListener;
import at.jku.dke.inga.shared.models.AnalysisSituation;
import at.jku.dke.inga.shared.models.ComparativeAnalysisSituation;
import at.jku.dke.inga.shared.models.DimensionQualification;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
        // Get Labels
        Set<String> uris = new HashSet<>();
        uris.add(evtAs.getCube());
        uris.addAll(evtAs.getMeasures());
        uris.addAll(evtAs.getBaseMeasureConditions());
        uris.addAll(evtAs.getFilterConditions());
        evtAs.getDimensionQualifications().forEach(dq -> {
            uris.add(dq.getDimension());
            uris.add(dq.getGranularityLevel());
            uris.add(dq.getDiceNode());
            uris.add(dq.getDiceLevel());
            uris.addAll(dq.getSliceConditions());
        });
        Map<String, Label> lbls = simpleRepository.findLabelsByUriAndLang(uris, lang);

        // Set labels
        NonComparativeAnalysisSituation newAs = new NonComparativeAnalysisSituation();
        newAs.setName(evtAs.getName());
        newAs.setDescription(evtAs.getDescription());
        newAs.setCube(lbls.getOrDefault(evtAs.getCube(), new Label(evtAs.getCube())).getLabel());
        evtAs.getMeasures().forEach(m -> newAs.addMeasure(lbls.getOrDefault(m, new Label(m)).getLabel()));
        evtAs.getBaseMeasureConditions().forEach(bm -> newAs.addMeasure(lbls.getOrDefault(bm, new Label(bm)).getLabel()));
        evtAs.getFilterConditions().forEach(f -> newAs.addMeasure(lbls.getOrDefault(f, new Label(f)).getLabel()));
        evtAs.getDimensionQualifications().forEach(dq -> {
            DimensionQualification mapped = new DimensionQualification();
            mapped.setDimension(lbls.getOrDefault(dq.getDimension(), new Label(dq.getDimension())).getLabel());
            mapped.setGranularityLevel(lbls.getOrDefault(dq.getGranularityLevel(), new Label(dq.getGranularityLevel())).getLabel());
            mapped.setDiceNode(lbls.getOrDefault(dq.getDiceNode(), new Label(dq.getDiceNode())).getLabel());
            mapped.setDiceLevel(lbls.getOrDefault(dq.getDiceLevel(), new Label(dq.getDiceLevel())).getLabel());
            dq.getSliceConditions().forEach(sc -> mapped.getSliceConditions().add(lbls.getOrDefault(sc, new Label(sc)).getLabel()));
            newAs.addDimensionQualification(mapped);
        });
        return newAs;
    }
}
