package at.jku.dke.inga.rules.models;

import at.jku.dke.inga.shared.models.AnalysisSituation;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * Model required by {@link at.jku.dke.inga.rules.services.ValuesDisplayDeterminationService}.
 */
public class ValuesDisplayDeterminationServiceModel extends DroolsServiceModel {

    private final String operation;

    /**
     * Instantiates a new instance of class {@link ValuesDisplayDeterminationServiceModel}.
     *
     * @param currentState      The current state of the state machine.
     * @param analysisSituation The analysis situation.
     * @param operation         The requested operation.
     * @param locale            The display locale.
     * @throws IllegalArgumentException If any of the parameters is {@code null} (or empty).
     */
    public ValuesDisplayDeterminationServiceModel(String currentState, AnalysisSituation analysisSituation, Locale locale, String operation) {
        super(currentState, analysisSituation, locale);

        if (StringUtils.isBlank(operation)) throw new IllegalArgumentException("operation must not be empty or null");
        this.operation = operation;
    }

    /**
     * Gets the requested operation.
     *
     * @return the operation
     */
    public String getOperation() {
        return operation;
    }
}
