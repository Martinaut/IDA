package at.jku.dke.inga.rules.models;

import at.jku.dke.inga.shared.models.AnalysisSituation;
import at.jku.dke.inga.shared.operations.Operation;

import java.util.Locale;
import java.util.Map;

/**
 * Model required by {@link at.jku.dke.inga.rules.services.OperationService}.
 */
public class OperationServiceModel extends DroolsServiceModel {

    private final String userInput;
    private final Map<Integer, Operation> possibleOperations;

    /**
     * Instantiates a new instance of class {@link OperationServiceModel}.
     *
     * @param currentState       The current state of the state machine.
     * @param locale             The display locale.
     * @param analysisSituation  The analysis situation.
     * @param operation          The operation the user wants to perform.
     * @param additionalData     Additional data.
     * @param userInput          The input of the user.
     * @param possibleOperations Map of possible operations (the key has to be the position in the display list).
     * @throws IllegalArgumentException If any of the parameters is {@code null} (except {@code locale} and {@code additionalData}).
     */
    public OperationServiceModel(String currentState, Locale locale, AnalysisSituation analysisSituation, String operation, Map<String, Object> additionalData,
                                 String userInput, Map<Integer, Operation> possibleOperations) {
        super(currentState, locale, analysisSituation, operation, additionalData);

        if (possibleOperations == null)
            throw new IllegalArgumentException("possibleOperations must not be null");
        if (userInput == null)
            throw new IllegalArgumentException("userInput must not be null");

        this.userInput = userInput;
        this.possibleOperations = possibleOperations;
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
     * Gets the possible operations.
     * The key has to be the position in the display list.
     *
     * @return the possible operations
     */
    public Map<Integer, Operation> getPossibleOperations() {
        return possibleOperations;
    }
}
