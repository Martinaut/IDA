package at.jku.dke.ida.app.nlp.drools;

import at.jku.dke.ida.app.nlp.NLPProcessor;
import at.jku.dke.ida.app.nlp.models.WordGroup;
import at.jku.dke.ida.app.nlp.models.WordGroupsServiceModel;
import at.jku.dke.ida.rules.services.DroolsService;
import org.kie.api.runtime.KieSession;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This service returns all word groups of a text.
 * Here, a word group can be a single word or a group of words belonging together.
 *
 * <p>Executes rules belonging to agenda-group: {@code word-groups}</p>
 */
public class WordGroupsService extends DroolsService<WordGroupsServiceModel, Set<WordGroup>> {

    /**
     * Instantiates a new instance of class {@linkplain WordGroupsService}.
     */
    public WordGroupsService() {
        super(new String[]{"word-groups"});
    }

    /**
     * Executes the rules using the given model.
     *
     * @param session The new kie session.
     * @param model   The model required by the rules. It is guaranteed, that the model is not {@code null}.
     * @return Result of the query execution
     */
    @Override
    protected Set<WordGroup> execute(KieSession session, WordGroupsServiceModel model) {
        logger.info("Finding word groups.");

        // Set additional model data
        model.setAnnotatedText(NLPProcessor.annotate(model.getLanguage(), model.getSentence()));

        // Add data
        session.insert(model);

        // Execute rules
        session.fireAllRules();

        // Get results
        Collection<?> result = session.getObjects(obj -> obj instanceof WordGroup);
        closeSession();

        return result.stream().map(x -> (WordGroup) x).collect(Collectors.toSet());
    }
}
