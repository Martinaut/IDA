package at.jku.dke.inga.rules.models;

import at.jku.dke.inga.shared.display.Display;
import at.jku.dke.inga.shared.models.AnalysisSituation;

import java.util.Locale;
import java.util.Map;

/**
 * Model required by {@link at.jku.dke.inga.rules.services.ValueService}.
 */
public class ValueServiceModel extends DroolsServiceModel {

    private final String userInput;
    private final Display displayData;

    /**
     * Instantiates a new instance of class {@link ValueServiceModel}.
     *
     * @param currentState      The current state of the state machine.
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param operation         The operation the user wants to perform.
     * @param additionalData    Additional data.
     * @param userInput         The input of the user.
     * @param displayData       The display data.
     * @throws IllegalArgumentException If any of the parameters is {@code null} (except {@code locale} and {@code additionalData}).
     */
    public ValueServiceModel(String currentState, Locale locale, AnalysisSituation analysisSituation, String operation, Map<String, Object> additionalData,
                             String userInput, Display displayData) {
        super(currentState, locale, analysisSituation, operation, additionalData);

        if (displayData == null)
            throw new IllegalArgumentException("displayData must not be null");
        if (userInput == null)
            throw new IllegalArgumentException("userInput must not be null");

        this.userInput = userInput;
        this.displayData = displayData;
    }

    /**
     * Gets the user input.
     *
     * @return the user input
     */
    public String getUserInput() {
        return userInput;
    }

    /**
     * Gets the display data.
     *
     * @return the display data
     */
    public Display getDisplayData() {
        return displayData;
    }
}
