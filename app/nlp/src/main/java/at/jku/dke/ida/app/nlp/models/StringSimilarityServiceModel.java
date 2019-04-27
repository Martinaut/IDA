package at.jku.dke.ida.app.nlp.models;

import at.jku.dke.ida.rules.models.DroolsServiceModel;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.models.AnalysisSituation;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Model used by {@link at.jku.dke.ida.app.nlp.drools.StringSimilarityService}.
 */
public class StringSimilarityServiceModel extends DroolsServiceModel {

    private final String input;
    private final Collection<Displayable> possibleValues;

    /**
     * Instantiates a new instance of class {@link StringSimilarityServiceModel}.
     *
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param additionalData    Additional data.
     * @param input             The user input.
     * @param possibleValues    The possible values the user can select from.
     */
    public StringSimilarityServiceModel(Locale locale, AnalysisSituation analysisSituation,
                                        Map<String, Object> additionalData, String input, Collection<Displayable> possibleValues) {
        super("NONE", locale, analysisSituation, "nlp", additionalData);
        this.input = input;
        this.possibleValues = Collections.unmodifiableCollection(possibleValues);
    }

    /**
     * Instantiates a new instance of class {@link StringSimilarityServiceModel}.
     *
     * @param currentState      The current state.
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param operation         The operation.
     * @param additionalData    Additional data.
     * @param input             The user input.
     * @param possibleValues    The possible values the user can select from.
     */
    public StringSimilarityServiceModel(String currentState, Locale locale, AnalysisSituation analysisSituation, String operation,
                                        Map<String, Object> additionalData, String input, Collection<Displayable> possibleValues) {
        super(currentState, locale, analysisSituation, operation, additionalData);
        this.input = input;
        this.possibleValues = Collections.unmodifiableCollection(possibleValues);
    }

    /**
     * Gets the input.
     *
     * @return the input
     */
    public String getInput() {
        return input;
    }

    /**
     * Gets the possible values.
     *
     * @return the possible values
     */
    public Collection<Displayable> getPossibleValues() {
        return possibleValues;
    }
}
