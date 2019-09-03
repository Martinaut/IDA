package at.jku.dke.ida.nlp.drools;

import at.jku.dke.ida.data.models.similarity.CubeSimilarity;
import at.jku.dke.ida.nlp.models.GraphDBSimilarityServiceModel;
import at.jku.dke.ida.rules.services.DroolsService;
import org.kie.api.runtime.KieSession;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This service returns all similarities found for the given word groups using GraphDB.
 *
 * <p>Executes rules belonging to agenda-group: {@code graphdb-similarity}</p>
 */
public class GraphDBSimilarityService extends DroolsService<GraphDBSimilarityServiceModel, Set<CubeSimilarity>> {

    /**
     * Instantiates a new instance of class {@linkplain GraphDBSimilarityService}.
     */
    public GraphDBSimilarityService() {
        super(new String[]{"graphdb-similarity"});
    }

    /**
     * Executes the rules using the given model.
     *
     * @param session The new kie session.
     * @param model   The model required by the rules. It is guaranteed, that the model is not {@code null}.
     * @return Result of the query execution
     */
    @Override
    protected Set<CubeSimilarity> execute(KieSession session, GraphDBSimilarityServiceModel model) {
        logger.info("Finding similarities using GraphDB.");

        // Add data
        session.insert(model);
        model.getWordGroups().forEach(session::insert);

        // Execute rules
        session.fireAllRules();

        // Get results
        Collection<?> result = session.getObjects(obj -> obj instanceof CubeSimilarity);
        closeSession();

        return result.stream()
                .map(x -> (CubeSimilarity) x)
                .collect(Collectors.toSet());
    }
}