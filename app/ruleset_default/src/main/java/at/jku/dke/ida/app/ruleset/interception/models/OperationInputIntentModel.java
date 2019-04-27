package at.jku.dke.ida.app.ruleset.interception.models;

import at.jku.dke.ida.data.models.Similarity;
import at.jku.dke.ida.rules.models.DefaultOperationIntentServiceModel;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.operations.Operation;
import at.jku.dke.ida.shared.session.SessionModel;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/**
 * Used by the {@link at.jku.dke.ida.app.ruleset.interception.interceptors.OperationInputIntentInterceptor}.
 */
public class OperationInputIntentModel extends DefaultOperationIntentServiceModel {

    private final Collection<Similarity<Displayable>> similarities;

    /**
     * Instantiates a new instance of class {@link OperationInputIntentModel}.
     *
     * @param currentState       The current state of the state machine.
     * @param sessionModel       The session model.
     * @param possibleOperations Map of possible operations (the key has to be the position in the display list).
     * @param similarities       The found similarities.
     * @throws IllegalArgumentException If the any of the parameters is {@code null} or empty.
     */
    public OperationInputIntentModel(String currentState, SessionModel sessionModel, Map<Integer, Operation> possibleOperations, Collection<Similarity<Displayable>> similarities) {
        super(currentState, sessionModel, possibleOperations);
        this.similarities = Collections.unmodifiableCollection(similarities);
    }

    /**
     * Instantiates a new instance of class {@link OperationInputIntentModel}.
     *
     * @param currentState       The current state of the state machine.
     * @param locale             The display locale.
     * @param analysisSituation  The analysis situation.
     * @param operation          The operation the user wants to perform.
     * @param sessionModel       The session model.
     * @param userInput          The input of the user.
     * @param possibleOperations Map of possible operations (the key has to be the position in the display list).
     * @param similarities       The found similarities.
     * @throws IllegalArgumentException If any of the parameters is {@code null} (except {@code locale} and {@code operation}).
     */
    public OperationInputIntentModel(String currentState, Locale locale, EngineAnalysisSituation analysisSituation, Event operation, SessionModel sessionModel, String userInput, Map<Integer, Operation> possibleOperations, Collection<Similarity<Displayable>> similarities) {
        super(currentState, locale, analysisSituation, operation, sessionModel, userInput, possibleOperations);
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
