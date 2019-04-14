package at.jku.dke.inga.app.ruleset.interception;

import at.jku.dke.inga.app.nlp.drools.StringSimilarityService;
import at.jku.dke.inga.app.nlp.models.StringSimilarityServiceModel;
import at.jku.dke.inga.data.models.Similarity;
import at.jku.dke.inga.rules.models.OperationServiceModel;
import at.jku.dke.inga.scxml.interceptors.DetermineOperationInterceptor;
import at.jku.dke.inga.shared.display.Displayable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OperationInterceptor implements DetermineOperationInterceptor {
    /**
     * Use this method to modify the model before executing the rules.
     *
     * @param operationServiceModel The basic model.
     * @return The modified model.
     */
    @Override
    public OperationServiceModel modifyModel(OperationServiceModel operationServiceModel) {
        // Build Model
        StringSimilarityServiceModel model = new StringSimilarityServiceModel(
                operationServiceModel.getCurrentState(),
                operationServiceModel.getLocale(),
                operationServiceModel.getAnalysisSituation(),
                operationServiceModel.getOperation(),
                operationServiceModel.getAdditionalData(),
                operationServiceModel.getUserInput(),
                operationServiceModel.getPossibleOperations().values().stream().map(x -> (Displayable) x).collect(Collectors.toSet())
        );

        // Execute
        Set<Similarity<Displayable>> result = new StringSimilarityService().executeRules(model);

        // Return
        return new OperationModel(
                operationServiceModel.getCurrentState(),
                operationServiceModel.getLocale(),
                operationServiceModel.getAnalysisSituation(),
                operationServiceModel.getOperation(),
                operationServiceModel.getAdditionalData(),
                operationServiceModel.getUserInput(),
                operationServiceModel.getPossibleOperations(),
                result
        );
    }
}
