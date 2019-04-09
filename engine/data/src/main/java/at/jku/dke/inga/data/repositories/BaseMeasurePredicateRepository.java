package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.IRIValidator;
import at.jku.dke.inga.data.QueryException;
import at.jku.dke.inga.data.configuration.GraphDbConnection;
import at.jku.dke.inga.data.models.Label;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Repository for querying base measure predicates.
 */
@Service
public class BaseMeasurePredicateRepository extends BaseRepository {

    /**
     * Instantiates a new instance of class {@linkplain BaseMeasurePredicateRepository}.
     *
     * @param connection The GraphDB connection service class.
     */
    @Autowired
    public BaseMeasurePredicateRepository(GraphDbConnection connection) {
        super(connection);
    }

    /**
     * Returns all base measure predicates for the specified cube.
     *
     * @param cubeIri The absolute IRI of the cube.
     * @return Set with all base measure predicate IRIs of the specified cube.
     * @throws IllegalArgumentException If {@code cubeIri} is {@code null}, blank or an invalid IRI.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public Set<String> getAllByCube(String cubeIri) throws QueryException {
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null nor empty");
        if (!IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");

        logger.debug("Querying all base measure predicates of cube {}.", cubeIri);
        return getAll("/repo_bmpred/getAllByCube.sparql", s -> s.replaceAll("###CUBE###", cubeIri));
    }

    /**
     * Returns the labels of the specified base measure predicates.
     *
     * @param lang        The requested language.
     * @param measureIris Collection with base measure predicate IRIs
     * @return List with aggregate measure labels of the language
     * @throws IllegalArgumentException If {@code lang} is {@code null} or blank or if {@code measureUris} is {@code null} or if the IRIs are not valid.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<Label> getLabelsByLangAndIris(String lang, Collection<String> measureIris) throws QueryException {
        if (measureIris == null) throw new IllegalArgumentException("measureIris must not be null");
        if (measureIris.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("measureIris contains at least one invalid IRI");

        logger.debug("Querying labels of base measure predicates {} in language {}.", measureIris, lang);
        return getLabelsByLang(
                "/repo_bmpred/getLabelsByLangAndUris.sparql",
                lang,
                s -> s.replace("###IN###", convertToFullIriString(measureIris)));
    }

    /**
     * Returns the labels of all base measure predicates of the specified cube.
     *
     * @param lang    The requested language.
     * @param cubeIri The absolute IRI of the cube.
     * @return List with aggregate measure labels of the cube in the requested language
     * @throws IllegalArgumentException If {@code lang} or {@code cubeIri} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<Label> getLabelsByLangAndCube(String lang, String cubeIri) throws QueryException {
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null or empty");
        if (!IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");

        logger.debug("Querying labels of base measure predicates of cube {} in language {}.", cubeIri, lang);
        return getLabelsByLang(
                "/repo_bmpred/getLabelsByLangAndCube.sparql",
                lang,
                s -> s.replace("###CUBE###", cubeIri));
    }

    /**
     * Returns the labels of all base measure predicates of the specified cube except those specified in {@code excluded}.
     *
     * @param lang     The requested language.
     * @param cubeIri  The absolute IRI of the cube.
     * @param excluded Collection with IRIs of measures to exclude from the result
     * @return List with aggregate measure labels of the cube in the requested language
     * @throws IllegalArgumentException If {@code lang} or {@code cubeIri} is {@code null} or or {@code excluded} is {@code null} or if the IRIs are not valid.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<Label> getLabelsByLangAndCubeWithExclusions(String lang, String cubeIri, Collection<String> excluded) throws QueryException {
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null or empty");
        if (excluded == null) throw new IllegalArgumentException("excluded must not be null");
        if (excluded.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("excluded contains at least one invalid IRI");
        if (!IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");

        logger.debug("Querying labels of base measure predicates of cube {} in language {} with measures {} excluded.", cubeIri, lang, excluded);
        return getLabelsByLang(
                "/repo_bmpred/getLabelsByLangAndCubeWithExclusions.sparql",
                lang,
                s -> s.replace("###CUBE###", cubeIri).replace("###NOTIN###", convertToFullIriString(excluded)));
    }
}