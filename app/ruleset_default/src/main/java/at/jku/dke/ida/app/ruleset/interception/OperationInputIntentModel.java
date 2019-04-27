package at.jku.dke.ida.app.ruleset.interception;

import at.jku.dke.ida.data.models.Similarity;
import at.jku.dke.ida.rules.models.OperationIntentServiceModel;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.models.AnalysisSituation;
import at.jku.dke.ida.shared.operations.Operation;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/**
 * Used by the {@link OperationInputIntentInterceptor}.
 */
public class OperationInputIntentModel extends OperationIntentServiceModel {

    private final Collection<Similarity<Displayable>> similarities;

    /**
     * Instantiates a new instance of class {@link OperationInputIntentModel}.
     *
     * @param currentState       The current state of the state machine.
     * @param locale             The display locale.
     * @param analysisSituation  The analysis situation.
     * @param operation          The operation the user wants to perform.
     * @param additionalData     Additional data.
     * @param userInput          The input of the user.
     * @param possibleOperations Map of possible operations (the key has to be the position in the display list).
     * @param similarities       The found similarities.
     * @throws IllegalArgumentException If any of the parameters is {@code null} (except {@code locale} and {@code additionalData}).
     */
    public OperationInputIntentModel(String currentState, Locale locale, AnalysisSituation analysisSituation, String operation,
                                     Map<String, Object> additionalData, String userInput, Map<Integer, Operation> possibleOperations,
                                     Collection<Similarity<Displayable>> similarities) {
        super(currentState, locale, analysisSituation, operation, additionalData, userInput, possibleOperations);
        this.similarities = Collections.unmodifiableCollection(similarities);
    }

    /**
     * Gets the similarities.
     *
     * @return the similarities
     */
    public Collection<Similarity<Displayable>> getSimilarities() {
        return similarities;
    }

    /**
     * Gets similarity with the highest score.
     * If there are no similarities available, {@code null} will be returned.
     *
     * @return the top similarity
     */
    public Similarity<Displayable> getTopSimilarity() {
        if (this.similarities == null) return null;
        return this.similarities.stream().sorted().findFirst().orElse(null);
    }
}
