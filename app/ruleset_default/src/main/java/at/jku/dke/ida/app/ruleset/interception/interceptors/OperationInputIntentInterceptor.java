package at.jku.dke.ida.app.ruleset.interception.interceptors;

import at.jku.dke.ida.app.ruleset.helpers.UserInput;
import at.jku.dke.ida.app.ruleset.interception.models.OperationInputIntentModel;
import at.jku.dke.ida.data.models.Similarity;
import at.jku.dke.ida.rules.interfaces.OperationIntentServiceModel;
import at.jku.dke.ida.scxml.interceptors.DetermineOperationInputIntentInterceptor;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.operations.Operation;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
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
        Collection<Operation> possibleOperations = new HashSet<>(operationIntentServiceModel.getPossibleOperations().values());
        Arrays.stream(UserInput.getExitKeywords(operationIntentServiceModel.getLocale())).forEach(k -> possibleOperations.add(new Operation(Event.EXIT, k, 1)));

        Set<Similarity<Displayable>> result = InterceptionHelper.computeOperationStringSimilarities(
                operationIntentServiceModel.getCurrentState(),
                operationIntentServiceModel.getSessionModel(),
                operationIntentServiceModel.getLocale(),
                possibleOperations
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
