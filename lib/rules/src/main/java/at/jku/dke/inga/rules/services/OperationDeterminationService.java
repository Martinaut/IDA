package at.jku.dke.inga.rules.services;

import at.jku.dke.inga.rules.results.StringValueConfidenceResult;
import at.jku.dke.inga.rules.models.OperationDeterminationServiceModel;
import at.jku.dke.inga.shared.EventNames;

import java.util.Collection;

/**
 * This service provides a method that determines the operation.
 */
public class OperationDeterminationService extends DroolsService<OperationDeterminationServiceModel, String> {

    /**
     * Instantiates a new instance of class {@linkplain OperationDeterminationService}.
     */
    public OperationDeterminationService() {
        super(new String[]{"operation-determination"});
    }

    /**
     * Executes the rules using the given model.
     *
     * @param model The model required by the rules.
     * @return Result of the query execution
     * @throws IllegalArgumentException If the {@code model} is {@code null}.
     */
    @SuppressWarnings("Duplicates")
    @Override
    public String executeRules(OperationDeterminationServiceModel model) {
        if (model == null) throw new IllegalArgumentException("model must not be null.");

        logger.info("Determining the operation");
        var session = getSession();

        // Add data
        session.insert(model);

        // Execute rules
        session.fireAllRules();

        // Get result and close session
        Collection<?> objs = getSession().getObjects(obj -> obj instanceof StringValueConfidenceResult);
        closeSession();

        return objs.stream()
                .map(x -> (StringValueConfidenceResult) x)
                .sorted()
                .findFirst().orElse(new StringValueConfidenceResult(EventNames.INVALID_INPUT)).getValue();
    }

}
