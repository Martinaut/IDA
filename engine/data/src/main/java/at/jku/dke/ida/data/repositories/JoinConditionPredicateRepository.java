package at.jku.dke.ida.data.repositories;

import at.jku.dke.ida.data.IRIValidator;
import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.configuration.GraphDbConnection;
import at.jku.dke.ida.data.models.Label;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Repository for querying join condition predicates.
 */
@Service
public class JoinConditionPredicateRepository extends BaseRepository {

    /**
     * Instantiates a new instance of class {@linkplain JoinConditionPredicateRepository}.
     *
     * @param connection The GraphDB connection service class.
     */
    @Autowired
    public JoinConditionPredicateRepository(GraphDbConnection connection) {
        super(connection);
    }

    /**
     * Returns all join condition predicates for the specified cube.
     *
     * @param cubeIri The absolute IRI of the cube.
     * @return Set with all join condition predicate IRIs of the specified cube.
     * @throws IllegalArgumentException If {@code cubeIri} is {@code null}, blank or an invalid IRI.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public Set<String> getAllByCube(String cubeIri) throws QueryException {
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null nor empty");
        if (!IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");

        logger.debug("Querying all join condition predicates of cube {}.", cubeIri);
        return getAll("/repo_joinpred/getAllByCube.sparql", s -> s.replaceAll("###CUBE###", cubeIri));
    }

    /**
     * Returns the labels of the specified join condition predicates.
     *
     * @param lang        The requested language.
     * @param predicateIris Collection with predicate IRIs
     * @return List with join condition predicate labels of the language
     * @throws IllegalArgumentException If {@code lang} is {@code null} or blank or if {@code predicateUris} is {@code null} or if the IRIs are not valid.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<Label> getLabelsByLangAndIris(String lang, Collection<String> predicateIris) throws QueryException {
        if (predicateIris == null) throw new IllegalArgumentException("predicateIris must not be null");
        if (predicateIris.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("predicateIris contains at least one invalid IRI");

        logger.debug("Querying labels of join condition predicates {} in language {}.", predicateIris, lang);
        return getLabelsByLang(
                "/repo_joinpred/getLabelsByLangAndUris.sparql",
                lang,
                s -> s.replace("###IN###", convertToFullIriString(predicateIris)));
    }

    /**
     * Returns the labels of all join condition predicates of the specified cube.
     *
     * @param lang    The requested language.
     * @param cubeIri The absolute IRI of the cube.
     * @return List with join condition predicate labels of the cube in the requested language
     * @throws IllegalArgumentException If {@code lang} or {@code cubeIri} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<Label> getLabelsByLangAndCube(String lang, String cubeIri) throws QueryException {
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null or empty");
        if (!IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");

        logger.debug("Querying labels of join condition predicates of cube {} in language {}.", cubeIri, lang);
        return getLabelsByLang(
                "/repo_joinpred/getLabelsByLangAndCube.sparql",
                lang,
                s -> s.replace("###CUBE###", cubeIri));
    }

    /**
     * Returns the labels of all join condition predicates of the specified cube except those specified in {@code excluded}.
     *
     * @param lang     The requested language.
     * @param cubeIri  The absolute IRI of the cube.
     * @param excluded Collection with IRIs of predicates to exclude from the result
     * @return List with join condition predicate labels of the cube in the requested language
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

        logger.debug("Querying labels of join condition predicates of cube {} in language {} with predicates {} excluded.", cubeIri, lang, excluded);
        return getLabelsByLang(
                "/repo_joinpred/getLabelsByLangAndCubeWithExclusions.sparql",
                lang,
                s -> s.replace("###CUBE###", cubeIri).replace("###NOTIN###", convertToFullIriString(excluded)));
    }
}
