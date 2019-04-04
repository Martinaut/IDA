package at.jku.dke.inga.app.ruleset.drools;

import at.jku.dke.inga.data.models.Label;
import at.jku.dke.inga.rules.models.DroolsServiceModel;
import at.jku.dke.inga.shared.models.AnalysisSituation;
import edu.stanford.nlp.pipeline.CoreDocument;

import java.util.*;

/**
 * Model used by {@link InitialSentenceService}.
 */
public class InitialSentenceModel extends DroolsServiceModel {

    private final String sessionId;
    private final String initialSentence;

    private Collection<Label> allLabels;
    private CoreDocument annotatedText;

    /**
     * Instantiates a new instance of class {@link InitialSentenceModel}.
     *
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param additionalData    Additional data.
     * @param sessionId         The session id.
     * @param initialSentence   The initial sentence.
     * @throws IllegalArgumentException If {@code analysisSituation} is {@code null} or empty.
     */
    public InitialSentenceModel(Locale locale, AnalysisSituation analysisSituation, Map<String, Object> additionalData, String sessionId, String initialSentence) {
        super("NONE", locale, analysisSituation, "initialSentence", additionalData);
        this.sessionId = sessionId;
        this.initialSentence = initialSentence;
        this.allLabels = Collections.emptySet();
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
     * Gets the session id.
     *
     * @return the session id
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Gets labels of all schema elements.
     *
     * @return the all labels
     */
    public Collection<Label> getAllLabels() {
        return allLabels;
    }

    /**
     * Sets labels of all schema elements.
     *
     * @param allLabels the all labels
     */
    void setAllLabels(Collection<Label> allLabels) {
        this.allLabels = allLabels;
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
