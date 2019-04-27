package at.jku.dke.ida.app.ruleset.interception;

import at.jku.dke.ida.app.nlp.drools.StringSimilarityService;
import at.jku.dke.ida.app.nlp.models.StringSimilarityServiceModel;
import at.jku.dke.ida.data.models.Similarity;
import at.jku.dke.ida.rules.models.OperationServiceModel;
import at.jku.dke.ida.scxml.interceptors.DetermineOperationInterceptor;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.operations.Operation;
import com.ibm.icu.text.RuleBasedNumberFormat;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
        Collection<Displayable> possibleValues = operationServiceModel.getPossibleOperations().values().stream().map(x -> (Displayable) x).collect(Collectors.toSet());

        int i = 1;
        RuleBasedNumberFormat nf = new RuleBasedNumberFormat(operationServiceModel.getLocale(), RuleBasedNumberFormat.SPELLOUT);
        for (Operation op : operationServiceModel.getPossibleOperations().values()) {
            possibleValues.add(new Operation(op.getEventName(), "Option " + nf.format(i++), op.getPosition())); // TODO: i18n
        }

        i = 1;
        for (Operation op : operationServiceModel.getPossibleOperations().values()) {
            possibleValues.add(new Operation(op.getEventName(), nf.format(i++, "%spellout-ordinal") + " Option", op.getPosition())); // TODO: i18n
        }

        // Build Model
        StringSimilarityServiceModel model = new StringSimilarityServiceModel(
                operationServiceModel.getCurrentState(),
                operationServiceModel.getLocale(),
                operationServiceModel.getAnalysisSituation(),
                operationServiceModel.getOperation(),
                operationServiceModel.getAdditionalData(),
                operationServiceModel.getUserInput(),
                possibleValues
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
