package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.models.DimensionLabel;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository for querying granularity levels.
 */
@Service
public class GranularityLevelRepository {

    private final GraphDbHelper graphDbHelper;

    /**
     * Instantiates a new instance of class {@linkplain GranularityLevelRepository}.
     *
     * @param graphDbHelper A helper class for accessing the graph db.
     */
    public GranularityLevelRepository(GraphDbHelper graphDbHelper) {
        this.graphDbHelper = graphDbHelper;
    }

    /**
     * Returns all base levels of the specified cube.
     *
     * @param cubeUri The full cube URI.
     * @param lang    The requested language.
     * @return All base levels.
     */
    public List<DimensionLabel> findBaseLevelsByCubeAndLang(String cubeUri, String lang) {
        try {
            var result = graphDbHelper.getQueryResult(
                    "/queries/repo_gl/findBaseLevelsByCubeAndLang.sparql",
                    s -> s.replaceAll("###LANG###", lang).replaceAll("###CUBE###", cubeUri));
            return result.stream().map(x ->
                    new DimensionLabel(
                            lang,
                            x.getValue("dimension").stringValue(),
                            x.getValue("dimensionLabel").stringValue(),
                            x.getValue("level").stringValue(),
                            x.getValue("label").stringValue(),
                            x.hasBinding("description") ? x.getValue("description").stringValue() : null
                    )
            ).collect(Collectors.toList());
        } catch (QueryException ex) {
            return Collections.emptyList();
        }
    }

    public List<DimensionLabel> findParentLevels(String cubeUri, String levelUri, String lang) {
        return null;
    }
}
