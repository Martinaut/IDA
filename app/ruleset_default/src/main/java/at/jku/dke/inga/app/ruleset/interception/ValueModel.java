package at.jku.dke.inga.app.ruleset.interception;

import at.jku.dke.inga.data.models.Similarity;
import at.jku.dke.inga.rules.models.ValueServiceModel;
import at.jku.dke.inga.shared.display.Display;
import at.jku.dke.inga.shared.display.Displayable;
import at.jku.dke.inga.shared.models.AnalysisSituation;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

public class ValueModel extends ValueServiceModel {

    private final Collection<Similarity<Displayable>> similarities;

    /**
     * Instantiates a new instance of class {@link ValueServiceModel}.
     *
     * @param currentState      The current state of the state machine.
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param operation         The operation the user wants to perform.
     * @param additionalData    Additional data.
     * @param userInput         The input of the user.
     * @param displayData       The display data.
     * @param similarities      The found similarities.
     * @throws IllegalArgumentException If any of the parameters is {@code null} (except {@code locale} and {@code additionalData}).
     */
    public ValueModel(String currentState, Locale locale, AnalysisSituation analysisSituation, String operation,
                      Map<String, Object> additionalData, String userInput, Display displayData,
                      Collection<Similarity<Displayable>> similarities) {
        super(currentState, locale, analysisSituation, operation, additionalData, userInput, displayData);
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
