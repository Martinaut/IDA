package at.jku.dke.inga.rules.models;

import at.jku.dke.inga.shared.models.AnalysisSituation;
import at.jku.dke.inga.shared.operations.Operation;

import java.util.Locale;
import java.util.Map;

public class ResolveOperationInputModel extends DataModel<AnalysisSituation> {

    private final String userInput;
    private final Map<Integer, Operation> possibleOperations;

    /**
     * Instantiates a new instance of class {@link ResolveOperationInputModel}.
     *
     * @param currentState       The current state of the state machine.
     * @param analysisSituation  The analysis situation.
     * @param locale             The display locale.
     * @param userInput          The user input.
     * @param possibleOperations Possible operations (the key of the map has to be the display number).
     * @throws IllegalArgumentException If any of the parameters is {@code null} (or empty).
     */
    public ResolveOperationInputModel(String currentState, AnalysisSituation analysisSituation, Locale locale,
                                      String userInput, Map<Integer, Operation> possibleOperations) {
        super(currentState, analysisSituation, locale);

        if (userInput == null) throw new IllegalArgumentException("userInput must not be null");
        if (possibleOperations == null) throw new IllegalArgumentException("possibleOperations must not be null");
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
     * Returns the possible operations.
     * The key of the map has to be the display number.
     *
     * @return The possible operations.
     */
    public Map<Integer, Operation> getPossibleOperations() {
        return possibleOperations;
    }
}
