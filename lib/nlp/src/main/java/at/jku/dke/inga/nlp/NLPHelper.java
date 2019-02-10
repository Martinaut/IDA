package at.jku.dke.inga.nlp;

import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.simple.Sentence;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * A class that provides helper-methods for executing Stanford CoreNLP Operations on input-texts.
 */
public class NLPHelper {
    /**
     * Prevents creating instances of this class.
     */
    private NLPHelper() {
    }
//TODO logging
    /**
     * Returns the dependency graph of the input-sentence.
     *
     * @param input One sentence.
     * @return The semantic graph of the sentence.
     * @throws IllegalArgumentException If input is {@code null} or empty.
     */
    public static SemanticGraph getSemanticGraph(String input) {
        if (StringUtils.isBlank(input)) throw new IllegalArgumentException("input must not be empty");
        return new Sentence(input).dependencyGraph();
    }

    /**
     * Returns the lemmas of the sentence.
     *
     * @param input One sentence.
     * @return Lemmas of the sentence ordered by position in the input text.
     * @throws IllegalArgumentException If input is {@code null} or empty.
     */
    public static List<String> getLemmas(String input) {
        if (StringUtils.isBlank(input)) throw new IllegalArgumentException("input must not be empty");
        return new Sentence(input).lemmas();
    }

    /**
     * Returns the part-of-speech-tags of the sentence.
     *
     * @param input One sentence.
     * @return POS-tags of the sentence ordered by position in the input text.
     * @throws IllegalArgumentException If input is {@code null} or empty.
     */
    public static List<String> getPartOfSpeech(String input) {
        if (StringUtils.isBlank(input)) throw new IllegalArgumentException("input must not be empty");
        return new Sentence(input).posTags();
    }
}
