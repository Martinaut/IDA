package at.jku.dke.ida.app.ruleset;

import at.jku.dke.ida.app.nlp.drools.GraphDBSimilarityService;
import at.jku.dke.ida.app.nlp.drools.WordGroupsService;
import at.jku.dke.ida.app.nlp.helpers.SimilarityHelper;
import at.jku.dke.ida.app.nlp.models.GraphDBSimilarityServiceModel;
import at.jku.dke.ida.app.nlp.models.WordGroup;
import at.jku.dke.ida.app.nlp.models.WordGroupsServiceModel;
import at.jku.dke.ida.app.ruleset.csp.service.ConstraintSatisfactionService;
import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.models.CubeSimilarity;
import at.jku.dke.ida.data.models.Label;
import at.jku.dke.ida.data.models.Similarity;
import at.jku.dke.ida.data.repositories.SimpleRepository;
import at.jku.dke.ida.shared.session.SessionModel;
import at.jku.dke.ida.shared.spring.BeanUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * Fills the analysis situation in the {@code sessionModel} with elements detected in the {@code initialSentence}.
     *
     * @param sessionModel    The session model.
     * @param initialSentence The initial sentence.
     */
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
        // GraphDB similarity
        GraphDBSimilarityServiceModel model = new GraphDBSimilarityServiceModel(sessionModel, wordGroups);
        Set<CubeSimilarity> similarities = new GraphDBSimilarityService().executeRules(model);

        // String similarity
        try {
            similarities = onlyKeepHighestStringSimilarity(sessionModel.getLocale().getLanguage(), similarities);
        } catch (QueryException ex) {
            LOGGER.error("Could not load labels for string similarity check.", ex);
        }

        // Return result
        return similarities;
    }

    private static Set<CubeSimilarity> onlyKeepHighestStringSimilarity(String language, Set<CubeSimilarity> similarities) throws QueryException {
        // Load all labels
        SimpleRepository repo = BeanUtil.getBean(SimpleRepository.class);
        Map<String, Label> labels = repo.getLabelsByLangAndIris(language, similarities.stream().map(Similarity::getElement).collect(Collectors.toList()));

        // Calculate similarity per term
        Set<CubeSimilarity> reduced = new HashSet<>();
        Map<String, List<CubeSimilarity>> map = similarities.stream().collect(Collectors.groupingBy(Similarity::getTerm));
        for (String term : map.keySet()) {
            // Calculate similarity
            Map<CubeSimilarity, Integer> simMap = new HashMap<>();
            for (CubeSimilarity sim : map.get(term)) {
                Similarity levensthein = SimilarityHelper.getNormalizedLevenstheinSimilarity(term, labels.get(sim.getElement()));
                Similarity jaro = SimilarityHelper.getJaroWinklerSimilarity(term, labels.get(sim.getElement()));

                simMap.put(sim, (int) ((levensthein.getScore() * jaro.getScore()) * 1_000_000d));
            }

            // Get the one with the highest score
            int max = simMap.values().stream().max(Integer::compareTo).orElse(0);
            simMap.entrySet().stream()
                    .filter(e -> e.getValue() == max)
                    .map(Map.Entry::getKey)
                    .forEach(reduced::add);
        }
        return reduced;
    }
}
