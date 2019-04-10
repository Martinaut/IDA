package at.jku.dke.inga.app.ruleset.drools;

import at.jku.dke.inga.app.ruleset.models.WordGroup;
import at.jku.dke.inga.app.ruleset.models.WordGroupsServiceModel;
import at.jku.dke.inga.app.ruleset.nlp.NLPProcessor;
import at.jku.dke.inga.rules.services.DroolsService;
import org.kie.api.runtime.KieSession;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This service returns all word groups of a text.
 * Here, a word group can be a single word or a group of words belonging together.
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
        model.setAnnotatedText(NLPProcessor.annotate(model.getLanguage(), model.getInitialSentence()));

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
