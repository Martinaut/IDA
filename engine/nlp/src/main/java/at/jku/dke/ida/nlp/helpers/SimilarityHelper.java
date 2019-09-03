package at.jku.dke.ida.nlp.helpers;

import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.models.similarity.CubeSimilarity;
import at.jku.dke.ida.data.models.similarity.Similarity;
import at.jku.dke.ida.data.repositories.SimilarityRepository;
import at.jku.dke.ida.nlp.models.WordGroup;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.spring.BeanUtil;
import info.debatty.java.stringsimilarity.*;

import java.util.List;
import java.util.function.BiFunction;

/**
 * Helper class containing methods used in "initial-sentence" rules.
 */
public final class SimilarityHelper {
    /**
     * Prevents creation of instances of this class.
     */
    private SimilarityHelper() {
    }

    private static final NormalizedLevenshtein normalizedLevenshtein = new NormalizedLevenshtein();
    private static final JaroWinkler jaroWinkler = new JaroWinkler();
    private static final MetricLCS metricLCS = new MetricLCS();
    private static final NGram ngram = new NGram();
    private static final Cosine cosine = new Cosine();
    private static final Jaccard jaccard = new Jaccard();
    private static final SorensenDice sorensen = new SorensenDice();

    /**
     * Gets english word similarities.
     *
     * @param wg The word group.
     * @return The word similarities.
     * @throws QueryException If an error occurred while executing the query.
     */
    public static List<CubeSimilarity> getGraphDbEnglishWordSimilarities(WordGroup wg) throws QueryException {
        return BeanUtil.getBean(SimilarityRepository.class).getWordSimilarity("en", wg.getText());
    }

    /**
     * Gets english word similarities.
     *
     * @param wg   The word group.
     * @param cube The cube.
     * @return The word similarities.
     * @throws QueryException If an error occurred while executing the query.
     */
    public static List<CubeSimilarity> getGraphDbEnglishWordSimilarities(WordGroup wg, String cube) throws QueryException {
        return BeanUtil.getBean(SimilarityRepository.class).getWordSimilarity("en", wg.getText(), cube);
    }

    /**
     * Gets the normalized levensthein similarity.
     *
     * @param input  The input.
     * @param option The option with which to compare the input.
     * @return the normalized levensthein similarity
     */
    public static Similarity getNormalizedLevenstheinSimilarity(String input, Displayable option) {
        return buildSimilarity(input, option, normalizedLevenshtein::similarity);
    }

    /**
     * Gets the jaro winkler similarity.
     *
     * @param input  The input.
     * @param option The option with which to compare the input.
     * @return the jaro winkler similarity
     */
    public static Similarity getJaroWinklerSimilarity(String input, Displayable option) {
        return buildSimilarity(input, option, jaroWinkler::similarity);
    }

    /**
     * Gets the longest common subsequence similarity.
     *
     * @param input  The input.
     * @param option The option with which to compare the input.
     * @return the lcs similarity
     */
    public static Similarity getMetricLCSSimilarity(String input, Displayable option) {
        return buildSimilarity(input, option, (s1, s2) -> 1 - metricLCS.distance(s1, s2));
    }

    /**
     * Gets the NGram similarity.
     *
     * @param input  The input.
     * @param option The option with which to compare the input.
     * @return the ngram similarity
     */
    public static Similarity getNGramSimilarity(String input, Displayable option) {
        return buildSimilarity(input, option, (s1, s2) -> 1 - ngram.distance(s1, s2));
    }

    /**
     * Gets the Cosine similarity.
     *
     * @param input  The input.
     * @param option The option with which to compare the input.
     * @return the cosine similarity
     */
    public static Similarity getCosineSimilarity(String input, Displayable option) {
        return buildSimilarity(input, option, (s1, s2) -> 1 - cosine.distance(s1, s2));
    }

    /**
     * Gets the Jaccard similarity.
     *
     * @param input  The input.
     * @param option The option with which to compare the input.
     * @return the jaccard similarity
     */
    public static Similarity getJaccardSimilarity(String input, Displayable option) {
        return buildSimilarity(input, option, (s1, s2) -> 1 - jaccard.distance(s1, s2));
    }

    /**
     * Gets the Sorensen Dice similarity.
     *
     * @param input  The input.
     * @param option The option with which to compare the input.
     * @return the sorensen dice similarity
     */
    public static Similarity getSorensenDiceSimilarity(String input, Displayable option) {
        return buildSimilarity(input, option, (s1, s2) -> 1 - sorensen.distance(s1, s2));
    }

    private static Similarity buildSimilarity(String input, Displayable option, BiFunction<String, String, Double> scoringFunction) {
        return new Similarity<>(input, option, scoringFunction.apply(input.toLowerCase(), option.getTitle().toLowerCase()));
    }
}
