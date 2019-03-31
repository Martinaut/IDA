package at.jku.dke.inga.rules.services;

import at.jku.dke.inga.rules.models.OperationIntentServiceModel;
import at.jku.dke.inga.rules.results.StringConfidenceResult;
import org.kie.api.runtime.KieSession;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This service provides a method that determines the users operation intent.
 *
 * <p>Executes rules belonging to agenda-groups: {@code intent-determination}, {@code operation-intent-determination}</p>
 */
public class OperationIntentService extends DroolsService<OperationIntentServiceModel, Collection<StringConfidenceResult>> {

    /**
     * Instantiates a new instance of class {@linkplain OperationIntentService}.
     */
    public OperationIntentService() {
        super(new String[]{"intent-determination", "operation-intent-determination"});
    }

    /**
     * Executes the rules using the given model.
     *
     * @param session The new kie session.
     * @param model   The model required by the rules. It is guaranteed, that the model is not {@code null}.
     * @return Result of the query execution
     */
    @Override
    protected Collection<StringConfidenceResult> execute(KieSession session, OperationIntentServiceModel model) {
        logger.info("Determining the operation intent");

        // Add data
        session.insert(model);
        session.insert(model.getAnalysisSituation());

        // Execute rules
        session.fireAllRules();

        // Get results and close session
        Collection<?> result = session.getObjects(obj -> obj instanceof StringConfidenceResult);
        closeSession();

        // Sort and return
        return result.stream()
                .map(x -> (StringConfidenceResult) x)
                .sorted()
                .collect(Collectors.toList());
    }

}
