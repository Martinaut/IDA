package at.jku.dke.ida.nlp.models;

import at.jku.dke.ida.rules.models.AbstractServiceModel;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.models.EngineAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;
import at.jku.dke.ida.nlp.drools.GraphDBSimilarityService;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

/**
 * Model used by {@link GraphDBSimilarityService}.
 */
public class GraphDBSimilarityServiceModel extends AbstractServiceModel {

    private final String cube;
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
        this.cube = null;
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
        this.cube = null;
    }

    /**
     * Instantiates a new instance of class {@link GraphDBSimilarityServiceModel}.
     *
     * @param sessionModel The session model.
     * @param wordGroups   The word groups.
     * @param cube         The cube for which to load the similarities.
     * @throws IllegalArgumentException If the any of the parameters is {@code null} or empty.
     */
    public GraphDBSimilarityServiceModel(SessionModel sessionModel, Set<WordGroup> wordGroups, String cube) {
        super("NONE", sessionModel);
        this.cube = cube;
        this.wordGroups = wordGroups;
    }

    /**
     * Instantiates a new instance of class {@link GraphDBSimilarityServiceModel}.
     *
     * @param locale            The display locale.
     * @param analysisSituation The analysis situation.
     * @param operation         The operation the user wants to perform.
     * @param sessionModel      The session model.
     * @param wordGroups        The word groups.
     * @param cube              The cube for which to load the similarities.
     * @throws IllegalArgumentException If the any of the parameters (except {@code locale} and {@code operation}) is {@code null} or empty.
     */
    public GraphDBSimilarityServiceModel(Locale locale, EngineAnalysisSituation analysisSituation,
                                         Event operation, SessionModel sessionModel, Set<WordGroup> wordGroups, String cube) {
        super("NONE", locale, analysisSituation, operation, sessionModel);
        this.cube = cube;
        this.wordGroups = wordGroups;
    }

    /**
     * Gets the word groups for which to find similarities in the schema.
     *
     * @return The word groups.
     */
    public Set<WordGroup> getWordGroups() {
        return wordGroups;
    }

    /**
     * Gets the cube for which to load the similarities.
     *
     * @return the cube
     */
    public String getCube() {
        return cube;
    }
}
