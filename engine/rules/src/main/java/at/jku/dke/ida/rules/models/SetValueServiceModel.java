package at.jku.dke.ida.rules.models;

import at.jku.dke.ida.shared.models.AnalysisSituation;

import java.util.Locale;
import java.util.Map;

/**
 * Model required by {@link at.jku.dke.ida.rules.services.SetValueService}.
 */
public class SetValueServiceModel extends DroolsServiceModel {

    private final Object value;

    /**
     * Instantiates a new instance of class {@link SetValueServiceModel}.
     *
     * @param currentState      The current state of the state machine.
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param operation         The operation the user wants to perform.
     * @param additionalData    Additional data.
     * @param value             The value.
     * @throws IllegalArgumentException If any of the parameters is {@code null} (except {@code locale} and {@code additionalData}).
     */
    public SetValueServiceModel(String currentState, Locale locale, AnalysisSituation analysisSituation, String operation,
                                Map<String, Object> additionalData, Object value) {
        super(currentState, locale, analysisSituation, operation, additionalData);

        if (operation == null)
            throw new IllegalArgumentException("operation must not be null");
        if (value == null)
            throw new IllegalArgumentException("value must not be null");

        this.value = value;
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
