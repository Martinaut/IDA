package at.jku.dke.inga.app.ruleset.drools;

import at.jku.dke.inga.app.ruleset.models.SimilarityServiceModel;
import at.jku.dke.inga.data.models.Similarity;
import at.jku.dke.inga.rules.services.DroolsService;
import org.kie.api.runtime.KieSession;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This service returns all similarities found for the given word groups.
 */
public class SimilarityService extends DroolsService<SimilarityServiceModel, Set<Similarity>> {

    /**
     * Instantiates a new instance of class {@linkplain WordGroupsService}.
     */
    public SimilarityService() {
        super(new String[]{"similarity"});
    }

    /**
     * Executes the rules using the given model.
     *
     * @param session The new kie session.
     * @param model   The model required by the rules. It is guaranteed, that the model is not {@code null}.
     * @return Result of the query execution
     */
    @Override
    protected Set<Similarity> execute(KieSession session, SimilarityServiceModel model) {
        logger.info("Finding similarities.");

        // Add data
        session.insert(model);
        model.getWordGroups().forEach(session::insert);

        // Execute rules
        session.fireAllRules();

        // Get results
        Collection<?> result = session.getObjects(obj -> obj instanceof Similarity);
        closeSession();

        return result.stream()
                .map(x -> (Similarity) x)
                .collect(Collectors.toSet());
    }
}