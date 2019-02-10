package at.jku.dke.inga.rules.models;

import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class ResolveValuesDataModel extends DataModel<NonComparativeAnalysisSituation> {

    private final String operation;

    /**
     * Instantiates a new instance of class {@link ResolveValuesDataModel}.
     *
     * @param currentState      The current state of the state machine.
     * @param analysisSituation The analysis situation.
     * @param operation         The requested operation.
     * @param locale            The display locale.
     * @throws IllegalArgumentException If any of the parameters is {@code null} (or empty).
     */
    public ResolveValuesDataModel(String currentState, NonComparativeAnalysisSituation analysisSituation, Locale locale, String operation) {
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
