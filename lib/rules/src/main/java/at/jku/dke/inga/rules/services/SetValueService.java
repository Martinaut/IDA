package at.jku.dke.inga.rules.services;

import at.jku.dke.inga.rules.models.SetValueServiceModel;

/**
 * This service provides a method that sets the value according to the selected operation.
 */
public class SetValueService extends DroolsService<SetValueServiceModel, Void> {

    /**
     * Instantiates a new instance of class {@linkplain SetValueService}.
     */
    public SetValueService() {
        super(new String[]{"set-value"});
    }

    /**
     * Executes the rules using the given model.
     *
     * @param model The model required by the rules.
     * @return Result of the query execution
     * @throws IllegalArgumentException If the {@code model} is {@code null}.
     */
    @Override
    public Void executeRules(SetValueServiceModel model) {
        if (model == null) throw new IllegalArgumentException("model must not be null.");

        logger.info("Setting the value");
        var session = getSession();

        // Add data
        session.insert(model);
        session.insert(model.getAnalysisSituation());

        // Execute rules
        session.fireAllRules();

        return null;
    }
}
