package at.jku.dke.ida.csp.service;

import at.jku.dke.ida.csp.models.FillDeterminedValuesServiceModel;
import at.jku.dke.ida.rules.services.DroolsService;
import org.kie.api.runtime.KieSession;

/**
 * This service fills the analysis situation with the given values determined by constraint satisfaction.
 *
 * <p>Executes rules belonging to agenda-group: {@code fill-csp}</p>
 */
public class FillDeterminedValuesService extends DroolsService<FillDeterminedValuesServiceModel, Void> {

    /**
     * Instantiates a new instance of class {@linkplain FillDeterminedValuesService}.
     */
    public FillDeterminedValuesService() {
        super(new String[]{"fill-csp"});
    }

    /**
     * Executes the rules using the given model.
     *
     * @param session The new kie session.
     * @param model   The model required by the rules. It is guaranteed, that the model is not {@code null}.
     * @return Result of the query execution
     */
    @Override
    protected Void execute(KieSession session, FillDeterminedValuesServiceModel model) {
        logger.info("Filling AS with values from CSP.");
        if (model.getDeterminedValues() == null) return null;


        // Add data
        session.insert(model);
        session.insert(model.getAnalysisSituation());
        session.insert(model.getDeterminedValues());

        // Execute rules
        session.fireAllRules();

        // Get results
        return null;
    }
}
