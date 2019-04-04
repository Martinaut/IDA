package at.jku.dke.inga.app.nlp;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.Constituent;
import edu.stanford.nlp.trees.LabeledScoredConstituentFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("Duplicates")
public class NLPProcessorTest {

    @Test
    void testAnnotate() {
        CoreDocument doc = NLPProcessor.annotate("en", "Show me the total costs per insurant and doctor district.");

        // dependency parse for the second sentence
        SemanticGraph dependencyParse = doc.sentences().get(0).dependencyParse();
        System.out.println("Example: dependency parse");
        System.out.println(dependencyParse);

        // constituency parse for the second sentence
        System.out.println("Example: constituency parse");
        System.out.println(doc.sentences().get(0).constituencyParse());
        System.out.println();

        //
//        Set<String> groups = new HashSet<>();
//        for (CoreSentence sentence : doc.sentences()) {
//            Tree constituencyParse = sentence.constituencyParse();
//            Set<Constituent> constituents = constituencyParse.constituents(new LabeledScoredConstituentFactory());
//
//            for (Constituent constituent : constituents) {
//                if (constituent.label() != null && (constituent.label().value().equals("VP") || constituent.label().value().equals("NP"))) {
//                    List<Tree> parts = constituencyParse.getLeaves().subList(constituent.start(), constituent.end() + 1);
//
//                    StringBuilder builder = new StringBuilder();
//                    for (Tree tree : parts) {
//                        String pos = ((CoreLabel) tree.label()).getString(CoreAnnotations.PartOfSpeechAnnotation.class);
//                        if (!pos.equals("DT") && !pos.equals("IN") && !pos.equals("PRP")) {
//                            if (pos.equals("CC")) {
//                                if (builder.length() > 0) groups.add(builder.toString());
//                                builder = new StringBuilder();
//                            } else {
//                                if (builder.length() != 0) builder.append(' ');
//                                builder.append(tree.value());
//                            }
//                        }
//                    }
//
//                    if (builder.length() > 0)
//                        groups.add(builder.toString());
//                }
//            }
//        }

        //NLPProcessor.executeTregex(doc, "NP < /NN.?/=A");
        NLPProcessor.executeTregex(doc, "/NN.?/=A $ /NN.?/=B");
        //NLPProcessor.executeTregex(doc, "NP < (NN !$ CC)");
        //NLPProcessor.executeTregex(doc, "NP");
        //NLPProcessor.executeTregex(doc, "NN");
        //-NLPProcessor.executeTregex(doc, "NP .. NN");
        //NLPProcessor.executeTregex(doc, "NN $ NN");
        //NLPProcessor.executeTregex(doc, "NP < NN");

        //NLPProcessor.executeSemgrex(doc, "{tag:/NN.*/}=B >amod {tag:/JJ/}=A");
        //NLPProcessor.executeSemgrex(doc, "{tag:/NN.*/}=B >compound {tag:/NN.*/}=A");

       // System.out.println(groups);
    }

    @Test
    void testAnnotateEnglish() {
        CoreDocument doc = NLPProcessor.annotate("en", "I am interested in the total sale numbers per year and customer.");

        // list of the part-of-speech tags for the second sentence
        List<String> posTags = doc.sentences().get(0).posTags();
        System.out.println("Example: pos tags");
        System.out.println(posTags);
        System.out.println();

        // dependency parse for the second sentence
        SemanticGraph dependencyParse = doc.sentences().get(0).dependencyParse();
        System.out.println("Example: dependency parse");
        System.out.println(dependencyParse);

        // constituency parse for the second sentence
        Tree constituencyParse = doc.sentences().get(0).constituencyParse();
        System.out.println("Example: constituency parse");
        System.out.println(constituencyParse);
        System.out.println();

        dosomething(doc, "");

        Set<Constituent> treeConstituents = constituencyParse.constituents(new LabeledScoredConstituentFactory());
        for (Constituent constituent : treeConstituents) {
            if (constituent.label() != null &&
                    (constituent.label().toString().equals("VP") || constituent.label().toString().equals("NP"))) {
                System.err.println("found constituent: " + constituent.toString());
                System.err.println(constituencyParse.getLeaves().subList(constituent.start(), constituent.end() + 1));
            }
        }
    }

    @Test
    void testAnnotateGerman() {
        CoreDocument doc = NLPProcessor.annotate("de", "Zeige mir die Verkaufszahlen pro Jahr und Kunde.");

        // list of the part-of-speech tags for the second sentence
        List<String> posTags = doc.sentences().get(0).posTags();
        System.out.println("Example: pos tags");
        System.out.println(posTags);
        System.out.println();

        // dependency parse for the second sentence
        SemanticGraph dependencyParse = doc.sentences().get(0).dependencyParse();
        System.out.println("Example: dependency parse");
        System.out.println(dependencyParse);
        System.out.println();

        // constituency parse for the second sentence
        Tree constituencyParse = doc.sentences().get(0).constituencyParse();
        System.out.println("Example: constituency parse");
        System.out.println(constituencyParse);
        System.out.println();
    }

    private static void dosomething(CoreDocument document, String pattern) {

    }

    public static void getNounPhrasesWithMultipleKeywords(CoreDocument doc,
                                                          List<String> tags) {
        StringBuilder sb = new StringBuilder();
        boolean firstWord = true;

        for (int i = 0; i < tags.size(); i++) {
            String word = tags.get(i);
            String[] splitted = word.split(" ");
            for (String splitWord : splitted) {
                if (!firstWord) {
                    sb.append(" &");
                }
                sb.append(" << " + splitWord);
                firstWord = false;
            }

        }
        // sb.append(")");

        TregexPattern pattern = TregexPattern.compile("NP < (__" + sb.toString() + ")");
        System.out.println(pattern);
    }
}
