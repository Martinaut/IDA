package at.jku.dke.ida.nlp.helpers;

import at.jku.dke.ida.nlp.NLPProcessor;
import at.jku.dke.ida.nlp.models.WordGroup;
import edu.stanford.nlp.pipeline.CoreDocument;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Helper class containing methods used in "word-groups" rules.
 */
public final class WordGroupsHelper {
    /**
     * Prevents creation of instances of this class.
     */
    private WordGroupsHelper() {
    }

    /**
     * Converts a set of strings to a set of word groups.
     *
     * @param words   The set with words.
     * @param pattern The pattern used to find the word groups.
     * @return Converted set with word groups
     */
    public static Set<WordGroup> convert(Set<String> words, String pattern) {
        return words.stream()
                .map(x -> new WordGroup(x, pattern))
                .collect(Collectors.toSet());
    }

    /**
     * Executes a Tregex Matcher using the specified pattern on the given document.
     *
     * @param doc     The document.
     * @param pattern The pattern.
     * @return Found results
     * @see NLPProcessor#executeTregex(CoreDocument, String)
     */
    public static Set<WordGroup> executeTregex(CoreDocument doc, String pattern) {
        return convert(NLPProcessor.executeTregex(doc, pattern), pattern);
    }

    /**
     * Executes a Semregex Matcher using the specified pattern on the given document.
     *
     * @param doc     The document.
     * @param pattern The pattern.
     * @return Found results
     * @see NLPProcessor#executeSemgrex(CoreDocument, String)
     */
    public static Set<WordGroup> executeSemgrex(CoreDocument doc, String pattern) {
        return convert(NLPProcessor.executeSemgrex(doc, pattern), pattern);
    }
}
