package at.jku.dke.ida.app.ruleset.interception.interceptors;

import at.jku.dke.ida.app.ruleset.helpers.Constants;
import at.jku.dke.ida.app.ruleset.helpers.UserInput;
import at.jku.dke.ida.app.ruleset.interception.models.OperationModel;
import at.jku.dke.ida.data.models.similarity.Similarity;
import at.jku.dke.ida.nlp.drools.StringSimilarityService;
import at.jku.dke.ida.nlp.drools.WordGroupsService;
import at.jku.dke.ida.nlp.models.StringSimilarityServiceModel;
import at.jku.dke.ida.data.models.WordGroup;
import at.jku.dke.ida.nlp.models.WordGroupsServiceModel;
import at.jku.dke.ida.rules.interfaces.OperationServiceModel;
import at.jku.dke.ida.rules.results.EventConfidenceResult;
import at.jku.dke.ida.scxml.interceptors.DetermineOperationInterceptor;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.operations.Operation;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Intercepts the creation of the model when determining the operation and adds possible
 * number based operations. After that the similarity will be calculated and added to the model.
 */
@Service
public class OperationInterceptor implements DetermineOperationInterceptor {
    @Override
    public OperationServiceModel modifyModel(OperationServiceModel operationServiceModel) {
        // Only number?
        if (UserInput.isNumber(operationServiceModel.getUserInput()) || UserInput.isTwoNumberSelection(operationServiceModel.getUserInput()))
            return operationServiceModel;
        if (operationServiceModel.getUserInput() == null || operationServiceModel.getUserInput().isBlank())
            return operationServiceModel;

        Collection<Operation> possibleOperations = new HashSet<>(operationServiceModel.getPossibleOperations().values());
        possibleOperations.addAll(possibleOperations.stream()
                .flatMap(o -> Arrays.stream(o.getEvent().getSynonyms(operationServiceModel.getLocale()))
                        .map(x -> new Operation(o.getEvent(), x, 1))
                )
                .collect(Collectors.toList()));

        Set<WordGroup> wordGroups = new WordGroupsService()
                .executeRules(new WordGroupsServiceModel(
                        operationServiceModel.getSessionModel(),
                        operationServiceModel.getUserInput()));
        operationServiceModel.addAdditionalData(Constants.ADD_WORD_GROUPS, wordGroups);

        Set<Similarity<Displayable>> similarities = new HashSet<>();

        // String similarities for word groups
        StringSimilarityService service = new StringSimilarityService();
        for (WordGroup wg : wordGroups) {
            similarities.addAll(service.executeRules(new StringSimilarityServiceModel(
                    operationServiceModel.getCurrentState(),
                    operationServiceModel.getSessionModel(),
                    wg.getText(),
                    possibleOperations.stream().map(x -> (Displayable) x).collect(Collectors.toList())
            )));
        }

        // String Similarities for full sentence
        similarities.addAll(InterceptionHelper.computeOperationStringSimilarities(
                operationServiceModel.getCurrentState(),
                operationServiceModel.getSessionModel(),
                operationServiceModel.getLocale(),
                possibleOperations
        ));

        // Return
        return new OperationModel(
                operationServiceModel.getCurrentState(),
                operationServiceModel.getSessionModel(),
                operationServiceModel.getPossibleOperations(),
                similarities
        );
    }

    /**
     * Use this method to modify the result before using it.
     *
     * @param operationServiceModel The basic model.
     * @param result                The basic result.
     * @return The modified result (may also be a new instance or an instance of a subclass).
     */
    @Override
    public Event modifyResult(OperationServiceModel operationServiceModel, Collection<EventConfidenceResult> result) {
        EventConfidenceResult confidenceResult = result.stream()
                .sorted()
                .findFirst().orElse(new EventConfidenceResult(Event.INVALID_INPUT));
        if (confidenceResult.getTerm() != null && operationServiceModel.additionalDataContainsKey(Constants.ADD_WORD_GROUPS)) {
            Set<WordGroup> similarities = (Set<WordGroup>) operationServiceModel.getAdditionalData(Constants.ADD_WORD_GROUPS, Set.class);
            if (similarities != null) {
                similarities.removeIf(ds -> ds.getText().equals(confidenceResult.getTerm()));
                operationServiceModel.getSessionModel().setUserInput(operationServiceModel.getUserInput().replace(confidenceResult.getTerm(), ""));
            }
        }

        return DetermineOperationInterceptor.super.modifyResult(operationServiceModel, result);
    }
}
