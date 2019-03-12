package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.models.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Repository for querying cube labels.
 */
@Service
public class CubeRepository extends BaseRepository {

    /**
     * Instantiates a new instance of class {@linkplain CubeRepository}.
     *
     * @param graphDbHelper A helper class for accessing the graph db.
     */
    @Autowired
    public CubeRepository(GraphDbHelper graphDbHelper) {
        super(graphDbHelper);
    }

    /**
     * Returns all cube labels for the specified language.
     *
     * @param lang The requested language.
     * @return List with found cube labels of the language
     */
    public List<Label> findLabelsByLang(String lang) {
        return findLabelsByLang(lang, "/queries/repo_cube/findLabelsByLang.sparql");
    }
}
