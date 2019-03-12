package at.jku.dke.inga.rules.models;

import at.jku.dke.inga.shared.models.AnalysisSituation;

import java.util.Locale;

/**
 * Model required by {@link at.jku.dke.inga.rules.services.SetValueService}.
 */
public class SetValueServiceModel extends DroolsServiceModel {

    private final String value;
    private final String operation;

    /**
     * Instantiates a new instance of class {@link SetValueServiceModel}.
     *
     * @param currentState      The current state of the state machine.
     * @param analysisSituation The analysis situation.
     * @param locale            The display locale.
     * @param value             The selected value.
     * @param operation         The operation to execute.
     * @throws IllegalArgumentException If {@code currentState} or {@code analysisSituation} are {@code null} (or empty).
     */
    public SetValueServiceModel(String currentState, AnalysisSituation analysisSituation, Locale locale, String value, String operation) {
        super(currentState, analysisSituation, locale);
        this.value = value;
        this.operation = operation;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets operation.
     *
     * @return the operation
     */
    public String getOperation() {
        return operation;
    }
}
