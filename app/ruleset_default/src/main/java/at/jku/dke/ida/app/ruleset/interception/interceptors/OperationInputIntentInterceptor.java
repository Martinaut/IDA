package at.jku.dke.ida.app.ruleset.interception.interceptors;

import at.jku.dke.ida.app.ruleset.interception.models.OperationInputIntentModel;
import at.jku.dke.ida.data.models.Similarity;
import at.jku.dke.ida.rules.interfaces.OperationIntentServiceModel;
import at.jku.dke.ida.scxml.interceptors.DetermineOperationInputIntentInterceptor;
import at.jku.dke.ida.shared.display.Displayable;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Intercepts the creation of the model when determining the operation intent and adds possible
 * number based operations. After that the similarity will be calculated and added to the model.
 */
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
        Set<Similarity<Displayable>> result = InterceptionHelper.computeOperationStringSimilarities(
                operationIntentServiceModel.getCurrentState(),
                operationIntentServiceModel.getSessionModel(),
                operationIntentServiceModel.getLocale(),
                operationIntentServiceModel.getPossibleOperations().values()
        );

        // Return
        return new OperationInputIntentModel(
                operationIntentServiceModel.getCurrentState(),
                operationIntentServiceModel.getSessionModel(),
                operationIntentServiceModel.getPossibleOperations(),
                result
        );
    }

}
