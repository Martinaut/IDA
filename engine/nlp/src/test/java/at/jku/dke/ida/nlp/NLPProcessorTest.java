package at.jku.dke.ida.nlp;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.semgrex.SemgrexMatcher;
import edu.stanford.nlp.semgraph.semgrex.SemgrexPattern;
import edu.stanford.nlp.trees.Constituent;
import edu.stanford.nlp.trees.LabeledScoredConstituentFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
public class NLPProcessorTest {

    @Test
    void testAnnotate() {
//        String pattern = "NP < (/NN.?/=A [!$+ CC & !$+ /NN.?/ & !$- /NN.?/])";
//        TregexPattern.compile(pattern);
//        CoreDocument doc = NLPProcessor.annotate("en", "Show me the total costs per insurant and doctor district.");
//        System.out.println(doc.sentences().get(0).constituencyParse());
//        NLPProcessor.executeTregex(doc, pattern);


//        String pattern = "{}=A >/nmod.*/ {}=C :  {}=C >case {}=B";
//        SemgrexPattern.compile(pattern);
//        CoreDocument doc = NLPProcessor.annotate("en", "Add the measure sum of costs.");
//        System.out.println(doc.sentences().get(0).constituencyParse());
//        NLPProcessor.executeSemgrex(doc, pattern);

        //NLPProcessor.executeTregex(doc, "NP < /NN.?/=A");
        //NLPProcessor.executeTregex(doc, "/NN.?/=A $+ /NN.?/=B");
        //NLPProcessor.executeTregex(doc, "(NP < /NN.?/=A & !(NP < (/NN.?/ $++ /NN.?/)))");
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
        CoreDocument doc = NLPProcessor.annotate("en", "I am interested in the total costs per doctor district and insurant province.");
//        CoreDocument doc = NLPProcessor.annotate("en", "Add the measure sum of costs.");

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
    void testAnnotateEnglish2() {
        CoreDocument doc = NLPProcessor.annotate("en", "compare the change of costs from this year with the previous year.");

        // list of the part-of-speech tags for the second sentence
        List<String> posTags = doc.sentences().get(0).posTags();
        System.out.println("Example: pos tags");
        System.out.println(posTags);
        System.out.println();

        // Tregex
        SemgrexPattern semgrex = SemgrexPattern.compile("{tag:NN}=A");
        for (CoreSentence sentence : doc.sentences()) {
            SemgrexMatcher matcher = semgrex.matcher(sentence.dependencyParse());
            while (matcher.find()) {
                for (String nname : matcher.getNodeNames()) {
                    IndexedWord word = matcher.getNode(nname);
                    System.out.println(new StringJoiner(System.lineSeparator() + "\t")
                            .add("word=" + word.value())
                            .add("begin=" + word.beginPosition())
                            .add("end=" + word.endPosition())
                            .add("index=" + word.index())
                            .add("copyCount=" + word.copyCount())
                            .add("size=" + word.size())
                            .add("after=" + word.after())
                            .add("before=" + word.before())
                            .add("originalText=" + word.originalText())
                            .add("tag=" + word.tag())
                            .add("backingLabel=" + word.backingLabel())
                            .add("pseudoPosition=" + word.pseudoPosition()));
                }
            }
        }

        System.out.println("----------------------------------------");
        semgrex = SemgrexPattern.compile("{tag:NN}=A >nmod {tag:NN}=B");
        for (CoreSentence sentence : doc.sentences()) {
            SemgrexMatcher matcher = semgrex.matcher(sentence.dependencyParse());
            while (matcher.find()) {
                for (String nname : matcher.getNodeNames()) {
                    IndexedWord word = matcher.getNode(nname);
                    System.out.println(new StringJoiner(System.lineSeparator() + "\t")
                            .add("word=" + word.value())
                            .add("begin=" + word.beginPosition())
                            .add("end=" + word.endPosition())
                            .add("index=" + word.index())
                            .add("copyCount=" + word.copyCount())
                            .add("size=" + word.size())
                            .add("after=" + word.after())
                            .add("before=" + word.before())
                            .add("originalText=" + word.originalText())
                            .add("tag=" + word.tag())
                            .add("backingLabel=" + word.backingLabel())
                            .add("pseudoPosition=" + word.pseudoPosition()));
                }
            }
        }

        System.out.println("----------------------------------------");
        TregexPattern tregex = TregexPattern.compile("NP < /NN.?/=A");
        for (CoreSentence sentence : doc.sentences()) {
            TregexMatcher matcher = tregex.matcher(sentence.constituencyParse());
            while (matcher.find()) {
                for (String nname : matcher.getNodeNames()) {
                    Tree word = matcher.getNode(nname);
                    System.out.println(word);
                }
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
}
