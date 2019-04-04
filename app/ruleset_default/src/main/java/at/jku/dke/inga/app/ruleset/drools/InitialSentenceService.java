package at.jku.dke.inga.app.ruleset.drools;

import at.jku.dke.inga.app.nlp.NLPProcessor;
import at.jku.dke.inga.data.QueryException;
import at.jku.dke.inga.data.models.Similarity;
import at.jku.dke.inga.data.repositories.SimpleRepository;
import at.jku.dke.inga.rules.services.DroolsService;
import at.jku.dke.inga.shared.spring.BeanUtil;
import org.kie.api.runtime.KieSession;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * This service tries to match parts of the sentence to schema elements and tries to create an
 * analysis situation out of it.
 */
public class InitialSentenceService extends DroolsService<InitialSentenceModel, Void> {

    /**
     * Instantiates a new instance of class {@linkplain InitialSentenceService}.
     */
    public InitialSentenceService() {
        super(new String[]{"initial-sentence"});
    }

    /**
     * Executes the rules using the given model.
     *
     * @param session The new kie session.
     * @param model   The model required by the rules. It is guaranteed, that the model is not {@code null}.
     * @return Result of the query execution
     */
    @Override
    protected Void execute(KieSession session, InitialSentenceModel model) {
        logger.info("Mapping elements of initial sentence to analysis situation");

        // Set additional model data
        try {
            model.setAllLabels(BeanUtil.getBean(SimpleRepository.class).getLabelsByLang(model.getLanguage()));
            model.setAnnotatedText(NLPProcessor.annotate(model.getLanguage(), model.getInitialSentence()));
        } catch (QueryException ex) {
            logger.fatal("Could not load required data from graph db.", ex);
        }

        // Add data
        session.insert(model);

        // Execute rules
        session.fireAllRules();

        // Get results and close session
        Collection<?> result = session.getObjects(obj -> obj instanceof Similarity);
        result = result.stream()
                .sorted()
                .collect(Collectors.toList());
        closeSession();

        // Determine most probable analysis situation
        // TODO

        // Session s = SessionManager.getInstance().getSession(model.getSessionId());
        // if (s != null)
        //    s.setCubeSetFlag(true);

        // Exit
        return null;
    }

}
