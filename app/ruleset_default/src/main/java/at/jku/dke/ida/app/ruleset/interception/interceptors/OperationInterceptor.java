package at.jku.dke.ida.app.ruleset.interception.interceptors;

import at.jku.dke.ida.app.nlp.drools.StringSimilarityService;
import at.jku.dke.ida.app.nlp.models.StringSimilarityServiceModel;
import at.jku.dke.ida.app.ruleset.interception.models.OperationModel;
import at.jku.dke.ida.data.models.Similarity;
import at.jku.dke.ida.rules.interfaces.OperationServiceModel;
import at.jku.dke.ida.scxml.interceptors.DetermineOperationInterceptor;
import at.jku.dke.ida.shared.ResourceBundleHelper;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.operations.Operation;
import com.ibm.icu.text.RuleBasedNumberFormat;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Intercepts the creation of the model when determining the operation and adds possible
 * number based operations. After that the similarity will be calculated and added to the model.
 */
@Service
public class OperationInterceptor implements DetermineOperationInterceptor {
    /**
     * Use this method to modify the model before executing the rules.
     *
     * @param operationServiceModel The basic model.
     * @return The modified model.
     */
    @Override
    @SuppressWarnings("Duplicates")
    public OperationServiceModel modifyModel(OperationServiceModel operationServiceModel) {
        Collection<Displayable> possibleValues = operationServiceModel.getPossibleOperations()
                .values().stream()
                .map(x -> (Displayable) x)
                .collect(Collectors.toSet());

        // Add numbered operations
        final String optionText = ResourceBundleHelper.getResourceString("ruleset.Translation", operationServiceModel.getLocale(), "Option");
        int i = 1;
        RuleBasedNumberFormat nf = new RuleBasedNumberFormat(operationServiceModel.getLocale(), RuleBasedNumberFormat.SPELLOUT);
        for (Operation op : operationServiceModel.getPossibleOperations().values()) {
            possibleValues.add(new Operation(op.getEvent(), optionText + ' ' + nf.format(i++), op.getPosition()));
        }

        i = 1;
        for (Operation op : operationServiceModel.getPossibleOperations().values()) {
            possibleValues.add(new Operation(op.getEvent(), nf.format(i++, "%spellout-ordinal") + ' ' + optionText, op.getPosition()));
        }

        // Build Model
        StringSimilarityServiceModel model = new StringSimilarityServiceModel(
                operationServiceModel.getCurrentState(),
                operationServiceModel.getSessionModel(),
                possibleValues
        );

        // Execute
        Set<Similarity<Displayable>> result = new StringSimilarityService().executeRules(model);

        // Return
        return new OperationModel(
                operationServiceModel.getCurrentState(),
                operationServiceModel.getSessionModel(),
                operationServiceModel.getPossibleOperations(),
                result
        );
    }
}
