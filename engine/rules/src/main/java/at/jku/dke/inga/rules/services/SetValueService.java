package at.jku.dke.inga.rules.services;

import at.jku.dke.inga.rules.models.SetValueServiceModel;
import org.kie.api.runtime.KieSession;

/**
 * This service provides a method that determines the selected value.
 *
 * <p>Executes rules belonging to agenda-group: {@code set-value}</p>
 */
public class SetValueService extends DroolsService<SetValueServiceModel, Void>  {

    /**
     * Instantiates a new instance of class {@linkplain SetValueService}.
     */
    public SetValueService() {
        super(new String[]{"set-value"});
    }

    /**
     * Executes the rules using the given model.
     *
     * @param session The new kie session.
     * @param model   The model required by the rules. It is guaranteed, that the model is not {@code null}.
     * @return Result of the query execution
     */
    @Override
    protected Void execute(KieSession session, SetValueServiceModel model) {
        logger.info("Setting the value");

        // Add data
        session.insert(model);
        session.insert(model.getAnalysisSituation());
        session.insert(model.getValue());

        // Execute rules
        session.fireAllRules();

        return null;
    }

}
