package at.jku.dke.inga.rules.services;

import at.jku.dke.inga.rules.models.OperationDisplayServiceModel;
import at.jku.dke.inga.shared.operations.Operation;
import org.kie.api.runtime.KieSession;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This service provides a method that returns the possible operations a user can execute,
 * based on the current analysis situation.
 *
 * <p>Executes rules belonging to agenda-group: {@code operation-display-determination}</p>
 */
public class OperationDisplayService extends DroolsService<OperationDisplayServiceModel, Collection<Operation>> {

    /**
     * Instantiates a new instance of class {@linkplain OperationDisplayService}.
     */
    public OperationDisplayService() {
        super(new String[]{"operation-display-determination"});
    }

    /**
     * Executes the rules using the given model.
     *
     * @param session The new kie session.
     * @param model   The model required by the rules. It is guaranteed, that the model is not {@code null}.
     * @return Result of the query execution
     */
    @Override
    protected Collection<Operation> execute(KieSession session, OperationDisplayServiceModel model) {
        logger.info("Determining possible actions a user can execute based on current analysis situation.");

        // Add data
        session.insert(model);
        session.insert(model.getAnalysisSituation());

        // Execute rules
        session.fireAllRules();

        // Get results and close session
        Collection<?> result = session.getObjects(obj -> obj instanceof Operation);
        closeSession();

        // Sort and return
        return result.stream()
                .map(x -> (Operation) x)
                .sorted()
                .collect(Collectors.toList());
    }

}
