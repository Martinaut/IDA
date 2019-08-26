package at.jku.dke.ida.rules.models;

import at.jku.dke.ida.rules.interfaces.PatternServiceModel;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.display.Display;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;

import java.util.Locale;

/**
 * Default model used by {@link at.jku.dke.ida.rules.services.PatternService}.
 */
public class DefaultPatternServiceModel extends AbstractServiceModel implements PatternServiceModel {

    private final String userInput;

    /**
     * Instantiates a new instance of class {@link DefaultPatternServiceModel}.
     *
     * @param currentState The current state of the state machine.
     * @param sessionModel The session model.
     * @throws IllegalArgumentException If the any of the parameters is {@code null} or empty.
     */
    public DefaultPatternServiceModel(String currentState, SessionModel sessionModel) {
        super(currentState, sessionModel);
        this.userInput = sessionModel.getUserInput();
    }

    /**
     * Instantiates a new instance of class {@link DefaultPatternServiceModel}.
     *
     * @param currentState      The current state of the state machine.
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param operation         The operation the user wants to perform.
     * @param sessionModel      The session model.
     * @param userInput         The user input.
     * @throws IllegalArgumentException If the any of the parameters (except {@code locale} and {@code operation}) is {@code null} or empty.
     */
    public DefaultPatternServiceModel(String currentState, Locale locale, EngineAnalysisSituation analysisSituation,
                                      Event operation, SessionModel sessionModel, String userInput) {
        super(currentState, locale, analysisSituation, operation, sessionModel);

        if (userInput == null)
            throw new IllegalArgumentException("userInput must not be null");

        this.userInput = userInput;
    }

    @Override
    public String getUserInput() {
        return userInput;
    }
}
