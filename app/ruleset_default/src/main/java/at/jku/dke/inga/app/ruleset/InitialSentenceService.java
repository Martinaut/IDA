package at.jku.dke.inga.app.ruleset;

import at.jku.dke.inga.app.ruleset.csp.service.ConstraintSatisfactionService;
import at.jku.dke.inga.app.ruleset.drools.SimilarityService;
import at.jku.dke.inga.app.ruleset.drools.WordGroupsService;
import at.jku.dke.inga.app.ruleset.models.SimilarityServiceModel;
import at.jku.dke.inga.app.ruleset.models.WordGroup;
import at.jku.dke.inga.app.ruleset.models.WordGroupsServiceModel;
import at.jku.dke.inga.data.models.Similarity;
import at.jku.dke.inga.shared.session.SessionModel;
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

    public static void fillAnalysisSituation(SessionModel sessionModel, String initialSentence) {
        if (sessionModel == null) throw new IllegalArgumentException("sessionModel must not be null");
        if (initialSentence == null || initialSentence.isBlank()) return;

        LOGGER.info("Filling analysis situation");
        Set<WordGroup> wordGroups = getWordGroups(sessionModel, initialSentence);
        Set<Similarity> similarities = getSimilarities(sessionModel, wordGroups);
        service.fillAnalysisSituation(sessionModel.getLocale().getLanguage(), sessionModel.getAnalysisSituation(), similarities);
    }

    private static Set<WordGroup> getWordGroups(SessionModel sessionModel, String initialSentence) {
        // Create model
        WordGroupsServiceModel model = new WordGroupsServiceModel(
                sessionModel.getLocale(),
                sessionModel.getAnalysisSituation(),
                sessionModel.getAdditionalData(),
                initialSentence
        );

        // Run service
        return new WordGroupsService().executeRules(model);
    }

    private static Set<Similarity> getSimilarities(SessionModel sessionModel, Set<WordGroup> wordGroups) {
        // Create model
        SimilarityServiceModel model = new SimilarityServiceModel(
                sessionModel.getLocale(),
                sessionModel.getAnalysisSituation(),
                sessionModel.getAdditionalData(),
                wordGroups
        );

        // Run service
        return new SimilarityService().executeRules(model);
    }
}
