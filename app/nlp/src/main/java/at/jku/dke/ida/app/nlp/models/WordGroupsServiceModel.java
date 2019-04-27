package at.jku.dke.ida.app.nlp.models;

import at.jku.dke.ida.rules.models.AbstractServiceModel;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;
import edu.stanford.nlp.pipeline.CoreDocument;

import java.util.Locale;

/**
 * Model used by {@link at.jku.dke.ida.app.nlp.drools.WordGroupsService}.
 */
public class WordGroupsServiceModel extends AbstractServiceModel {

    private final String initialSentence;
    private CoreDocument annotatedText;

    /**
     * Instantiates a new instance of class {@link WordGroupsServiceModel}.
     *
     * @param sessionModel    The session model.
     * @param initialSentence The initial sentence.
     * @throws IllegalArgumentException If {@code sessionModel} is {@code null} or empty.
     */
    public WordGroupsServiceModel(SessionModel sessionModel, String initialSentence) {
        super("NONE", sessionModel);
        this.initialSentence = initialSentence;
    }

    /**
     * Instantiates a new instance of class {@link WordGroupsServiceModel}.
     *
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param sessionModel      The session model.
     * @param initialSentence   The initial sentence.
     * @throws IllegalArgumentException If the any of the parameters (except {@code locale} and {@code initialSentence}) is {@code null} or empty.
     */
    public WordGroupsServiceModel(Locale locale, EngineAnalysisSituation analysisSituation, SessionModel sessionModel, String initialSentence) {
        super("NONE", locale, analysisSituation, null, sessionModel);
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
