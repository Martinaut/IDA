package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.IRIValidator;
import at.jku.dke.inga.data.QueryException;
import at.jku.dke.inga.data.configuration.GraphDbConnection;
import at.jku.dke.inga.data.models.DimensionLabel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Repository for querying level predicates.
 */
@Service
public class LevelPredicateRepository extends BaseRepository {

    /**
     * Instantiates a new instance of class {@linkplain LevelPredicateRepository}.
     *
     * @param connection The GraphDB connection service class.
     */
    @Autowired
    public LevelPredicateRepository(GraphDbConnection connection) {
        super(connection);
    }

    /**
     * Returns all level predicates for the specified cube.
     *
     * @param cubeIri The absolute IRI of the cube.
     * @return Set with all level predicate IRIs of the specified cube. The key of the pair represents the dimension, the value is the level predicate.
     * @throws IllegalArgumentException If {@code cubeIri} is {@code null}, blank or an invalid IRI.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public Set<Pair<String, String>> getAllByCube(String cubeIri) throws QueryException {
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null nor empty");
        if (!IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");

        logger.debug("Querying all level predicates of cube {}.", cubeIri);
        return connection.getQueryResult("/repo_levelpred/getAllByCube.sparql", s -> s.replaceAll("###CUBE###", cubeIri))
                .stream()
                .map(x -> new ImmutablePair<>(
                        x.getValue("dimension").stringValue(),
                        x.getValue("element").stringValue()
                )).collect(Collectors.toSet());
    }

    /**
     * Returns the labels of all level predicates of the specified cube.
     *
     * @param lang    The requested language.
     * @param cubeIri The absolute IRI of the cube.
     * @return List with level predicate labels of the cube in the requested language
     * @throws IllegalArgumentException If {@code lang} or {@code cubeIri} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<DimensionLabel> getLabelsByLangAndCube(String lang, String cubeIri) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null or empty");
        if (!IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");

        logger.debug("Querying labels of level predicates of cube {} in language {}.", cubeIri, lang);

        return connection.getQueryResult(
                "/repo_levelpred/getLabelsByLangAndCube.sparql",
                s -> s.replaceAll("###LANG###", lang).replaceAll("###CUBE###", cubeIri)
        )
                .stream()
                .map(x -> RepositoryHelpers.convert(lang, x))
                .collect(Collectors.toList());
    }

    /**
     * Returns the labels of all level predicates of the specified dimensions (without the specified level predicates).
     *
     * @param lang               The requested language.
     * @param dimensionIri       The absolute IRI of the dimension.
     * @param levelPredicateIris The IRIs of the level predicates to exclude from the result.
     * @return List with level predicate labels of the dimension in the requested language
     * @throws IllegalArgumentException If {@code lang} or {@code dimensionIri} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<DimensionLabel> getLabelsByLangAndDimension(String lang, String dimensionIri, Collection<String> levelPredicateIris) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (StringUtils.isBlank(dimensionIri))
            throw new IllegalArgumentException("dimensionIri must not be null or empty");
        if (!IRIValidator.isValidAbsoluteIRI(dimensionIri))
            throw new IllegalArgumentException("dimensionIri must be an absolute IRI");
        if (levelPredicateIris != null && levelPredicateIris.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("levelPredicateIris contains at least one invalid IRI");

        logger.debug("Querying labels of level predicates of dimension {} in language {} with exclusions {}.", dimensionIri, lang, levelPredicateIris);

        return connection.getQueryResult(
                "/repo_levelpred/getLabelsByLangAndDimension.sparql",
                s -> s.replaceAll("###LANG###", lang)
                        .replaceAll("###DIMENSION###", dimensionIri)
                        .replace("###NOTIN###", convertToFullIriString(levelPredicateIris))
        )
                .stream()
                .map(x -> RepositoryHelpers.convert(lang, x))
                .collect(Collectors.toList());
    }

    /**
     * Returns the labels of the specified level predicates.
     *
     * @param lang               The requested language.
     * @param levelPredicateIris The IRIs of the level predicates to query the labels.
     * @return List with level predicate labels in the requested language
     * @throws IllegalArgumentException If {@code lang} or {@code dimensionIri} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<DimensionLabel> getLabelsByLangAndIris(String lang, Collection<String> levelPredicateIris) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (levelPredicateIris == null) throw new IllegalArgumentException("levelPredicateIris must not be null");
        if (levelPredicateIris.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("levelPredicateIris contains at least one invalid IRI");

        logger.debug("Querying labels of level predicates in language {} with for predicates {}.", lang, levelPredicateIris);

        return connection.getQueryResult(
                "/repo_levelpred/getLabelsByLangAndIris.sparql",
                s -> s.replaceAll("###LANG###", lang)
                        .replace("###IN###", convertToFullIriString(levelPredicateIris))
        )
                .stream()
                .map(x -> RepositoryHelpers.convert(lang, x))
                .collect(Collectors.toList());
    }
}
