package at.jku.dke.inga.rules.services;

import at.jku.dke.inga.rules.models.ValueIntentDeterminationServiceModel;

import java.util.Collection;

/**
 * This service provides a method that determines the users intent and returns the appropriate event.
 */
public class ValueIntentDeterminationService extends DroolsService<ValueIntentDeterminationServiceModel, String> {

    /**
     * Instantiates a new instance of class {@linkplain ValueIntentDeterminationService}.
     */
    public ValueIntentDeterminationService() {
        super(new String[]{"value-intent-determination"});
    }

    /**
     * Executes the rules using the given model.
     *
     * @param model The model required by the rules.
     * @return Result of the query execution
     * @throws IllegalArgumentException If the {@code model} is {@code null}.
     */
    @Override
    public String executeRules(ValueIntentDeterminationServiceModel model) {
        if (model == null) throw new IllegalArgumentException("model must not be null.");

        logger.info("Determining the users value intent");
        var session = getSession();

        // Add data
        session.insert(model);

        // Execute rules
        session.fireAllRules();

        // Get result and close session
        Collection<?> objs = getSession().getObjects(obj -> obj instanceof String);
        closeSession();

        return (String) objs.stream().findFirst().orElse(null);
    }
}
