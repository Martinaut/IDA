package at.jku.dke.inga.rules.models;

import at.jku.dke.inga.shared.models.AnalysisSituation;
import at.jku.dke.inga.shared.models.NonComparativeAnalysisSituation;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Model required by {@link at.jku.dke.inga.rules.services.OperationDisplayDeterminationService}.
 */
public class OperationDisplayDeterminationServiceModel extends DroolsServiceModel {

    private final Collection<String> measures;

    /**
     * Instantiates a new instance of class {@link DroolsServiceModel}.
     *
     * @param currentState      The current state of the state machine.
     * @param analysisSituation The analysis situation.
     * @param locale            The display locale.
     * @param measures          The measures of the selected cube.
     * @throws IllegalArgumentException If {@code currentState} or {@code analysisSituation} are {@code null} (or empty).
     */
    public OperationDisplayDeterminationServiceModel(String currentState, AnalysisSituation analysisSituation, Locale locale, Set<String> measures) {
        super(currentState, analysisSituation, locale);
        this.measures = Collections.unmodifiableSet(measures);
    }

    /**
     * Gets the measures of the cube.
     *
     * @return the measures
     */
    public Collection<String> getMeasures() {
        return measures;
    }

    /**
     * Returns all measures contained in {@link #getMeasures()} ()} which are not already selected in the analysis situation.
     *
     * @return not already selected measures
     */
    public Collection<String> getNotSelectedMeasures() {
        if (measures == null || measures.isEmpty()) return Collections.emptyList();
        if (!(getAnalysisSituation() instanceof NonComparativeAnalysisSituation)) return Collections.emptyList();
        NonComparativeAnalysisSituation as = (NonComparativeAnalysisSituation) getAnalysisSituation();

        return measures.parallelStream()
                .filter(x -> !as.getMeasures().contains(x))
                .collect(Collectors.toSet());

    }
}
