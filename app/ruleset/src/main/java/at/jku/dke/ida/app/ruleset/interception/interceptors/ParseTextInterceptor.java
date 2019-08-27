package at.jku.dke.ida.app.ruleset.interception.interceptors;

import at.jku.dke.ida.csp.service.ConstraintSatisfactionService;
import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.models.CubeSimilarity;
import at.jku.dke.ida.data.models.Label;
import at.jku.dke.ida.data.models.Similarity;
import at.jku.dke.ida.data.repositories.SimpleRepository;
import at.jku.dke.ida.nlp.drools.GraphDBSimilarityService;
import at.jku.dke.ida.nlp.drools.WordGroupsService;
import at.jku.dke.ida.nlp.helpers.SimilarityHelper;
import at.jku.dke.ida.nlp.models.GraphDBSimilarityServiceModel;
import at.jku.dke.ida.nlp.models.WordGroup;
import at.jku.dke.ida.nlp.models.WordGroupsServiceModel;
import at.jku.dke.ida.scxml.interceptors.ParseFreeTextInterceptor;
import at.jku.dke.ida.shared.session.SessionModel;
import at.jku.dke.ida.shared.spring.BeanUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Parses the user input and sets the values in the analysis situation.
 */
@Service
public class ParseTextInterceptor implements ParseFreeTextInterceptor {

    private static Logger LOGGER = LogManager.getLogger(ParseTextInterceptor.class);
    private static final ConstraintSatisfactionService service;

    static {
        service = new ConstraintSatisfactionService();
    }

    /**
     * Use this method to parse the user input and set the values appropriately.
     *
     * @param sessionModel The basic model.
     * @param result       Nothing
     * @return Nothing
     */
    @Override
    public Void modifyResult(SessionModel sessionModel, Void result) {
        if (sessionModel == null || sessionModel.getUserInput() == null || sessionModel.getUserInput().isBlank())
            return null;

        LOGGER.info("Filling analysis situation");
        Set<WordGroup> wordGroups = getWordGroups(sessionModel, sessionModel.getUserInput());
        Set<CubeSimilarity> similarities = getSimilarities(sessionModel, wordGroups);
        service.fillAnalysisSituation(sessionModel, similarities);
        return null;
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
