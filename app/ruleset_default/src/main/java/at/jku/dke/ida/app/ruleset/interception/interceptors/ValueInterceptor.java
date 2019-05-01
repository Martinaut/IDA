package at.jku.dke.ida.app.ruleset.interception.interceptors;

import at.jku.dke.ida.app.nlp.drools.GraphDBSimilarityService;
import at.jku.dke.ida.app.nlp.drools.StringSimilarityService;
import at.jku.dke.ida.app.nlp.drools.WordGroupsService;
import at.jku.dke.ida.app.nlp.models.GraphDBSimilarityServiceModel;
import at.jku.dke.ida.app.nlp.models.StringSimilarityServiceModel;
import at.jku.dke.ida.app.nlp.models.WordGroup;
import at.jku.dke.ida.app.nlp.models.WordGroupsServiceModel;
import at.jku.dke.ida.app.ruleset.helpers.Constants;
import at.jku.dke.ida.app.ruleset.helpers.UserInput;
import at.jku.dke.ida.app.ruleset.interception.models.ValueModel;
import at.jku.dke.ida.data.models.CubeSimilarity;
import at.jku.dke.ida.data.models.Similarity;
import at.jku.dke.ida.rules.interfaces.ValueServiceModel;
import at.jku.dke.ida.scxml.interceptors.DetermineValueInterceptor;
import at.jku.dke.ida.shared.display.Display;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.display.ListDisplay;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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
    @SuppressWarnings("Duplicates")
    public ValueServiceModel modifyModel(ValueServiceModel valueServiceModel) {
        // Only number?
        if (UserInput.isNumber(valueServiceModel.getUserInput()) || UserInput.isTwoNumberSelection(valueServiceModel.getUserInput()))
            return valueServiceModel;
        if (!(valueServiceModel.getDisplayData() instanceof ListDisplay)) return valueServiceModel;
        // TODO: make also to work with other types (twolistdisplay)

        // Possible Values
        Collection<Displayable> possibleValues = ((ListDisplay) valueServiceModel.getDisplayData())
                .getData().stream()
                .map(x -> (Displayable) x)
                .collect(Collectors.toSet());

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
        InterceptionHelper.computeValueStringSimilarities(
                valueServiceModel.getCurrentState(),
                valueServiceModel.getSessionModel(),
                valueServiceModel.getLocale(),
                possibleValues
        );

        // Return
        return new ValueModel(
                valueServiceModel.getCurrentState(),
                valueServiceModel.getSessionModel(),
                similarities
        );
    }
}
