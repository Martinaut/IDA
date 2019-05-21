package at.jku.dke.ida.nlp;

import at.jku.dke.ida.nlp.helpers.SimilarityHelper;
import at.jku.dke.ida.shared.display.Displayable;
import at.jku.dke.ida.shared.display.SimpleDisplayable;
import org.junit.jupiter.api.Test;

class StringSimilarityTest {
    @Test
    void test() {
        String word1 = "drug prescription";
        Displayable word2 = new SimpleDisplayable(null, "durg presciption", null);

        System.out.println("Normalized Levensthein: " + SimilarityHelper.getNormalizedLevenstheinSimilarity(word1, word2).getScore());
        System.out.println("Jaro Winkler: " + SimilarityHelper.getJaroWinklerSimilarity(word1, word2).getScore());
        System.out.println("Cosine: " + SimilarityHelper.getCosineSimilarity(word1, word2).getScore());
        System.out.println("Jaccard: " + SimilarityHelper.getJaccardSimilarity(word1, word2).getScore());
        System.out.println("Metric LCS: " + SimilarityHelper.getMetricLCSSimilarity(word1, word2).getScore());
        System.out.println("NGram: " + SimilarityHelper.getNGramSimilarity(word1, word2).getScore());
        System.out.println("Sorensen Dice: " + SimilarityHelper.getSorensenDiceSimilarity(word1, word2).getScore());
    }
}
