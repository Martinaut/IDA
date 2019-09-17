package at.jku.dke.ida.app.ruleset.interception.interceptors;

import at.jku.dke.ida.app.ruleset.helpers.Constants;
import at.jku.dke.ida.app.ruleset.helpers.UserInput;
import at.jku.dke.ida.app.ruleset.interception.models.ValueModel;
import at.jku.dke.ida.data.models.similarity.Similarity;
import at.jku.dke.ida.nlp.drools.GraphDBSimilarityService;
import at.jku.dke.ida.nlp.drools.StringSimilarityService;
import at.jku.dke.ida.nlp.drools.WordGroupsService;
import at.jku.dke.ida.nlp.models.GraphDBSimilarityServiceModel;
import at.jku.dke.ida.nlp.models.StringSimilarityServiceModel;
import at.jku.dke.ida.data.models.WordGroup;
import at.jku.dke.ida.nlp.models.WordGroupsServiceModel;
import at.jku.dke.ida.rules.interfaces.ValueServiceModel;
import at.jku.dke.ida.scxml.interceptors.DetermineValueInterceptor;
import at.jku.dke.ida.shared.display.Display;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.display.ListDisplay;
import at.jku.dke.ida.shared.display.TwoListDisplay;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Intercepts the creation of the model when determining the value.
 */
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
        // Only number or empty input?
        if (valueServiceModel.getUserInput() == null || valueServiceModel.getUserInput().isBlank())
            return valueServiceModel;
        if (UserInput.isNumber(valueServiceModel.getUserInput()) || UserInput.isTwoNumberSelection(valueServiceModel.getUserInput()))
            return valueServiceModel;

        // Possible Values
        Collection<Displayable> possibleValues = getPossibleValues(valueServiceModel.getDisplayData());
        if (possibleValues.isEmpty())
            return valueServiceModel;

        // Word Groups
        Set<WordGroup> wordGroups = null;
        if (valueServiceModel.additionalDataContainsKey(Constants.ADD_WORD_GROUPS)) {
            wordGroups = (Set<WordGroup>) valueServiceModel.getAdditionalData(Constants.ADD_WORD_GROUPS, Set.class);
            valueServiceModel.removeAdditionalData(Constants.ADD_WORD_GROUPS);
        }
        if (wordGroups == null || wordGroups.isEmpty()) {
            wordGroups = new WordGroupsService()
                    .executeRules(new WordGroupsServiceModel(
                            valueServiceModel.getSessionModel(),
                            valueServiceModel.getUserInput()));
        }

        Set<Similarity<Displayable>> similarities = new HashSet<>();

        // GraphDB similarity
        if (valueServiceModel.getAnalysisSituation() instanceof NonComparativeAnalysisSituation) {
            new GraphDBSimilarityService().executeRules(new GraphDBSimilarityServiceModel(
                    valueServiceModel.getSessionModel(),
                    wordGroups,
                    ((NonComparativeAnalysisSituation) valueServiceModel.getAnalysisSituation()).getCube()
            )).stream()
                    .map(s -> {
                        Displayable elem = possibleValues.stream().filter(d -> d.getDisplayableId().equals(s.getElement())).findFirst().orElse(null);
                        if (elem == null)
                            return null;
                        return new Similarity<>(s.getTerm(), elem, s.getScore());
                    })
                    .filter(Objects::nonNull)
                    .forEach(similarities::add);
        }

        // String similarities for word groups
        StringSimilarityService service = new StringSimilarityService();
        for (WordGroup wg : wordGroups) {
            similarities.addAll(service.executeRules(new StringSimilarityServiceModel(
                    valueServiceModel.getCurrentState(),
                    valueServiceModel.getSessionModel(),
                    wg.getText(),
                    possibleValues
            )));
        }

        // String Similarities for full sentence
        if (valueServiceModel.getDisplayData() instanceof TwoListDisplay) {
            similarities.addAll(InterceptionHelper.computeValueStringSimilarities(
                    valueServiceModel.getCurrentState(),
                    valueServiceModel.getSessionModel(),
                    valueServiceModel.getLocale(),
                    ((TwoListDisplay) valueServiceModel.getDisplayData()).getDataLeft().stream().map(x -> (Displayable) x).collect(Collectors.toList()),
                    ((TwoListDisplay) valueServiceModel.getDisplayData()).getDataRight().stream().map(x -> (Displayable) x).collect(Collectors.toList())
            ));
        } else {
            similarities.addAll(InterceptionHelper.computeValueStringSimilarities(
                    valueServiceModel.getCurrentState(),
                    valueServiceModel.getSessionModel(),
                    valueServiceModel.getLocale(),
                    possibleValues
            ));
        }

        // Return
        return new ValueModel(
                valueServiceModel.getCurrentState(),
                valueServiceModel.getSessionModel(),
                similarities
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