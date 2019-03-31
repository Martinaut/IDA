package at.jku.dke.inga.rules.models;

import at.jku.dke.inga.shared.models.AnalysisSituation;

import java.util.Locale;

/**
 * Model required by {@link at.jku.dke.inga.rules.services.SetValueService}.
 */
public class SetValueServiceModel extends DroolsServiceModel {

    private final String operation;
    private final Object value;

    /**
     * Instantiates a new instance of class {@link SetValueServiceModel}.
     *
     * @param currentState      The current state of the state machine.
     * @param analysisSituation The analysis situation.
     * @param locale            The display locale.
     * @param operation         The operation.
     * @param value             The value.
     * @throws IllegalArgumentException If any of the parameters is {@code null} (except {@code locale}).
     */
    public SetValueServiceModel(String currentState, AnalysisSituation analysisSituation, Locale locale, String operation, Object value) {
        super(currentState, analysisSituation, locale);

        if (operation == null)
            throw new IllegalArgumentException("operation must not be null");
        if (value == null)
            throw new IllegalArgumentException("value must not be null");

        this.operation = operation;
        this.value = value;
    }

    /**
     * Gets the operation.
     *
     * @return the operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }
}
