package at.jku.dke.inga.app.ruleset.helpers;

import at.jku.dke.inga.app.ruleset.models.WordGroup;
import at.jku.dke.inga.data.QueryException;
import at.jku.dke.inga.data.models.Similarity;
import at.jku.dke.inga.data.repositories.SimilarityRepository;
import at.jku.dke.inga.shared.spring.BeanUtil;

import java.util.List;

/**
 * Helper class containing methods used in "initial-sentence" rules.
 */
public final class SimilarityHelper {
    /**
     * Prevents creation of instances of this class.
     */
    private SimilarityHelper() {
    }

    /**
     * Gets english word similarities.
     *
     * @param wg The word group.
     * @return The word similarities.
     * @throws QueryException If an error occurred while executing the query.
     */
    public static List<Similarity> getWordSimilarities(WordGroup wg) throws QueryException {
        return BeanUtil.getBean(SimilarityRepository.class).getWordSimilarity("en", wg.getText());
    }
}
