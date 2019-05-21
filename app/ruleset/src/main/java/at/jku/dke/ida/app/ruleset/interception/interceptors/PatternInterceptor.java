package at.jku.dke.ida.app.ruleset.interception.interceptors;

import at.jku.dke.ida.app.ruleset.helpers.UserInput;
import at.jku.dke.ida.app.ruleset.interception.models.PatternModel;
import at.jku.dke.ida.data.models.Similarity;
import at.jku.dke.ida.rules.interfaces.PatternServiceModel;
import at.jku.dke.ida.scxml.interceptors.DeterminePatternSelectionInterceptor;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.display.ListDisplay;
import at.jku.dke.ida.shared.operations.Operation;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Intercepts the creation of the model when determining the pattern.
 */
@Service
public class PatternInterceptor implements DeterminePatternSelectionInterceptor {

    /**
     * Use this method to modify the model before executing the rules.
     * <p>
     * The default implementation just returns the model.
     *
     * @param patternServiceModel The basic model.
     * @return The modified model (may also be a new instance or an instance of a subclass).
     */
    @Override
    public PatternServiceModel modifyModel(PatternServiceModel patternServiceModel) {
        // Only number?
        if (UserInput.isNumber(patternServiceModel.getUserInput()) || UserInput.isTwoNumberSelection(patternServiceModel.getUserInput()))
            return patternServiceModel;
        if (!(patternServiceModel.getDisplayData() instanceof ListDisplay)) return patternServiceModel;
        if (patternServiceModel.getUserInput() == null || patternServiceModel.getUserInput().isBlank())
            return patternServiceModel;

        // Possible Values
        Collection<Displayable> possibleValues = new HashSet<>(((ListDisplay) patternServiceModel.getDisplayData()).getData());
        possibleValues.add(new Operation(Event.EXIT, patternServiceModel.getLocale(), 1));
        Arrays.stream(Event.EXIT.getSynonyms(patternServiceModel.getLocale())).forEach(k -> possibleValues.add(new Operation(Event.EXIT, k, 1)));

        // String Similarities for full sentence
        Set<Similarity<Displayable>> similarities = InterceptionHelper.computeValueStringSimilarities(
                patternServiceModel.getCurrentState(),
                patternServiceModel.getSessionModel(),
                patternServiceModel.getLocale(),
                possibleValues
        );

        // Return
        return new PatternModel(
                patternServiceModel.getCurrentState(),
                patternServiceModel.getSessionModel(),
                similarities
        );
    }

}
