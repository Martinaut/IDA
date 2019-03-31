package at.jku.dke.inga.rules.services;

import at.jku.dke.inga.rules.models.OperationServiceModel;
import at.jku.dke.inga.rules.results.StringConfidenceResult;
import org.kie.api.runtime.KieSession;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This service provides a method that determines the selected operation.
 *
 * <p>Executes rules belonging to agenda-group: {@code operation-determination}</p>
 */
public class OperationService extends DroolsService<OperationServiceModel, Collection<StringConfidenceResult>>  {

    /**
     * Instantiates a new instance of class {@linkplain OperationService}.
     */
    public OperationService() {
        super(new String[]{"operation-determination"});
    }

    /**
     * Executes the rules using the given model.
     *
     * @param session The new kie session.
     * @param model   The model required by the rules. It is guaranteed, that the model is not {@code null}.
     * @return Result of the query execution
     */
    @Override
    protected Collection<StringConfidenceResult> execute(KieSession session, OperationServiceModel model) {
        logger.info("Determining the operation");

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
