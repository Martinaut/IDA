package at.jku.dke.inga.app.ruleset.models;

import at.jku.dke.inga.rules.models.DroolsServiceModel;
import at.jku.dke.inga.shared.models.AnalysisSituation;
import edu.stanford.nlp.pipeline.CoreDocument;

import java.util.Locale;
import java.util.Map;

/**
 * Model used by {@link at.jku.dke.inga.app.ruleset.drools.WordGroupsService}.
 */
public class WordGroupsServiceModel extends DroolsServiceModel {

    private final String initialSentence;
    private CoreDocument annotatedText;

    /**
     * Instantiates a new instance of class {@link WordGroupsServiceModel}.
     *
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param additionalData    Additional data.
     * @param initialSentence   The initial sentence.
     * @throws IllegalArgumentException If {@code analysisSituation} is {@code null} or empty.
     */
    public WordGroupsServiceModel(Locale locale, AnalysisSituation analysisSituation, Map<String, Object> additionalData, String initialSentence) {
        super("NONE", locale, analysisSituation, "nlp", additionalData);
        this.initialSentence = initialSentence;
    }

    /**
     * Instantiates a new instance of class {@link WordGroupsServiceModel}.
     *
     * @param currentState      The current state.
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param operation         The operation.
     * @param additionalData    Additional data.
     * @param initialSentence   The initial sentence.
     * @throws IllegalArgumentException If {@code analysisSituation} is {@code null} or empty.
     */
    public WordGroupsServiceModel(String currentState, Locale locale, AnalysisSituation analysisSituation, String operation, Map<String, Object> additionalData, String initialSentence) {
        super(currentState, locale, analysisSituation, operation, additionalData);
        this.initialSentence = initialSentence;
    }

    /**
     * Gets the initial sentence.
     *
     * @return the initial sentence
     */
    public String getInitialSentence() {
        return initialSentence;
    }

    /**
     * Gets the annotated text.
     *
     * @return the annotated text
     */
    public CoreDocument getAnnotatedText() {
        return annotatedText;
    }

    /**
     * Sets the annotated text.
     *
     * @param annotatedText the annotated text
     */
    public void setAnnotatedText(CoreDocument annotatedText) {
        this.annotatedText = annotatedText;
    }
}
