package at.jku.dke.ida.rules.interfaces;

import at.jku.dke.ida.shared.operations.Operation;

import java.util.Map;

/**
 * Model used by {@link at.jku.dke.ida.rules.services.OperationIntentService}.
 */
public interface OperationIntentServiceModel extends ServiceModel {

    /**
     * Gets the user input.
     *
     * @return the user input
     */
    String getUserInput();

    /**
     * Gets the possible operations.
     * The key has to be the position in the display list.
     *
     * @return the possible operations
     */
    Map<Integer, Operation> getPossibleOperations();

}
