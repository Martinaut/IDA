package at.jku.dke.inga.rules.services;

import at.jku.dke.inga.rules.models.OperationDisplayDeterminationServiceModel;
import at.jku.dke.inga.shared.operations.Operation;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This service provides a method that returns the possible operations a user can executed,
 * based on the current analysis situation.
 */
public class OperationDisplayDeterminationService extends DroolsService<OperationDisplayDeterminationServiceModel, Collection<Operation>> {

    /**
     * Instantiates a new instance of class {@linkplain OperationDisplayDeterminationService}.
     */
    public OperationDisplayDeterminationService() {
        super(new String[]{"operations-display-determination"});
    }

    /**
     * Executes the rules using the given model.
     *
     * @param model The model required by the rules.
     * @return Result of the query execution
     * @throws IllegalArgumentException If the {@code model} is {@code null}.
     */
    @Override
    public Collection<Operation> executeRules(OperationDisplayDeterminationServiceModel model) {
        if (model == null) throw new IllegalArgumentException("model must not be null.");

        logger.info("Determining possible actions a user can execute based on current analysis situation");
        var session = getSession();

        // Add data
        session.insert(model);
        session.insert(model.getAnalysisSituation());

        // Execute rules
        session.fireAllRules();

        // Get results and close session
        Collection<?> objs = getSession().getObjects(object -> object instanceof Operation);
        closeSession();

        return objs.stream()
                .map(x -> (Operation) x)
                .sorted()
                .collect(Collectors.toList());
    }
}
