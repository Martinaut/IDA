package at.jku.dke.ida.app.ruleset.interception;

import at.jku.dke.ida.app.nlp.drools.StringSimilarityService;
import at.jku.dke.ida.app.nlp.models.StringSimilarityServiceModel;
import at.jku.dke.ida.data.models.Similarity;
import at.jku.dke.ida.rules.models.OperationIntentServiceModel;
import at.jku.dke.ida.scxml.interceptors.DetermineOperationInputIntentInterceptor;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.operations.Operation;
import com.ibm.icu.text.RuleBasedNumberFormat;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
        Collection<Displayable> possibleValues = operationIntentServiceModel.getPossibleOperations().values().stream().map(x -> (Displayable) x).collect(Collectors.toSet());

        int i = 1;
        RuleBasedNumberFormat nf = new RuleBasedNumberFormat(operationIntentServiceModel.getLocale(), RuleBasedNumberFormat.SPELLOUT);
        for (Operation op : operationIntentServiceModel.getPossibleOperations().values()) {
            possibleValues.add(new Operation(op.getEventName(), "Option " + nf.format(i++), op.getPosition())); // TODO: i18n
        }

        i = 1;
        for (Operation op : operationIntentServiceModel.getPossibleOperations().values()) {
            possibleValues.add(new Operation(op.getEventName(), nf.format(i++, "%spellout-ordinal") + " Option", op.getPosition())); // TODO: i18n
        }

        // Build Model
        StringSimilarityServiceModel model = new StringSimilarityServiceModel(
                operationIntentServiceModel.getCurrentState(),
                operationIntentServiceModel.getLocale(),
                operationIntentServiceModel.getAnalysisSituation(),
                operationIntentServiceModel.getOperation(),
                operationIntentServiceModel.getAdditionalData(),
                operationIntentServiceModel.getUserInput(),
                possibleValues
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
