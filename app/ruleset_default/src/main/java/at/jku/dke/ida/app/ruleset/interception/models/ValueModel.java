package at.jku.dke.ida.app.ruleset.interception.models;

import at.jku.dke.ida.data.models.Similarity;
import at.jku.dke.ida.rules.models.DefaultValueServiceModel;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.display.Display;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.display.TwoListDisplay;
import at.jku.dke.ida.shared.models.AnalysisSituation;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/**
 * Used by the {@link at.jku.dke.ida.app.ruleset.interception.interceptors.ValueInterceptor}.
 */
public class ValueModel extends DefaultValueServiceModel {

    private final Collection<Similarity<Displayable>> similarities;

    /**
     * Instantiates a new instance of class {@link DefaultValueServiceModel}.
     *
     * @param currentState The current state of the state machine.
     * @param sessionModel The session model.
     * @param similarities The found similarities.
     * @throws IllegalArgumentException If the any of the parameters is {@code null} or empty.
     */
    public ValueModel(String currentState, SessionModel sessionModel, Collection<Similarity<Displayable>> similarities) {
        super(currentState, sessionModel);
        this.similarities = Collections.unmodifiableCollection(similarities);
    }

    /**
     * Instantiates a new instance of class {@link DefaultValueServiceModel}.
     *
     * @param currentState      The current state of the state machine.
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param operation         The operation the user wants to perform.
     * @param sessionModel      The session model.
     * @param userInput         The user input.
     * @param displayData       The data displayed to the user.
     * @param similarities      The found similarities.
     * @throws IllegalArgumentException If the any of the parameters (except {@code locale} and {@code operation}) is {@code null} or empty.
     */
    public ValueModel(String currentState, Locale locale, EngineAnalysisSituation analysisSituation, Event operation,
                      SessionModel sessionModel, String userInput, Display displayData,
                      Collection<Similarity<Displayable>> similarities) {
        super(currentState, locale, analysisSituation, operation, sessionModel, userInput, displayData);
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
     * <p>
     * If the display is an instance of {@link at.jku.dke.ida.shared.display.TwoListDisplay}, similarities of the
     * left part will be returned.
     *
     * @return the top similarity
     */
    public Similarity<Displayable> getTopSimilarity() {
        if (this.similarities == null) return null;
        if (getDisplayData() instanceof TwoListDisplay) {
            TwoListDisplay disp = (TwoListDisplay) getDisplayData();
            return this.similarities
                    .stream()
                    .filter(x -> disp.getDataLeft().stream()
                            .anyMatch(d -> d.getDisplayableId().equals(x.getElement().getDisplayableId())))
                    .sorted()
                    .findFirst().orElse(null);
        }
        return this.similarities.stream().sorted().findFirst().orElse(null);
    }

    /**
     * Gets similarity with the highest score.
     * If there are no similarities available, {@code null} will be returned.
     * <p>
     * If the display is an instance of {@link at.jku.dke.ida.shared.display.TwoListDisplay}, similarities of the
     * right part will be returned; {@code null} otherwise.
     *
     * @return the top similarity
     */
    public Similarity<Displayable> getRightTopSimilarity() {
        if (this.similarities == null) return null;
        if (!(getDisplayData() instanceof TwoListDisplay)) return null;
        TwoListDisplay disp = (TwoListDisplay) getDisplayData();
        return this.similarities
                .stream()
                .filter(x -> disp.getDataRight().stream()
                        .anyMatch(d -> d.getDisplayableId().equals(x.getElement().getDisplayableId())))
                .sorted()
                .findFirst().orElse(null);
    }
}
