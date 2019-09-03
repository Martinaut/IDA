package at.jku.dke.ida.rules.models;

import at.jku.dke.ida.rules.interfaces.ValueServiceModel;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.display.Display;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;

import java.util.Locale;

/**
 * Default model used by {@link at.jku.dke.ida.rules.services.ValueService}.
 */
public class DefaultValueServiceModel extends AbstractServiceModel implements ValueServiceModel {

    private final String userInput;
    private final Display displayData;

    /**
     * Instantiates a new instance of class {@link DefaultValueServiceModel}.
     *
     * @param currentState The current state of the state machine.
     * @param sessionModel The session model.
     * @throws IllegalArgumentException If the any of the parameters is {@code null} or empty.
     */
    public DefaultValueServiceModel(String currentState, SessionModel sessionModel) {
        super(currentState, sessionModel);
        this.userInput = sessionModel.getUserInput();
        this.displayData = sessionModel.getDisplayData();
    }

    /**
     * Instantiates a new instance of class {@link DefaultValueServiceModel}.
     *
     * @param currentState      The current state of the state machine.
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param operation         The operation the user wants to perform.
     * @param sessionModel      The session model.
     * @param userInput         The user input.
     * @param displayData       The data displayed to the user.
     * @throws IllegalArgumentException If the any of the parameters (except {@code locale} and {@code operation}) is {@code null} or empty.
     */
    public DefaultValueServiceModel(String currentState, Locale locale, EngineAnalysisSituation analysisSituation, Event operation, SessionModel sessionModel,
                                    String userInput, Display displayData) {
        super(currentState, locale, analysisSituation, operation, sessionModel);

        if (displayData == null)
            throw new IllegalArgumentException("displayData must not be null");
        if (userInput == null)
            throw new IllegalArgumentException("userInput must not be null");

        this.userInput = userInput;
        this.displayData = displayData;
    }

    @Override
    public String getUserInput() {
        return userInput;
    }

    @Override
    public Display getDisplayData() {
        return displayData;
    }
}
