package at.jku.dke.ida.app.ruleset.interception;

import at.jku.dke.ida.app.nlp.drools.StringSimilarityService;
import at.jku.dke.ida.app.nlp.models.StringSimilarityServiceModel;
import at.jku.dke.ida.data.models.Similarity;
import at.jku.dke.ida.rules.models.ValueServiceModel;
import at.jku.dke.ida.scxml.interceptors.DetermineValueInterceptor;
import at.jku.dke.ida.shared.EventNames;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.display.ListDisplay;
import at.jku.dke.ida.shared.operations.Operation;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ValueInterceptor implements DetermineValueInterceptor {
    /**
     * Use this method to modify the model before executing the rules.
     *
     * @param valueServiceModel The basic model.
     * @return The modified model (may also be a new instance or an instance of a subclass).
     */
    @Override
    public ValueServiceModel modifyModel(ValueServiceModel valueServiceModel) {
        if(!(valueServiceModel.getDisplayData() instanceof ListDisplay)) return valueServiceModel;
        // TODO: make also to work with other types

        Collection<Displayable> possibleValues = ((ListDisplay)valueServiceModel.getDisplayData()).getData().stream().map(x -> (Displayable) x).collect(Collectors.toSet());
        possibleValues.add(new Operation(EventNames.EXIT, valueServiceModel.getLocale(), 1));

        // Build Model
        StringSimilarityServiceModel model = new StringSimilarityServiceModel(
                valueServiceModel.getCurrentState(),
                valueServiceModel.getLocale(),
                valueServiceModel.getAnalysisSituation(),
                valueServiceModel.getOperation(),
                valueServiceModel.getAdditionalData(),
                valueServiceModel.getUserInput(),
                possibleValues
        );

        // Execute
        Set<Similarity<Displayable>> result = new StringSimilarityService().executeRules(model);

        // Return
        return new ValueModel(
                valueServiceModel.getCurrentState(),
                valueServiceModel.getLocale(),
                valueServiceModel.getAnalysisSituation(),
                valueServiceModel.getOperation(),
                valueServiceModel.getAdditionalData(),
                valueServiceModel.getUserInput(),
                valueServiceModel.getDisplayData(),
                result
        );
    }
}
