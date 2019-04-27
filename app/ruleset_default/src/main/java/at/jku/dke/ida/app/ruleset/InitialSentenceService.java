package at.jku.dke.ida.app.ruleset;

import at.jku.dke.ida.app.nlp.drools.GraphDBSimilarityService;
import at.jku.dke.ida.app.nlp.drools.WordGroupsService;
import at.jku.dke.ida.app.nlp.models.GraphDBSimilarityServiceModel;
import at.jku.dke.ida.app.nlp.models.WordGroup;
import at.jku.dke.ida.app.nlp.models.WordGroupsServiceModel;
import at.jku.dke.ida.app.ruleset.csp.service.ConstraintSatisfactionService;
import at.jku.dke.ida.data.models.CubeSimilarity;
import at.jku.dke.ida.shared.session.SessionModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

/**
 * This class provides a method to fill the analysis situation based on the initial sentence.
 */
public final class InitialSentenceService {

    private static Logger LOGGER = LogManager.getLogger(InitialSentenceService.class);
    private static final ConstraintSatisfactionService service;

    static {
        service = new ConstraintSatisfactionService();
    }

    /**
     * Prevents creation of instances of this class.
     */
    private InitialSentenceService() {
    }

    //TODO: javadoc
    public static void fillAnalysisSituation(SessionModel sessionModel, String initialSentence) {
        if (sessionModel == null) throw new IllegalArgumentException("sessionModel must not be null");
        if (initialSentence == null || initialSentence.isBlank()) return;

        LOGGER.info("Filling analysis situation");
        Set<WordGroup> wordGroups = getWordGroups(sessionModel, initialSentence);
        Set<CubeSimilarity> similarities = getSimilarities(sessionModel, wordGroups);
        service.fillAnalysisSituation(sessionModel.getLocale().getLanguage(), sessionModel.getAnalysisSituation(), similarities);
    }

    private static Set<WordGroup> getWordGroups(SessionModel sessionModel, String initialSentence) {
        // Create model
        WordGroupsServiceModel model = new WordGroupsServiceModel(sessionModel, initialSentence);

        // Run service
        return new WordGroupsService().executeRules(model);
    }

    private static Set<CubeSimilarity> getSimilarities(SessionModel sessionModel, Set<WordGroup> wordGroups) {
        // Create model
        GraphDBSimilarityServiceModel model = new GraphDBSimilarityServiceModel(sessionModel, wordGroups);

        // Run service
        return new GraphDBSimilarityService().executeRules(model);
    }
}
