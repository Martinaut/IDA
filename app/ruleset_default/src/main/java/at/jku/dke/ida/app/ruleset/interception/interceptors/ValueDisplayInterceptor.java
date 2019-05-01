package at.jku.dke.ida.app.ruleset.interception.interceptors;

import at.jku.dke.ida.app.nlp.models.WordGroup;
import at.jku.dke.ida.app.ruleset.helpers.Constants;
import at.jku.dke.ida.app.ruleset.helpers.UserInput;
import at.jku.dke.ida.rules.interfaces.ValueDisplayServiceModel;
import at.jku.dke.ida.rules.models.DefaultValueDisplayServiceModel;
import at.jku.dke.ida.scxml.interceptors.DisplayValuesInterceptor;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Intercepts the creation of the model when determining the values to display.
 */
@Service
public class ValueDisplayInterceptor implements DisplayValuesInterceptor {
    @Override
    public ValueDisplayServiceModel modifyModel(ValueDisplayServiceModel valueDisplayServiceModel) {
        // Check if number input
        if (valueDisplayServiceModel.getSessionModel().getUserInput() == null || UserInput.isNumber(valueDisplayServiceModel.getSessionModel().getUserInput()) || UserInput.isTwoNumberSelection(valueDisplayServiceModel.getSessionModel().getUserInput()))
            return valueDisplayServiceModel;

        // Check if word groups are still available
        if (!valueDisplayServiceModel.additionalDataContainsKey(Constants.ADD_WORD_GROUPS))
            return valueDisplayServiceModel;

        Set<WordGroup> wordGroups = (Set<WordGroup>) valueDisplayServiceModel.getAdditionalData(Constants.ADD_WORD_GROUPS, Set.class);
        if (wordGroups == null || wordGroups.isEmpty()) {
            valueDisplayServiceModel.removeAdditionalData(Constants.ADD_WORD_GROUPS);
            return valueDisplayServiceModel;
        }

        // Set skip to true
        if (valueDisplayServiceModel instanceof DefaultValueDisplayServiceModel) {
            ((DefaultValueDisplayServiceModel) valueDisplayServiceModel).setSkipDisplay(true);
        }

        return valueDisplayServiceModel;
    }
}
