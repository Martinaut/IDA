package at.jku.dke.ida.data.repositories;

import at.jku.dke.ida.data.IRIValidator;
import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.GraphDbConnection;
import at.jku.dke.ida.data.models.DimensionLevelLabel;
import at.jku.dke.ida.data.repositories.base.CubeElementRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.eclipse.rdf4j.query.BindingSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Repository for querying level members.
 */
@Service
public class LevelMemberRepository extends CubeElementRepository<Triple<String, String, String>, DimensionLevelLabel> {

    /**
     * Instantiates a new instance of class {@linkplain LevelMemberRepository}.
     *
     * @param connection The GraphDB connection service class.
     */
    @Autowired
    public LevelMemberRepository(GraphDbConnection connection) {
        super(connection, "repo_levelmem", "level members");
    }

    @Override
    protected Set<Triple<String, String, String>> mapResultToType(Stream<BindingSet> stream) {
        return stream
                .map(x -> new ImmutableTriple<>(
                        x.getValue("dimension").stringValue(),
                        x.getValue("level").stringValue(),
                        x.getValue("element").stringValue()
                )).collect(Collectors.toSet());
    }

    @Override
    protected List<DimensionLevelLabel> mapResultToLabel(String lang, Stream<BindingSet> stream) {
        return stream
                .map(x -> RepositoryHelpers.convertToLevelLabel(lang, x))
                .collect(Collectors.toList());
    }

    /**
     * Returns all level members for the specified cube.
     *
     * @param cubeIri The absolute IRI of the cube.
     * @return Set with all level member IRIs of the specified cube. The key of the pair represents the dimension, the value is the level member.
     * @throws IllegalArgumentException If {@code cubeIri} is {@code null}, blank or an invalid IRI.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public Set<Triple<String, String, String>> getAllByCubeAndDimension(String cubeIri, String dimensionIri) throws QueryException {
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null nor empty");
        if (StringUtils.isBlank(dimensionIri))
            throw new IllegalArgumentException("dimensionIri must not be null nor empty");
        if (!IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");
        if (!IRIValidator.isValidAbsoluteIRI(dimensionIri))
            throw new IllegalArgumentException("dimensionIri must be an absolute IRI");

        logger.debug("Querying all level members of cube {} in dimension {}.", cubeIri, dimensionIri);
        return mapResultToType(connection.getQueryResult("/" + queryFolder + "/getAllByCubeAndDimension.sparql", s -> s.replaceAll("###CUBE###", cubeIri)
                .replaceAll("###DIMENSION###", dimensionIri))
                .stream());
    }

    /**
     * Returns all level members for the specified cube.
     *
     * @param cubeIri The absolute IRI of the cube.
     * @return Set with all level member IRIs of the specified cube. The key of the pair represents the dimension, the value is the level member.
     * @throws IllegalArgumentException If {@code cubeIri} is {@code null}, blank or an invalid IRI.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public Set<Triple<String, String, String>> getAllByCubeAndLevel(String cubeIri, String levelIri) throws QueryException {
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null nor empty");
        if (StringUtils.isBlank(levelIri))
            throw new IllegalArgumentException("levelIri must not be null nor empty");
        if (!IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");
        if (!IRIValidator.isValidAbsoluteIRI(levelIri))
            throw new IllegalArgumentException("levelIri must be an absolute IRI");

        logger.debug("Querying all level members of cube {} in level {}.", cubeIri, levelIri);
        return mapResultToType(connection.getQueryResult("/" + queryFolder + "/getAllByCubeAndLevel.sparql", s -> s.replaceAll("###CUBE###", cubeIri)
                .replaceAll("###LEVEL###", levelIri))
                .stream());
    }

    /**
     * Returns the labels of all level members of the specified level (without the specified level members).
     *
     * @param lang            The requested language.
     * @param levelIri        The absolute IRI of the level.
     * @param levelMemberIris The IRIs of the level members to exclude from the result.
     * @return List with level member labels of the dimension in the requested language
     * @throws IllegalArgumentException If {@code lang} or {@code levelIri} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<DimensionLevelLabel> getLabelsByLangAndLevel(String lang, String levelIri, Collection<String> levelMemberIris) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (StringUtils.isBlank(levelIri))
            throw new IllegalArgumentException("levelIri must not be null or empty");
        if (!IRIValidator.isValidAbsoluteIRI(levelIri))
            throw new IllegalArgumentException("levelIri must be an absolute IRI");
        if (levelMemberIris != null && levelMemberIris.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("levelmemberIris contains at least one invalid IRI");

        logger.debug("Querying labels of level members of level {} in language {} with exclusions {}.", levelIri, lang, levelMemberIris);

        return mapResultToLabel(lang, connection.getQueryResult(
                "/" + queryFolder + "/getLabelsByLangAndLevel.sparql",
                s -> s.replaceAll("###LANG###", lang)
                        .replaceAll("###LEVEL###", levelIri)
                        .replace("###NOTIN###", convertToFullIriString(levelMemberIris))
        ).stream());
    }

}
