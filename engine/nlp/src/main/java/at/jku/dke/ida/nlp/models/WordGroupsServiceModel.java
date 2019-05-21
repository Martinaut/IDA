package at.jku.dke.ida.nlp.models;

import at.jku.dke.ida.rules.models.AbstractServiceModel;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;
import edu.stanford.nlp.pipeline.CoreDocument;
import at.jku.dke.ida.nlp.drools.WordGroupsService;

import java.util.Locale;

/**
 * Model used by {@link WordGroupsService}.
 */
public class WordGroupsServiceModel extends AbstractServiceModel {

    private final String sentence;
    private CoreDocument annotatedText;

    /**
     * Instantiates a new instance of class {@link WordGroupsServiceModel}.
     *
     * @param sessionModel The session model.
     * @param sentence     The sentence.
     * @throws IllegalArgumentException If {@code sessionModel} is {@code null} or empty.
     */
    public WordGroupsServiceModel(SessionModel sessionModel, String sentence) {
        super("NONE", sessionModel);
        this.sentence = sentence;
    }

    /**
     * Instantiates a new instance of class {@link WordGroupsServiceModel}.
     *
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param sessionModel      The session model.
     * @param sentence          The sentence.
     * @throws IllegalArgumentException If the any of the parameters (except {@code locale} and {@code sentence}) is {@code null} or empty.
     */
    public WordGroupsServiceModel(Locale locale, EngineAnalysisSituation analysisSituation, SessionModel sessionModel, String sentence) {
        super("NONE", locale, analysisSituation, null, sessionModel);
        this.sentence = sentence;
    }

    /**
     * Gets the sentence.
     *
     * @return the sentence
     */
    public String getSentence() {
        return sentence;
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
