package at.jku.dke.ida.nlp.drools;

import at.jku.dke.ida.nlp.models.StringSimilarityServiceModel;
import at.jku.dke.ida.data.models.Similarity;
import at.jku.dke.ida.rules.services.DroolsService;
import at.jku.dke.ida.shared.display.Displayable;
import org.kie.api.runtime.KieSession;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This service returns all similarities found for the given word groups using String similarity algorithms.
 *
 * <p>Executes rules belonging to agenda-group: {@code string-similarity}</p>
 */
public class StringSimilarityService extends DroolsService<StringSimilarityServiceModel, Set<Similarity<Displayable>>> {

    /**
     * Instantiates a new instance of class {@linkplain StringSimilarityService}.
     */
    public StringSimilarityService() {
        super(new String[]{"string-similarity"});
    }

    /**
     * Executes the rules using the given model.
     *
     * @param session The new kie session.
     * @param model   The model required by the rules. It is guaranteed, that the model is not {@code null}.
     * @return Result of the query execution
     */
    @Override
    protected Set<Similarity<Displayable>> execute(KieSession session, StringSimilarityServiceModel model) {
        logger.info("Finding similarities using similarity algorithms.");

        // Add data
        session.insert(model);
        model.getPossibleValues().forEach(session::insert);

        // Execute rules
        session.fireAllRules();

        // Get results
        Collection<?> result = session.getObjects(obj -> obj instanceof Similarity && ((Similarity) obj).getElement() instanceof Displayable);
        closeSession();

        return result.stream()
                .map(x -> (Similarity<Displayable>) x)
                .collect(Collectors.toSet());
    }
}