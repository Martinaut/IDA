package at.jku.dke.ida.data.repositories.base;

import at.jku.dke.ida.data.IRIValidator;
import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.configuration.GraphDbConnection;
import at.jku.dke.ida.data.models.Label;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Repository for repositories querying simple cube elements (elements not belonging to a dimension).
 */
public abstract class SimpleCubeElementRepository extends CubeElementRepository<String, Label> {

    /**
     * Instantiates a new instance of class {@linkplain SimpleCubeElementRepository}.
     *
     * @param connection    The GraphDB connection service class.
     * @param queryFolder   The folder-name for the folder containing the query files of this repository.
     * @param pluralLogName The plural name of the type used for log-messages.
     * @throws IllegalArgumentException If {@code queryFolder} is {@code null} or empty.
     */
    protected SimpleCubeElementRepository(GraphDbConnection connection, String queryFolder, String pluralLogName) {
        super(connection, queryFolder, pluralLogName);
    }

    /**
     * Returns all elements of the type of this repository for the specified cube,
     * except those specified in the exclusions collection.
     *
     * @param cubeIri   The absolute IRI of the cube.
     * @param exclusion The collection with IRIs to exclude from the result.
     * @return Set with all element IRIs except the specified ones
     * @throws IllegalArgumentException If {@code cubeIri} is {@code null}, blank or an invalid IRI.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    @Override
    public Set<String> getAllByCube(String cubeIri, Collection<String> exclusion) throws QueryException {
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null nor empty");
        if (!IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");
        final Collection<String> exclusions = Objects.requireNonNullElseGet(exclusion, Collections::emptyList);

        logger.debug("Querying all {} of cube {} with exclusions {}.", pluralLogName, cubeIri, exclusion);
        return getAll("/" + queryFolder + "/getAllByCube.sparql",
                s -> s.replaceAll("###CUBE###", cubeIri)
                        .replaceAll("###NOTIN###", convertToFullIriString(exclusions)));
    }

    /**
     * Returns the labels of the specified elements the type of this repository.
     *
     * @param lang        The requested language.
     * @param elementIris Collection with element IRIs
     * @return List with element labels in the requested language
     * @throws IllegalArgumentException If {@code lang} is {@code null} or blank or if {@code elementIris} is {@code null} or if the IRIs are not valid.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    @Override
    public List<Label> getLabelsByLangAndIris(String lang, Collection<String> elementIris) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (elementIris == null) throw new IllegalArgumentException("elementIris must not be null");
        if (elementIris.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("elementIris contains at least one invalid IRI");

        logger.debug("Querying labels of {} {} in language {}.", pluralLogName, elementIris, lang);
        return getLabelsByLang(
                "/" + queryFolder + "/getLabelsByLangAndIris.sparql",
                lang,
                s -> s.replaceAll("###IN###", convertToFullIriString(elementIris)));
    }

    /**
     * Returns the labels of all elements of the type of this repository of the specified cube,
     * except those specified in the exclusions collection.
     *
     * @param lang      The requested language.
     * @param cubeIri   The absolute IRI of the cube.
     * @param exclusion The collection with IRIs to exclude from the result.
     * @return List with elements labels of the cube in the requested language except the specified ones
     * @throws IllegalArgumentException If {@code lang} or {@code cubeIri} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    @Override
    public List<Label> getLabelsByLangAndCube(String lang, String cubeIri, Collection<String> exclusion) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null or empty");
        if (!IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");
        if (exclusion == null) throw new IllegalArgumentException("excluded must not be null");
        if (exclusion.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("exclusion contains at least one invalid IRI");

        logger.debug("Querying labels of {} of cube {} in language {} with exclusions {}.", pluralLogName, cubeIri, lang, exclusion);
        return getLabelsByLang(
                "/" + queryFolder + "/getLabelsByLangAndCube.sparql",
                lang,
                s -> s.replaceAll("###CUBE###", cubeIri)
                        .replaceAll("###NOTIN###", convertToFullIriString(exclusion)));
    }
}
