package at.jku.dke.ida.data.repositories.base;

import at.jku.dke.ida.data.IRIValidator;
import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.GraphDbConnection;
import at.jku.dke.ida.data.models.Label;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.query.BindingSet;

import java.util.*;
import java.util.stream.Stream;

/**
 * Base repository for all repositories querying cube elements.
 *
 * @param <TReturn> The type of the return type for the getAll methods.
 * @param <TLabel>  The type of the label returned by the query methods.
 */
public abstract class CubeElementRepository<TReturn, TLabel extends Label> extends BaseRepository {

    /**
     * The folder-name for the folder containing the query files of this repository.
     */
    protected final String queryFolder;

    /**
     * The plural name of the type used for log-messages.
     */
    protected final String pluralLogName;

    /**
     * Instantiates a new instance of class {@linkplain CubeElementRepository}.
     *
     * @param connection    The GraphDB connection service class.
     * @param queryFolder   The folder-name for the folder containing the query files of this repository.
     * @param pluralLogName The plural name of the type used for log-messages.
     * @throws IllegalArgumentException If {@code queryFolder} is {@code null} or empty.
     */
    protected CubeElementRepository(GraphDbConnection connection, String queryFolder, String pluralLogName) {
        super(connection);
        if (StringUtils.isBlank(queryFolder))
            throw new IllegalArgumentException("queryFolder must not be null or empty");

        this.queryFolder = queryFolder;
        this.pluralLogName = pluralLogName;
    }


    // region --- ALL BY CUBE ---

    /**
     * Returns all elements of the type of this repository for the specified cube.
     *
     * @param cubeIri The absolute IRI of the cube.
     * @return Set with all element IRIs of the specified cube.
     * @throws IllegalArgumentException If {@code cubeIri} is {@code null}, blank or an invalid IRI.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public Set<TReturn> getAllByCube(String cubeIri) throws QueryException {
        return getAllByCube(cubeIri, Collections.emptyList());
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
    public Set<TReturn> getAllByCube(String cubeIri, Collection<String> exclusion) throws QueryException {
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null nor empty");
        if (!IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");
        final Collection<String> exclusions = Objects.requireNonNullElseGet(exclusion, Collections::emptyList);
        if (exclusions != null && exclusions.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("exclusion contains at least one invalid IRI");

        logger.debug("Querying all {} of cube {} with exclusions {}.", pluralLogName, cubeIri, exclusion);
        return mapResultToType(connection.getQueryResult(
                "/" + queryFolder + "/getAllByCube.sparql",
                s -> s.replaceAll("###CUBE###", cubeIri)
                        .replaceAll("###NOTIN###", convertToFullIriString(exclusions))).stream());
    }
    // endregion

    // region --- LABELS BY IRI ---

    /**
     * Returns the labels of the specified elements the type of this repository.
     *
     * @param lang        The requested language.
     * @param elementIris Collection with element IRIs
     * @return List with element labels in the requested language
     * @throws IllegalArgumentException If {@code lang} is {@code null} or blank or if {@code elementIris} is {@code null} or if the IRIs are not valid.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<TLabel> getLabelsByLangAndIris(String lang, Collection<String> elementIris) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (elementIris == null) throw new IllegalArgumentException("elementIris must not be null");
        if (elementIris.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("elementIris contains at least one invalid IRI");

        logger.debug("Querying labels of {} {} in language {}.", pluralLogName, elementIris, lang);
        return mapResultToLabel(lang, connection.getQueryResult(
                "/" + queryFolder + "/getLabelsByLangAndIris.sparql",
                s -> s.replaceAll("###IN###", convertToFullIriString(elementIris))
                        .replaceAll("###LANG###", lang)).stream());
    }
    // endregion

    // region --- LABELS BY CUBE ---

    /**
     * Returns the labels of all elements of the type of this repository of the specified cube.
     *
     * @param lang    The requested language.
     * @param cubeIri The absolute IRI of the cube.
     * @return List with elements labels of the cube in the requested language
     * @throws IllegalArgumentException If {@code lang} or {@code cubeIri} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<TLabel> getLabelsByLangAndCube(String lang, String cubeIri) throws QueryException {
        return getLabelsByLangAndCube(lang, cubeIri, Collections.emptyList());
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
    public List<TLabel> getLabelsByLangAndCube(String lang, String cubeIri, Collection<String> exclusion) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null or empty");
        if (!IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");
        if (exclusion == null) throw new IllegalArgumentException("excluded must not be null");
        if (exclusion.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("exclusion contains at least one invalid IRI");

        logger.debug("Querying labels of {} of cube {} in language {} with exclusions {}.", pluralLogName, cubeIri, lang, exclusion);
        return mapResultToLabel(lang, connection.getQueryResult(
                "/" + queryFolder + "/getLabelsByLangAndCube.sparql",
                s -> s.replaceAll("###NOTIN###", convertToFullIriString(exclusion))
                        .replaceAll("###CUBE###", cubeIri)
                        .replaceAll("###LANG###", lang)).stream());
    }
    // endregion

    // region --- MAPPING ---

    /**
     * Maps the BindingSet result stream from the TReturn get.. methods to the return type.
     *
     * @param stream The stream with the bindings from the query result.
     * @return Mapped binding sets
     */
    protected abstract Set<TReturn> mapResultToType(Stream<BindingSet> stream);

    /**
     * Maps the BindingSet result stream from the TReturn get.. methods to the return type.
     *
     * @param lang   The language.
     * @param stream The stream with the bindings from the query result.
     * @return Mapped binding sets
     */
    protected abstract List<TLabel> mapResultToLabel(String lang, Stream<BindingSet> stream);
    // endregion
}


