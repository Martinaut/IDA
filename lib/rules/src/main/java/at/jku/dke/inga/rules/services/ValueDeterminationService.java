package at.jku.dke.inga.rules.services;

import at.jku.dke.inga.rules.results.ConfidenceResult;
import at.jku.dke.inga.rules.results.StringValueConfidenceResult;
import at.jku.dke.inga.rules.models.ValueDeterminationServiceModel;
import at.jku.dke.inga.shared.EventNames;

import java.util.Collection;

/**
 * This service provides a method that determines the value.
 */
public class ValueDeterminationService extends DroolsService<ValueDeterminationServiceModel, Object> {

    /**
     * Instantiates a new instance of class {@linkplain ValueDeterminationService}.
     */
    public ValueDeterminationService() {
        super(new String[]{"value-determination"});
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
    public ConfidenceResult executeRules(ValueDeterminationServiceModel model) {
        if (model == null) throw new IllegalArgumentException("model must not be null.");

        logger.info("Determining the value");
        var session = getSession();

        // Add data
        session.insert(model);
        session.insert(model.getAnalysisSituation());
        session.insert(model.getDisplayData());

        // Execute rules
        session.fireAllRules();

        // Get result and close session
        Collection<?> objs = getSession().getObjects(obj -> obj instanceof ConfidenceResult);
        closeSession();

        // Get value
        return objs.stream()
                .map(x -> (ConfidenceResult) x)
                .sorted()
                .findFirst()
                .orElse(new StringValueConfidenceResult(EventNames.INVALID_INPUT));
    }
}
