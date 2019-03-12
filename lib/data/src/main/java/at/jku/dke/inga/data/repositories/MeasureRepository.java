package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.models.Label;
import at.jku.dke.inga.shared.DefaultTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Repository for querying measures.
 */
@Service
public class MeasureRepository extends BaseRepository {

    /**
     * Instantiates a new instance of class {@linkplain MeasureRepository}.
     *
     * @param graphDbHelper A helper class for accessing the graph db.
     */
    @Autowired
    public MeasureRepository(GraphDbHelper graphDbHelper) {
        super(graphDbHelper);
    }

    /**
     * Returns all cube labels for the specified language.
     *
     * @param lang The requested language.
     * @return List with found measure labels of the language
     */
    public List<Label> findLabelsByCubeAndLangWithExclusions(String cubeUri, String lang, Set<String> excluded) {
        return findLabelsByLang(lang,
                "/queries/repo_measure/findLabelsByCubeAndLangWithExclusions.sparql",
                s -> s.replace("###CUBE###", cubeUri)
                        .replace("###NOTIN###", excluded == null || excluded.isEmpty() ?
                                "" :
                                excluded.stream().map(x -> '<' + x + '>').collect(Collectors.joining(", "))));
    }

    /**
     * Returns the labels for the specified measures.
     *
     * @param measureUris The URIs of the requested measures.
     * @param lang        The requested language.
     * @return List with labels of the language
     */
    public List<Label> findLabelsByUrisAndLang(Set<String> measureUris, String lang) {
        return findLabelsByLang(lang,
                "/queries/repo_measure/findLabelsByUrisAndLang.sparql",
                s -> s.replace("###IN###", measureUris.stream().map(x -> '<' + x + '>').collect(Collectors.joining(", "))));
    }

    /**
     * Returns all measures for the specified cube.
     *
     * @param cubeUri The full URI of the cube.
     * @return A set with all measure-URIs of the specified cube.
     */
    public Set<String> findByCube(String cubeUri) {
        return getAll(DefaultTypes.TYPE_MEASURE, "/queries/repo_measure/findByCube.sparql", s -> s.replace("###CUBE###", cubeUri));
    }
}
