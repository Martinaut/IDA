package at.jku.dke.inga.web.listeners;

import at.jku.dke.inga.data.QueryException;
import at.jku.dke.inga.data.models.Label;
import at.jku.dke.inga.data.repositories.SimpleRepository;
import at.jku.dke.inga.scxml.events.AnalysisSituationEvent;
import at.jku.dke.inga.scxml.events.AnalysisSituationListener;
import at.jku.dke.inga.shared.models.AnalysisSituation;
import at.jku.dke.inga.shared.models.ComparativeAnalysisSituation;
import at.jku.dke.inga.shared.models.DimensionQualification;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

/**
 * Listens to analysis situation changes and sends them to the specified session.
 */
@Component()
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

        AnalysisSituation as;
        if (evt.getAnalysisSituation() instanceof NonComparativeAnalysisSituation) {
            as = translate((NonComparativeAnalysisSituation) evt.getAnalysisSituation(), evt.getLanguage());
        } else {
            if (evt.getAnalysisSituation() instanceof ComparativeAnalysisSituation) {
                as = translate((ComparativeAnalysisSituation) evt.getAnalysisSituation(), evt.getLanguage());
            } else {
                throw new IllegalStateException("Analysis Situation is of an invalid type. This exception should never be thrown.");
            }
        }

        template.convertAndSendToUser(
                evt.getSessionId(),
                "/queue/as",
                as,
                headerAccessor.getMessageHeaders());

    }

    private NonComparativeAnalysisSituation translate(NonComparativeAnalysisSituation evtAs, String lang) {
        LOGGER.debug("Translating NonComparativeAnalysisSituation with cube {}.", evtAs.getCube());

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

        try {
            Map<String, Label> lbls = simpleRepository.getLabelsByLangAndIris(lang, uris);

            // Set labels
            NonComparativeAnalysisSituation newAs = new NonComparativeAnalysisSituation();
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
        } catch (QueryException ex) {
            LOGGER.error("Could not load translations of NonComparativeAnalysisSituation.", ex);
        }
        return evtAs;
    }

    private ComparativeAnalysisSituation translate(ComparativeAnalysisSituation evtAs, String lang) {
        LOGGER.debug("Translating ComparativeAnalysisSituation.");
        ComparativeAnalysisSituation newAs = new ComparativeAnalysisSituation();
        newAs.setContextOfInterest(translate(evtAs.getContextOfInterest(), lang));
        newAs.setContextOfComparison(translate(evtAs.getContextOfComparison(), lang));

        // Get Labels
        Set<String> uris = new HashSet<>();
        uris.addAll(evtAs.getScores());
        uris.addAll(evtAs.getScoreFilters());
        evtAs.getJoinConditions().forEach(jc -> {
            uris.add(jc.getLeft());
            uris.add(jc.getRight());
        });

        try {
            Map<String, Label> lbls = simpleRepository.getLabelsByLangAndIris(lang, uris);

            // Set labels
            evtAs.getScores().forEach(s -> newAs.addScore(lbls.getOrDefault(s, new Label(s)).getLabel()));
            evtAs.getScoreFilters().forEach(sf -> newAs.addScoreFilter(lbls.getOrDefault(sf, new Label(sf)).getLabel()));
            evtAs.getJoinConditions().forEach(jc -> newAs.addJoinCondition(new ImmutablePair<>(
                    lbls.getOrDefault(jc.getLeft(), new Label(jc.getLeft())).getLabel(),
                    lbls.getOrDefault(jc.getRight(), new Label(jc.getRight())).getLabel()
            )));
            return newAs;
        } catch (QueryException ex) {
            LOGGER.error("Could not load translations of ComparativeAnalysisSituation.", ex);
        }
        return newAs;
    }
}
