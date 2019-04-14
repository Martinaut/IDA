package at.jku.dke.inga.app.nlp.helpers;

import at.jku.dke.inga.app.nlp.NLPProcessor;
import at.jku.dke.inga.app.nlp.models.WordGroup;
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

//    /**
//     * Returns all labels that have an exact match with a part of the initial sentence.
//     *
//     * @param model The model.
//     * @return All exact matches
//     * @see #hasExactMatches(InitialSentenceModel)
//     */
//    public static Collection<CubeSimilarity> exactMatches(InitialSentenceModel model) {
//        return model.getAllLabels().stream()
//                .filter(x -> model.getInitialSentence().contains(x.getLabel()))
//                .map(x -> new CubeSimilarity(x.getLabel(), null, x.getUri(), 0.5))
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * Returns {@code true}, if there is at least one exact match with a part of the initial sentence.
//     *
//     * @param model The model.
//     * @return {@code true} if there is an exact match; {@code false} otherwise.
//     * @see #exactMatches(InitialSentenceModel)
//     */
//    public static boolean hasExactMatches(InitialSentenceModel model) {
//        return model.getAllLabels().stream()
//                .anyMatch(x -> model.getInitialSentence().contains(x.getLabel()));
//    }

    /**
     * Converts a set of strings to a set of word groups.
     *
     * @param words The set with words.
     * @return Converted set with word groups
     */
    public static Set<WordGroup> convert(Set<String> words) {
        return words.stream()
                .map(WordGroup::new)
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
        return convert(NLPProcessor.executeTregex(doc, pattern));
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
        return convert(NLPProcessor.executeSemgrex(doc, pattern));
    }
}
