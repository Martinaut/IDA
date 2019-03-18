package at.jku.dke.inga.rules.models;

import at.jku.dke.inga.shared.models.AnalysisSituation;

import java.util.Locale;

/**
 * Model required by {@link at.jku.dke.inga.rules.services.SetValueService}.
 */
public class SetStringValueServiceModel extends SetValueServiceModel<String> {

    /**
     * Instantiates a new instance of class {@link SetStringValueServiceModel}.
     *
     * @param currentState      The current state of the state machine.
     * @param analysisSituation The analysis situation.
     * @param locale            The display locale.
     * @param s                 The selected value.
     * @param operation         The operation to execute.
     * @throws IllegalArgumentException If {@code currentState} or {@code analysisSituation} are {@code null} (or empty).
     */
    public SetStringValueServiceModel(String currentState, AnalysisSituation analysisSituation, Locale locale, String s, String operation) {
        super(currentState, analysisSituation, locale, s, operation);
    }
}
