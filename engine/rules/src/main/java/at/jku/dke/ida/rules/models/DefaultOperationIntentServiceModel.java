package at.jku.dke.ida.rules.models;

import at.jku.dke.ida.rules.interfaces.OperationIntentServiceModel;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.operations.Operation;
import at.jku.dke.ida.shared.session.SessionModel;

import java.util.Locale;
import java.util.Map;

/**
 * Default model used by {@link at.jku.dke.ida.rules.services.OperationIntentService}.
 */
public class DefaultOperationIntentServiceModel extends AbstractServiceModel implements OperationIntentServiceModel {

    private final String userInput;
    private final Map<Integer, Operation> possibleOperations;

    /**
     * Instantiates a new instance of class {@link DefaultOperationIntentServiceModel}.
     *
     * @param currentState       The current state of the state machine.
     * @param sessionModel       The session model.
     * @param possibleOperations Map of possible operations (the key has to be the position in the display list).
     * @throws IllegalArgumentException If the any of the parameters is {@code null} or empty.
     */
    public DefaultOperationIntentServiceModel(String currentState, SessionModel sessionModel, Map<Integer, Operation> possibleOperations) {
        super(currentState, sessionModel);
        if (possibleOperations == null)
            throw new IllegalArgumentException("possibleOperations must not be null");
        this.possibleOperations = possibleOperations;
        this.userInput = sessionModel.getUserInput();
    }

    /**
     * Instantiates a new instance of class {@link DefaultOperationIntentServiceModel}.
     *
     * @param currentState       The current state of the state machine.
     * @param locale             The display locale.
     * @param analysisSituation  The analysis situation.
     * @param operation          The operation the user wants to perform.
     * @param sessionModel       The session model.
     * @param userInput          The input of the user.
     * @param possibleOperations Map of possible operations (the key has to be the position in the display list).
     * @throws IllegalArgumentException If any of the parameters is {@code null} (except {@code locale} and {@code operation}).
     */
    public DefaultOperationIntentServiceModel(String currentState, Locale locale, EngineAnalysisSituation analysisSituation, Event operation, SessionModel sessionModel,
                                              String userInput, Map<Integer, Operation> possibleOperations) {
        super(currentState, locale, analysisSituation, operation, sessionModel);

        if (possibleOperations == null)
            throw new IllegalArgumentException("possibleOperations must not be null");
        if (userInput == null)
            throw new IllegalArgumentException("userInput must not be null");

        this.userInput = userInput;
        this.possibleOperations = possibleOperations;
    }

    @Override
    public String getUserInput() {
        return userInput;
    }

    @Override
    public Map<Integer, Operation> getPossibleOperations() {
        return possibleOperations;
    }
}
