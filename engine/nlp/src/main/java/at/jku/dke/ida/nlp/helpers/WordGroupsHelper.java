package at.jku.dke.ida.nlp.helpers;

import at.jku.dke.ida.nlp.NLPProcessor;
import at.jku.dke.ida.data.models.WordGroup;
import edu.stanford.nlp.pipeline.CoreDocument;

import java.util.Set;

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
     * Executes a Tregex Matcher using the specified pattern on the given document.
     *
     * @param doc     The document.
     * @param pattern The pattern.
     * @return Found results
     * @see NLPProcessor#executeTregex(CoreDocument, String)
     */
    public static Set<WordGroup> executeTregex(CoreDocument doc, String pattern) {
        return NLPProcessor.executeTregex(doc, pattern);
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
        return NLPProcessor.executeSemgrex(doc, pattern);
    }
}
