package at.jku.dke.inga.app.ruleset.interception;

import at.jku.dke.inga.app.nlp.drools.StringSimilarityService;
import at.jku.dke.inga.app.nlp.models.StringSimilarityServiceModel;
import at.jku.dke.inga.data.models.Similarity;
import at.jku.dke.inga.rules.models.OperationIntentServiceModel;
import at.jku.dke.inga.scxml.interceptors.DetermineOperationInputIntentInterceptor;
import at.jku.dke.inga.shared.display.Displayable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OperationInputIntentInterceptor implements DetermineOperationInputIntentInterceptor {
    /**
     * Use this method to modify the model before executing the rules.
     *
     * @param operationIntentServiceModel The basic model.
     * @return The modified model.
     */
    @Override
    public OperationIntentServiceModel modifyModel(OperationIntentServiceModel operationIntentServiceModel) {
        // Build Model
        StringSimilarityServiceModel model = new StringSimilarityServiceModel(
                operationIntentServiceModel.getCurrentState(),
                operationIntentServiceModel.getLocale(),
                operationIntentServiceModel.getAnalysisSituation(),
                operationIntentServiceModel.getOperation(),
                operationIntentServiceModel.getAdditionalData(),
                operationIntentServiceModel.getUserInput(),
                operationIntentServiceModel.getPossibleOperations().values().stream().map(x -> (Displayable) x).collect(Collectors.toSet())
        );

        // Execute
        Set<Similarity<Displayable>> result = new StringSimilarityService().executeRules(model);

        // Return
        return new OperationInputIntentModel(
                operationIntentServiceModel.getCurrentState(),
                operationIntentServiceModel.getLocale(),
                operationIntentServiceModel.getAnalysisSituation(),
                operationIntentServiceModel.getOperation(),
                operationIntentServiceModel.getAdditionalData(),
                operationIntentServiceModel.getUserInput(),
                operationIntentServiceModel.getPossibleOperations(),
                result
        );
    }
}
