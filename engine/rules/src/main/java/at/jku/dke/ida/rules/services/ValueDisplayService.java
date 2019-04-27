package at.jku.dke.ida.rules.services;

import at.jku.dke.ida.rules.interfaces.ValueDisplayServiceModel;
import at.jku.dke.ida.shared.display.Display;
import org.kie.api.runtime.KieSession;

import java.util.Collection;

/**
 * This service provides a method that returns the values from which a user can select one,
 * based on the current analysis situation and operation.
 *
 * <p>Executes rules belonging to agenda-group: {@code value-display-determination}</p>
 */
public class ValueDisplayService extends DroolsService<ValueDisplayServiceModel, Display> {

    /**
     * Instantiates a new instance of class {@linkplain ValueDisplayService}.
     */
    public ValueDisplayService() {
        super(new String[]{"value-display-determination"});
    }

    /**
     * Executes the rules using the given model.
     * <p>
     * Inserts the model and the analysis situation into the Kie session.
     *
     * @param session The new kie session.
     * @param model   The model required by the rules. It is guaranteed, that the model is not {@code null}.
     * @return Result of the query execution
     */
    @Override
    protected Display execute(KieSession session, ValueDisplayServiceModel model) {
        logger.info("Determining the values to display");

        // Add data
        session.insert(model);
        session.insert(model.getAnalysisSituation());

        // Execute rules
        session.fireAllRules();

        // Get results and close session
        Collection<?> result = session.getObjects(obj -> obj instanceof Display);
        closeSession();

        if (result.size() > 1)
            logger.warn("More than one display found in rules facts list. There should only be one. Only the first one will be returned by this service.");

        // Sort and return
        return result.stream()
                .map(x -> (Display) x)
                .findFirst().orElse(null);
    }

}
