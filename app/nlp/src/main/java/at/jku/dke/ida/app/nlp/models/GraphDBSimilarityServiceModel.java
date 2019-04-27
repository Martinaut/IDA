package at.jku.dke.ida.app.nlp.models;

import at.jku.dke.ida.rules.models.AbstractServiceModel;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

/**
 * Model used by {@link at.jku.dke.ida.app.nlp.drools.GraphDBSimilarityService}.
 */
public class GraphDBSimilarityServiceModel extends AbstractServiceModel {

    private final Set<WordGroup> wordGroups;

    /**
     * Instantiates a new instance of class {@link GraphDBSimilarityServiceModel}.
     *
     * @param sessionModel The session model.
     * @param wordGroups   The word groups.
     * @throws IllegalArgumentException If {@code sessionModel} is {@code null} or empty.
     */
    public GraphDBSimilarityServiceModel(SessionModel sessionModel, Set<WordGroup> wordGroups) {
        super("NONE", sessionModel);
        this.wordGroups = Collections.unmodifiableSet(wordGroups);
    }

    /**
     * Instantiates a new instance of class {@link GraphDBSimilarityServiceModel}.
     *
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param sessionModel      The session model.
     * @param wordGroups        The word groups.
     * @throws IllegalArgumentException If the any of the parameters (except {@code locale} and {@code wordGroups}) is {@code null} or empty.
     */
    public GraphDBSimilarityServiceModel(Locale locale, EngineAnalysisSituation analysisSituation, SessionModel sessionModel, Set<WordGroup> wordGroups) {
        super("NONE", locale, analysisSituation, null, sessionModel);
        this.wordGroups = Collections.unmodifiableSet(wordGroups);
    }

    /**
     * Gets the word groups for which to find similarities in the schema.
     *
     * @return The word groups.
     */
    public Set<WordGroup> getWordGroups() {
        return wordGroups;
    }
}
