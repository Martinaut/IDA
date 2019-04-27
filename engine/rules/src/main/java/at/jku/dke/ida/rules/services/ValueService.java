package at.jku.dke.ida.rules.services;

import at.jku.dke.ida.rules.interfaces.ValueServiceModel;
import at.jku.dke.ida.rules.results.ConfidenceResult;
import org.kie.api.runtime.KieSession;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This service provides a method that determines the selected value.
 *
 * <p>Executes rules belonging to agenda-group: {@code value-determination}</p>
 */
public class ValueService extends DroolsService<ValueServiceModel, Collection<ConfidenceResult>>  {

    /**
     * Instantiates a new instance of class {@linkplain ValueService}.
     */
    public ValueService() {
        super(new String[]{"value-determination"});
    }

    /**
     * Executes the rules using the given model.
     * <p>
     * Inserts the model, the display data and the analysis situation into the Kie session.
     *
     * @param session The new kie session.
     * @param model   The model required by the rules. It is guaranteed, that the model is not {@code null}.
     * @return Result of the query execution
     */
    @Override
    protected Collection<ConfidenceResult> execute(KieSession session, ValueServiceModel model) {
        logger.info("Determining the value");

        // Add data
        session.insert(model);
        session.insert(model.getAnalysisSituation());
        session.insert(model.getDisplayData());

        // Execute rules
        session.fireAllRules();

        // Get results and close session
        Collection<?> result = session.getObjects(obj -> obj instanceof ConfidenceResult);
        closeSession();

        // Sort and return
        return result.stream()
                .map(x -> (ConfidenceResult) x)
                .sorted()
                .collect(Collectors.toList());
    }

}
