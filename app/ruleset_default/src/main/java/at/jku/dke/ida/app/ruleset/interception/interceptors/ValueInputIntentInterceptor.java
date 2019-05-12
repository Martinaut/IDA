package at.jku.dke.ida.app.ruleset.interception.interceptors;

import at.jku.dke.ida.app.ruleset.helpers.UserInput;
import at.jku.dke.ida.app.ruleset.interception.models.ValueInputIntentModel;
import at.jku.dke.ida.data.models.Similarity;
import at.jku.dke.ida.rules.interfaces.ValueIntentServiceModel;
import at.jku.dke.ida.rules.results.EventConfidenceResult;
import at.jku.dke.ida.scxml.interceptors.DetermineValueInputIntentInterceptor;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.display.Display;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.display.ListDisplay;
import at.jku.dke.ida.shared.display.TwoListDisplay;
import at.jku.dke.ida.shared.operations.Operation;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Intercepts the creation of the model when determining the value intent and adds the exit operation.
 * After that the similarity will be calculated and added to the model.
 */
@Service
public class ValueInputIntentInterceptor implements DetermineValueInputIntentInterceptor {

    /**
     * Use this method to modify the model before executing the rules.
     *
     * @param valueIntentServiceModel The basic model.
     * @return The modified model (may also be a new instance or an instance of a subclass).
     */
    @Override
    public ValueIntentServiceModel modifyModel(ValueIntentServiceModel valueIntentServiceModel) {
        // Only number or empty input?
        if (valueIntentServiceModel.getUserInput() == null || valueIntentServiceModel.getUserInput().isBlank())
            return valueIntentServiceModel;
        if (UserInput.isNumber(valueIntentServiceModel.getUserInput()) || UserInput.isTwoNumberSelection(valueIntentServiceModel.getUserInput()))
            return valueIntentServiceModel;

        Collection<Displayable> possibleValues = getPossibleValues(valueIntentServiceModel.getDisplayData());

        // Execute value string similarity
        Set<Similarity<Displayable>> result = InterceptionHelper.computeValueStringSimilarities(
                valueIntentServiceModel.getCurrentState(),
                valueIntentServiceModel.getSessionModel(),
                valueIntentServiceModel.getLocale(),
                possibleValues
        );

        // execute operation string similarity
        Set<Operation> possibleOperations = new HashSet<>();
        possibleOperations.add(new Operation(Event.ABORT, valueIntentServiceModel.getLocale(), 1));
        possibleOperations.add(new Operation(Event.EXIT, valueIntentServiceModel.getLocale(), 1));
        Arrays.stream(Event.ABORT.getSynonyms(valueIntentServiceModel.getLocale())).forEach(k -> possibleOperations.add(new Operation(Event.ABORT, k, 1)));
        Arrays.stream(Event.EXIT.getSynonyms(valueIntentServiceModel.getLocale())).forEach(k -> possibleOperations.add(new Operation(Event.EXIT, k, 1)));
        result.addAll(InterceptionHelper.computeOperationStringSimilarities(
                valueIntentServiceModel.getCurrentState(),
                valueIntentServiceModel.getSessionModel(),
                valueIntentServiceModel.getLocale(),
                possibleOperations,
                false
        ));

        // Return
        return new ValueInputIntentModel(
                valueIntentServiceModel.getCurrentState(),
                valueIntentServiceModel.getSessionModel(),
                result
        );
    }


    private Collection<Displayable> getPossibleValues(Display display) {
        ArrayList<Displayable> possibleValues = new ArrayList<>();
        if (display instanceof ListDisplay) {
            ((ListDisplay) display)
                    .getData().stream()
                    .map(x -> (Displayable) x)
                    .forEachOrdered((possibleValues)::add);
        }
        if (display instanceof TwoListDisplay) {
            ((TwoListDisplay) display)
                    .getDataLeft().stream()
                    .map(x -> (Displayable) x)
                    .forEachOrdered((possibleValues)::add);
            ((TwoListDisplay) display)
                    .getDataRight().stream()
                    .map(x -> (Displayable) x)
                    .forEachOrdered((possibleValues)::add);
        }
        return possibleValues;
    }
}