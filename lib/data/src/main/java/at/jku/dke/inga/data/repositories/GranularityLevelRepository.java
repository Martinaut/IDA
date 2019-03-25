package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.models.DimensionLabel;
import at.jku.dke.inga.shared.models.DimensionQualification;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.*;
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

    public Set<Pair<String, String>> getAllGranularityLevelRelationships(String cubeUri) {
        try {
            var result = graphDbHelper.getQueryResult(
                    "/queries/repo_gl/getAllLevels.sparql",
                    s -> s.replaceAll("###CUBE###", cubeUri));
            return result.stream().map(x ->
                    new ImmutablePair<>(
                            x.getValue("child").stringValue(),
                            x.getValue("parent").stringValue())
            ).collect(Collectors.toSet());
        } catch (QueryException ex) {
            return Collections.emptySet();
        }
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
            //noinspection Duplicates
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

    public List<DimensionLabel> findParentLevels(String cubeUri, String lang, Collection<DimensionQualification> dimensionQualifications) {
        try {
            List<DimensionLabel> list = new ArrayList<>(dimensionQualifications.size());

            for (DimensionQualification dq : dimensionQualifications) {
                //noinspection Duplicates
                graphDbHelper.getQueryResult(
                        "/queries/repo_gl/findParentLevel.sparql",
                        s -> s.replaceAll("###LANG###", lang).replaceAll("###CUBE###", cubeUri).replaceAll("###LEVEL###", dq.getGranularityLevel()))
                        .stream().map(x ->
                        new DimensionLabel(
                                lang,
                                x.getValue("dimension").stringValue(),
                                x.getValue("dimensionLabel").stringValue(),
                                x.getValue("level").stringValue(),
                                x.getValue("label").stringValue(),
                                x.hasBinding("description") ? x.getValue("description").stringValue() : null
                        )
                ).findFirst().ifPresent(list::add);
            }

            return list;
        } catch (QueryException ex) {
            return Collections.emptyList();
        }
    }

    public List<DimensionLabel> findChildLevels(String cubeUri, String lang, Collection<DimensionQualification> dimensionQualifications) {
        try {
            List<DimensionLabel> list = new ArrayList<>(dimensionQualifications.size());

            for (DimensionQualification dq : dimensionQualifications) {
                //noinspection Duplicates
                graphDbHelper.getQueryResult(
                        "/queries/repo_gl/findChildLevel.sparql",
                        s -> s.replaceAll("###LANG###", lang).replaceAll("###CUBE###", cubeUri).replaceAll("###LEVEL###", dq.getGranularityLevel()))
                        .stream().map(x ->
                        new DimensionLabel(
                                lang,
                                x.getValue("dimension").stringValue(),
                                x.getValue("dimensionLabel").stringValue(),
                                x.getValue("level").stringValue(),
                                x.getValue("label").stringValue(),
                                x.hasBinding("description") ? x.getValue("description").stringValue() : null
                        )
                ).findFirst().ifPresent(list::add);
            }

            return list;
        } catch (QueryException ex) {
            return Collections.emptyList();
        }
    }
}
