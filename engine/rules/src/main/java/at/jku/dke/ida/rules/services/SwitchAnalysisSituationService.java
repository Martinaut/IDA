package at.jku.dke.ida.rules.services;

import at.jku.dke.ida.rules.interfaces.SwitchAnalysisSituationServiceModel;
import at.jku.dke.ida.rules.results.AnalysisSituationConfidenceResult;
import org.kie.api.runtime.KieSession;

import java.util.Collection;

/**
 * This service provides a method that sets the current analysis situation.
 *
 * <p>Executes rules belonging to agenda-group: {@code switch-as}</p>
 */
public class SwitchAnalysisSituationService extends DroolsService<SwitchAnalysisSituationServiceModel, AnalysisSituationConfidenceResult> {

    /**
     * Instantiates a new instance of class {@linkplain SwitchAnalysisSituationService}.
     */
    public SwitchAnalysisSituationService() {
        super(new String[]{"switch-as"});
    }

    /**
     * Executes the rules using the given model.
     * <p>
     * Inserts the model, the value and the analysis situation into the Kie session.
     *
     * @param session The new kie session.
     * @param model   The model required by the rules. It is guaranteed, that the model is not {@code null}.
     * @return Result of the query execution
     */
    @Override
    protected AnalysisSituationConfidenceResult execute(KieSession session, SwitchAnalysisSituationServiceModel model) {
        logger.info("Set values based on pattern and switch analysis situation.");

        // Add data
        session.insert(model);
        session.insert(model.getComparativeAnalysisSituation());

        // Execute rules
        session.fireAllRules();

        // Get results and close session
        Collection<?> result = session.getObjects(obj -> obj instanceof AnalysisSituationConfidenceResult);
        closeSession();

        // Sort and return
        return result.stream()
                .map(x -> (AnalysisSituationConfidenceResult) x)
                .sorted()
                .findFirst().orElse(null);
    }

}