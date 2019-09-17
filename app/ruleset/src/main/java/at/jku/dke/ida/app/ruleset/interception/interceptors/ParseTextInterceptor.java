package at.jku.dke.ida.app.ruleset.interception.interceptors;

import at.jku.dke.ida.csp.service.ConstraintSatisfactionService;
import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.models.labels.*;
import at.jku.dke.ida.data.models.similarity.ComparativeMeasureSimilarity;
import at.jku.dke.ida.data.models.similarity.ComparativeSimilarity;
import at.jku.dke.ida.data.models.similarity.CubeSimilarity;
import at.jku.dke.ida.data.models.similarity.DimensionSimilarity;
import at.jku.dke.ida.data.repositories.*;
import at.jku.dke.ida.nlp.drools.GraphDBSimilarityService;
import at.jku.dke.ida.nlp.drools.WordGroupsService;
import at.jku.dke.ida.nlp.helpers.SimilarityHelper;
import at.jku.dke.ida.nlp.models.GraphDBSimilarityServiceModel;
import at.jku.dke.ida.data.models.WordGroup;
import at.jku.dke.ida.nlp.models.WordGroupsServiceModel;
import at.jku.dke.ida.scxml.interceptors.ParseFreeTextInterceptor;
import at.jku.dke.ida.shared.models.NonComparativeAnalysisSituation;
import at.jku.dke.ida.shared.session.SessionModel;
import at.jku.dke.ida.shared.spring.BeanUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Parses the user input and sets the values in the analysis situation.
 */
@Service
public class ParseTextInterceptor implements ParseFreeTextInterceptor {

    // region --- STATIC ---
    private static Logger LOGGER = LogManager.getLogger(ParseTextInterceptor.class);
    private static final ConstraintSatisfactionService service;

