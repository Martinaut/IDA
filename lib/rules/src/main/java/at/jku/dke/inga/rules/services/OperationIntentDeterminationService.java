package at.jku.dke.inga.rules.services;

import at.jku.dke.inga.rules.helpers.ConfidenceResult;
import at.jku.dke.inga.rules.models.OperationIntentDeterminationServiceModel;
import at.jku.dke.inga.shared.EventNames;

import java.util.Collection;

/**
 * This service provides a method that determines the users intent and returns the appropriate event.
 */
public class OperationIntentDeterminationService extends DroolsService<OperationIntentDeterminationServiceModel, String> {

    /**
     * Instantiates a new instance of class {@linkplain OperationIntentDeterminationService}.
     */
    public OperationIntentDeterminationService() {
        super(new String[]{"operation-intent-determination", "intent-determination"});
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
    public String executeRules(OperationIntentDeterminationServiceModel model) {
        if (model == null) throw new IllegalArgumentException("model must not be null.");

        logger.info("Determining the users operation intent");
        var session = getSession();

        // Add data
        session.insert(model);

        // Execute rules
        session.fireAllRules();

        // Get result and close session
        Collection<?> objs = getSession().getObjects(obj -> obj instanceof ConfidenceResult);
        closeSession();

        return objs.stream()
                .map(x -> (ConfidenceResult) x)
                .sorted()
                .findFirst().orElse(new ConfidenceResult(EventNames.INVALID_INPUT)).getValue();
    }
}
