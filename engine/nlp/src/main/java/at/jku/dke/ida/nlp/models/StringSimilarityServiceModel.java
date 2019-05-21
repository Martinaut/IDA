package at.jku.dke.ida.nlp.models;

import at.jku.dke.ida.rules.models.AbstractServiceModel;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;
import at.jku.dke.ida.nlp.drools.StringSimilarityService;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

/**
 * Model used by {@link StringSimilarityService}.
 */
public class StringSimilarityServiceModel extends AbstractServiceModel {

    private final String input;
    private final Collection<Displayable> possibleValues;

    /**
     * Instantiates a new instance of class {@link StringSimilarityServiceModel}.
     * <p>
     * Uses the user input in the session model.
     *
     * @param currentState   The current state of the state machine.
     * @param sessionModel   The session model.
     * @param possibleValues The possible values the user can select from.
     * @throws IllegalArgumentException If the any of the parameters is {@code null} or empty.
     */
    public StringSimilarityServiceModel(String currentState, SessionModel sessionModel, Collection<Displayable> possibleValues) {
        super(currentState, sessionModel);
        this.input = sessionModel.getUserInput();
        this.possibleValues = Collections.unmodifiableCollection(possibleValues);
    }

    /**
     * Instantiates a new instance of class {@link StringSimilarityServiceModel}.
     *
     * @param currentState   The current state of the state machine.
     * @param sessionModel   The session model.
     * @param term           The term to compare with.
     * @param possibleValues The possible values the user can select from.
     * @throws IllegalArgumentException If the any of the parameters is {@code null} or empty.
     */
    public StringSimilarityServiceModel(String currentState, SessionModel sessionModel, String term, Collection<Displayable> possibleValues) {
        super(currentState, sessionModel);
        this.input = term;
        this.possibleValues = Collections.unmodifiableCollection(possibleValues);
    }

    /**
     * Instantiates a new instance of class {@link StringSimilarityServiceModel}.
     * <p>
     * Uses the user input in the session model.
     *
     * @param currentState      The current state of the state machine.
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param operation         The operation the user wants to perform.
     * @param sessionModel      The session model.
     * @param possibleValues    The possible values the user can select from.
     * @throws IllegalArgumentException If the any of the parameters (except {@code locale} and {@code operation}) is {@code null} or empty.
     */
    public StringSimilarityServiceModel(String currentState, Locale locale, EngineAnalysisSituation analysisSituation,
                                        Event operation, SessionModel sessionModel, Collection<Displayable> possibleValues) {
        super(currentState, locale, analysisSituation, operation, sessionModel);
        this.input = sessionModel.getUserInput();
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
