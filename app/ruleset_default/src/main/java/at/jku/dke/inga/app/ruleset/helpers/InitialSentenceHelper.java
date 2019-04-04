package at.jku.dke.inga.app.ruleset.helpers;

import at.jku.dke.inga.app.ruleset.drools.InitialSentenceModel;
import at.jku.dke.inga.app.ruleset.models.WordGroup;
import at.jku.dke.inga.data.QueryException;
import at.jku.dke.inga.data.models.Similarity;
import at.jku.dke.inga.data.repositories.SimilarityRepository;
import at.jku.dke.inga.shared.spring.BeanUtil;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.trees.Constituent;
import edu.stanford.nlp.trees.LabeledScoredConstituentFactory;
import edu.stanford.nlp.trees.Tree;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Helper class containing methods used in "initial-sentence" rules.
 */
public final class InitialSentenceHelper {
    /**
     * Prevents creation of instances of this class.
     */
    private InitialSentenceHelper() {
    }

    /**
     * Returns all labels that have an exact match with a part of the initial sentence.
     *
     * @param model The model.
     * @return All exact matches
     * @see #hasExactMatches(InitialSentenceModel)
     */
    public static Collection<Similarity> exactMatches(InitialSentenceModel model) {
        return model.getAllLabels().stream()
                .filter(x -> model.getInitialSentence().contains(x.getLabel()))
                .map(x -> new Similarity(x.getLabel(), null, x.getUri(), 0.5))
                .collect(Collectors.toList());
    }

    /**
     * Returns {@code true}, if there is at least one exact match with a part of the initial sentence.
     *
     * @param model The model.
     * @return {@code true} if there is an exact match; {@code false} otherwise.
     * @see #exactMatches(InitialSentenceModel)
     */
    public static boolean hasExactMatches(InitialSentenceModel model) {
        return model.getAllLabels().stream()
                .anyMatch(x -> model.getInitialSentence().contains(x.getLabel()));
    }

    public static List<Similarity> getSimilarities(WordGroup wg) throws QueryException {
        return BeanUtil.getBean(SimilarityRepository.class).getSimilarity("en", wg.getText());
    }

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

    public static Set<WordGroup> getWordgroups(InitialSentenceModel model) {
        Set<WordGroup> groups = new HashSet<>();

        for (CoreSentence sentence : model.getAnnotatedText().sentences()) {
            Tree constituencyParse = sentence.constituencyParse();
            Set<Constituent> constituents = constituencyParse.constituents(new LabeledScoredConstituentFactory());

            for (Constituent constituent : constituents) {
                if (constituent.label() != null && (constituent.label().value().equals("VP") || constituent.label().value().equals("NP"))) {
                    List<Tree> parts = constituencyParse.getLeaves().subList(constituent.start(), constituent.end() + 1);

                    StringBuilder builder = new StringBuilder();
                    for (Tree tree : parts) {
                        String pos = ((CoreLabel) tree.label()).getString(CoreAnnotations.PartOfSpeechAnnotation.class);
                        if (!pos.equals("DT") && !pos.equals("IN") && !pos.equals("PRP")) {
                            if (builder.length() != 0) builder.append(' ');
                            builder.append(tree.value());
                        }
                    }

                    if (builder.length() > 0)
                        groups.add(new WordGroup(builder.toString()));
                }
            }
        }

        return groups;
    }
}