    static {
        service = new ConstraintSatisfactionService();
    }
    // endregion

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
        var similarities = getStringSimilarities(sessionModel, wordGroups);
        similarities.addAll(getGraphDBSimilarities(sessionModel, wordGroups));
        service.fillAnalysisSituation(sessionModel, similarities);
        return null;
    }

    /**
     * Creates word-groups based on the given sentence.
     *
     * @param sessionModel    The session model.
     * @param initialSentence The sentence.
     * @return Set of found word-groups in the sentence.
     */
    private static Set<WordGroup> getWordGroups(SessionModel sessionModel, String initialSentence) {
        // Create model
        WordGroupsServiceModel model = new WordGroupsServiceModel(sessionModel, initialSentence);

        // Run service
        return new WordGroupsService().executeRules(model);
    }

    private static Set<CubeSimilarity> getStringSimilarities(SessionModel model, Set<WordGroup> wordGroups) {
        // Load all labels
        List<Label> labels = new ArrayList<>();
        try {
            labels.addAll(BeanUtil.getBean(AggregateMeasurePredicateRepository.class).getAllLabelsByLang(model.getLocale().getLanguage()));
            labels.addAll(BeanUtil.getBean(LevelRepository.class).getAllLabelsByLang(model.getLocale().getLanguage()));
            labels.addAll(BeanUtil.getBean(LevelPredicateRepository.class).getAllLabelsByLang(model.getLocale().getLanguage()));
//            labels.addAll(BeanUtil.getBean(LevelMemberRepository.class).getAllLabelsByLang(model.getLocale().getLanguage()));
            if (model.getAnalysisSituation() instanceof NonComparativeAnalysisSituation) {
                labels.addAll(BeanUtil.getBean(AggregateMeasureRepository.class).getAllLabelsByLang(model.getLocale().getLanguage()));
            } else {
                labels.addAll(BeanUtil.getBean(JoinConditionPredicateRepository.class).getAllLabelsByLang(model.getLocale().getLanguage()));
                labels.addAll(BeanUtil.getBean(ComparativeMeasureRepository.class).getAllLabelsByLang(model.getLocale().getLanguage()));
                labels.addAll(BeanUtil.getBean(ComparativeMeasurePredicateRepository.class).getAllLabelsByLang(model.getLocale().getLanguage()));
            }
        } catch (QueryException ex) {
            LOGGER.error("Could not load labels for string similarity calculation.", ex);
        }

        // Calculate similarities
        Set<CubeSimilarity> similarities = new HashSet<>();
        for (WordGroup wg : wordGroups) {
            for (Label lbl : labels) {
                var similarity = SimilarityHelper.getNormalizedLevenstheinSimilarity(wg.getText(), lbl);
                if (similarity.getScore() < 0.5) continue;

                if (lbl instanceof ComparativeMeasureLabel) {
                    var cml = (ComparativeMeasureLabel) lbl;
                    similarities.add(new ComparativeMeasureSimilarity(wg, cml.getCubeUri(), cml.getUri(), cml.getTypeUri(), similarity.getScore(), cml.getPart(), cml.getMeasure()));
                } else if (lbl instanceof ComparativeLabel) {
                    var cl = (ComparativeLabel) lbl;
                    similarities.add(new ComparativeSimilarity(wg, cl.getCubeUri(), cl.getUri(), cl.getTypeUri(), similarity.getScore(), cl.getPart()));
                } else if (lbl instanceof DimensionLevelLabel) {
                    var dll = (DimensionLevelLabel) lbl;
                    similarities.add(new DimensionSimilarity(wg, dll.getCubeUri(), dll.getUri(), dll.getTypeUri(), similarity.getScore(), dll.getDimensionUri()));
                } else if (lbl instanceof DimensionLabel) {
                    var dl = (DimensionLabel) lbl;
                    similarities.add(new DimensionSimilarity(wg, dl.getCubeUri(), dl.getUri(), dl.getTypeUri(), similarity.getScore(), dl.getDimensionUri()));
                } else if (lbl instanceof CubeLabel) {
                    var cl = (CubeLabel) lbl;
                    similarities.add(new CubeSimilarity(wg, cl.getCubeUri(), cl.getUri(), cl.getTypeUri(), similarity.getScore()));
                }
            }
        }
        return similarities;
    }

    private static Set<CubeSimilarity> getGraphDBSimilarities(SessionModel sessionModel, Set<WordGroup> wordGroups) {
        // GraphDB similarity
        GraphDBSimilarityServiceModel model = new GraphDBSimilarityServiceModel(sessionModel, wordGroups);
        Set<CubeSimilarity> similarities = new GraphDBSimilarityService().executeRules(model);

        // String similarity
//        try {
//            similarities = onlyKeepHighestStringSimilarity(sessionModel.getLocale().getLanguage(), similarities);
//        } catch (QueryException ex) {
//            LOGGER.error("Could not load labels for string similarity check.", ex);
//        }

        // Return result
        return similarities;
    }

//    private static Set<CubeSimilarity> onlyKeepHighestStringSimilarity(String language, Set<CubeSimilarity> similarities) throws QueryException {
//        // Load all labels
//        SimpleRepository repo = BeanUtil.getBean(SimpleRepository.class);
//        Map<String, Label> labels = repo.getLabelsByLangAndIris(language, similarities.stream().map(Similarity::getElement).collect(Collectors.toList()));
//
//        // Calculate similarity per term
//        Set<CubeSimilarity> reduced = new HashSet<>();
//        Map<String, List<CubeSimilarity>> map = similarities.stream().collect(Collectors.groupingBy(Similarity::getTerm));
//        for (String term : map.keySet()) {
//            // Calculate similarity
//            Map<CubeSimilarity, Integer> simMap = new HashMap<>();
//            for (CubeSimilarity sim : map.get(term)) {
//                Similarity levensthein = SimilarityHelper.getNormalizedLevenstheinSimilarity(term, labels.get(sim.getElement()));
//                Similarity jaro = SimilarityHelper.getJaroWinklerSimilarity(term, labels.get(sim.getElement()));
//
//                simMap.put(sim, (int) ((levensthein.getScore() * jaro.getScore()) * 1_000_000d));
//            }
//
//            // Get the one with the highest score
//            int max = simMap.values().stream().max(Integer::compareTo).orElse(0);
//            simMap.entrySet().stream()
//                    .filter(e -> e.getValue() == max)
//                    .map(Map.Entry::getKey)
//                    .forEach(reduced::add);
//        }
//        return reduced;
//    }
}
