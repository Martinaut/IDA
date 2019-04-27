package at.jku.dke.ida.app.ruleset.interception.interceptors;

import at.jku.dke.ida.app.nlp.drools.StringSimilarityService;
import at.jku.dke.ida.app.nlp.models.StringSimilarityServiceModel;
import at.jku.dke.ida.app.ruleset.interception.models.OperationInputIntentModel;
import at.jku.dke.ida.data.models.Similarity;
import at.jku.dke.ida.rules.interfaces.OperationIntentServiceModel;
import at.jku.dke.ida.scxml.interceptors.DetermineOperationInputIntentInterceptor;
import at.jku.dke.ida.shared.ResourceBundleHelper;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.operations.Operation;
import com.ibm.icu.text.RuleBasedNumberFormat;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

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
    @SuppressWarnings("Duplicates")
    public OperationIntentServiceModel modifyModel(OperationIntentServiceModel operationIntentServiceModel) {
        Collection<Displayable> possibleValues = operationIntentServiceModel.getPossibleOperations()
                .values().stream()
                .map(x -> (Displayable) x)
                .collect(Collectors.toSet());

        // Add numbered operations
        final String optionText = ResourceBundleHelper.getResourceString("ruleset.Translation", operationIntentServiceModel.getLocale(), "Option");
        int i = 1;
        RuleBasedNumberFormat nf = new RuleBasedNumberFormat(operationIntentServiceModel.getLocale(), RuleBasedNumberFormat.SPELLOUT);
        for (Operation op : operationIntentServiceModel.getPossibleOperations().values()) {
            possibleValues.add(new Operation(op.getEvent(), optionText + ' ' + nf.format(i++), op.getPosition()));
        }

        i = 1;
        for (Operation op : operationIntentServiceModel.getPossibleOperations().values()) {
            possibleValues.add(new Operation(op.getEvent(), nf.format(i++, "%spellout-ordinal") + ' ' + optionText, op.getPosition()));
        }

        // Build Model
        StringSimilarityServiceModel model = new StringSimilarityServiceModel(
                operationIntentServiceModel.getCurrentState(),
                operationIntentServiceModel.getSessionModel(),
                possibleValues
        );

        // Execute
        Set<Similarity<Displayable>> result = new StringSimilarityService().executeRules(model);

        // Return
        return new OperationInputIntentModel(
                operationIntentServiceModel.getCurrentState(),
                operationIntentServiceModel.getSessionModel(),
                operationIntentServiceModel.getPossibleOperations(),
                result
        );
    }

}
