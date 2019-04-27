package at.jku.dke.ida.data.repositories;

import at.jku.dke.ida.data.IRIValidator;
import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.configuration.GraphDbConnection;
import at.jku.dke.ida.data.models.DimensionLevelLabel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Repository for querying level members.
 */
@Service
public class LevelMemberRepository extends BaseRepository {

    /**
     * Instantiates a new instance of class {@linkplain LevelMemberRepository}.
     *
     * @param connection The GraphDB connection service class.
     */
    @Autowired
    public LevelMemberRepository(GraphDbConnection connection) {
        super(connection);
    }

    /**
     * Returns all level members for the specified cube.
     *
     * @param cubeIri The absolute IRI of the cube.
     * @return Set with all level member IRIs of the specified cube. The key of the pair represents the dimension, the value is the level member.
     * @throws IllegalArgumentException If {@code cubeIri} is {@code null}, blank or an invalid IRI.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public Set<Triple<String, String, String>> getAllByCube(String cubeIri) throws QueryException {
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null nor empty");
        if (!IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");

        logger.debug("Querying all level members of cube {}.", cubeIri);
        return connection.getQueryResult("/repo_levelmem/getAllByCube.sparql", s -> s.replaceAll("###CUBE###", cubeIri))
                .stream()
                .map(x -> new ImmutableTriple<>(
                        x.getValue("dimension").stringValue(),
                        x.getValue("level").stringValue(),
                        x.getValue("element").stringValue()
                )).collect(Collectors.toSet());
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
        return connection.getQueryResult("/repo_levelmem/getAllByCubeAndDimension.sparql", s -> s.replaceAll("###CUBE###", cubeIri).replaceAll("###DIMENSION###", dimensionIri))
                .stream()
                .map(x -> new ImmutableTriple<>(
                        x.getValue("dimension").stringValue(),
                        x.getValue("level").stringValue(),
                        x.getValue("element").stringValue()
                )).collect(Collectors.toSet());
    }

    /**
     * Returns level members with the specified IRIs.
     *
     * @param iris The absolute IRIs to query.
     * @return Set with level member IRIs. The key of the pair represents the dimension, the value is the level member.
     * @throws IllegalArgumentException If {@code iris} is {@code null} or contains at least one invalid IRI.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public Set<Triple<String, String, String>> getByIri(Set<String> iris) throws QueryException {
        if (iris == null) throw new IllegalArgumentException("iris must not be null");
        if (iris.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("iris contains at least one invalid IRI");

        logger.debug("Querying level members {}.", iris);
        return connection.getQueryResult(
                "/repo_levelmem/getbyIris.sparql",
                s -> s.replaceAll("###IN###", iris.stream()
                        .map(x -> '(' + convertToFullIriString(x) + ')')
                        .collect(Collectors.joining(" "))))
                .stream()
                .map(x -> new ImmutableTriple<>(
                        x.getValue("dimension").stringValue(),
                        x.getValue("level").stringValue(),
                        x.getValue("element").stringValue()
                )).collect(Collectors.toSet());
    }

    /**
     * Returns the labels of all level members of the specified cube.
     *
     * @param lang    The requested language.
     * @param cubeIri The absolute IRI of the cube.
     * @return List with level member labels of the cube in the requested language
     * @throws IllegalArgumentException If {@code lang} or {@code cubeIri} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<DimensionLevelLabel> getLabelsByLangAndCube(String lang, String cubeIri) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null or empty");
        if (!IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");

        logger.debug("Querying labels of level members of cube {} in language {}.", cubeIri, lang);

        return connection.getQueryResult(
                "/repo_levelmem/getLabelsByLangAndCube.sparql",
                s -> s.replaceAll("###LANG###", lang).replaceAll("###CUBE###", cubeIri)
        )
                .stream()
                .map(x -> RepositoryHelpers.convertToLevelLabel(lang, x))
                .collect(Collectors.toList());
    }

    /**
     * Returns the labels of all level members of the specified dimensions (without the specified level members).
     *
     * @param lang            The requested language.
     * @param dimensionIri    The absolute IRI of the dimension.
     * @param levelMemberIris The IRIs of the level members to exclude from the result.
     * @return List with level member labels of the dimension in the requested language
     * @throws IllegalArgumentException If {@code lang} or {@code dimensionIri} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<DimensionLevelLabel> getLabelsByLangAndDimension(String lang, String dimensionIri, Collection<String> levelMemberIris) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (StringUtils.isBlank(dimensionIri))
            throw new IllegalArgumentException("dimensionIri must not be null or empty");
        if (!IRIValidator.isValidAbsoluteIRI(dimensionIri))
            throw new IllegalArgumentException("dimensionIri must be an absolute IRI");
        if (levelMemberIris != null && levelMemberIris.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("levelmemberIris contains at least one invalid IRI");

        logger.debug("Querying labels of level members of dimension {} in language {} with exclusions {}.", dimensionIri, lang, levelMemberIris);

        return connection.getQueryResult(
                "/repo_levelmem/getLabelsByLangAndDimension.sparql",
                s -> s.replaceAll("###LANG###", lang)
                        .replaceAll("###DIMENSION###", dimensionIri)
                        .replace("###NOTIN###", convertToFullIriString(levelMemberIris))
        )
                .stream()
                .map(x -> RepositoryHelpers.convertToLevelLabel(lang, x))
                .collect(Collectors.toList());
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

        return connection.getQueryResult(
                "/repo_levelmem/getLabelsByLangAndLevel.sparql",
                s -> s.replaceAll("###LANG###", lang)
                        .replaceAll("###LEVEL###", levelIri)
                        .replace("###NOTIN###", convertToFullIriString(levelMemberIris))
        )
                .stream()
                .map(x -> RepositoryHelpers.convertToLevelLabel(lang, x))
                .collect(Collectors.toList());
    }

    /**
     * Returns the labels of the specified level members.
     *
     * @param lang            The requested language.
     * @param levelmemberIris The IRIs of the level members to query the labels.
     * @return List with level member labels in the requested language
     * @throws IllegalArgumentException If {@code lang} or {@code dimensionIri} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<DimensionLevelLabel> getLabelsByLangAndIris(String lang, Collection<String> levelmemberIris) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (levelmemberIris == null) throw new IllegalArgumentException("levelmemberIris must not be null");
        if (levelmemberIris.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("levelmemberIris contains at least one invalid IRI");

        logger.debug("Querying labels of level members in language {} with for members {}.", lang, levelmemberIris);

        return connection.getQueryResult(
                "/repo_levelmem/getLabelsByLangAndIris.sparql",
                s -> s.replaceAll("###LANG###", lang)
                        .replace("###IN###", convertToFullIriString(levelmemberIris))
        )
                .stream()
                .map(x -> RepositoryHelpers.convertToLevelLabel(lang, x))
                .collect(Collectors.toList());
    }
}
