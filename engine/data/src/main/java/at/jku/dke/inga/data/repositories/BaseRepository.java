package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.IRIValidator;
import at.jku.dke.inga.data.QueryException;
import at.jku.dke.inga.data.configuration.GraphDbConnection;
import at.jku.dke.inga.data.models.Label;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Base class for repositories containing some helper methods.
 */
public abstract class BaseRepository {

    /**
     * The graph db connection.
     */
    protected final GraphDbConnection connection;

    /**
     * The logger of the current class.
     */
    protected final Logger logger;

    /**
     * Instantiates a new instance of class {@linkplain BaseRepository}.
     *
     * @param connection The GraphDB connection service class.
     */
    public BaseRepository(GraphDbConnection connection) {
        this.logger = LogManager.getLogger(getClass());
        this.connection = connection;
    }

    /**
     * Converts the collection of strings to a comma-separated IRI-string.
     * <p>
     * Invalid IRIs are skipped.
     *
     * @param iris The collection with IRIs.
     * @return The result has the format {@code (<http://a.com/>, <http://b.com/>)}. If {@code iris} is {@code null}, an empty String will be returned.
     */
    protected String convertToFullIriString(Collection<String> iris) {
        if (iris == null) return "";
        return iris.stream()
                .filter(IRIValidator::isValidAbsoluteIRI)
                .map(this::convertToFullIriString)
                .collect(Collectors.joining(", "));
    }

    /**
     * Converts IRI to a full IRI String format to be used in the queries.
     *
     * @param iri The IRI to convert.
     * @return The result has the format {@code (<http://a.com/>)}. If {@code iri} is {@code null} or invalid, an empty String will be returned.
     */
    protected String convertToFullIriString(String iri) {
        if (iri == null) return "";
        if (!IRIValidator.isValidAbsoluteIRI(iri)) return "";
        return '<' + iri + '>';
    }

    // region --- getLabelsByLang ---

    /**
     * Returns all labels returned by the query.
     * <p>
     * Following bindings have to be returned by the query:
     * <ul>
     * <li>element</li>
     * <li>label</li>
     * <li>description (optional)</li>
     * </ul>
     *
     * @param queryFile The path and name to the query file.
     * @param lang      The requested language.
     * @return List with found labels in the requested language
     * @throws IllegalArgumentException If {@code queryFile} or {@code lang} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query (e.g. query file not found).
     */
    protected List<Label> getLabelsByLang(String queryFile, String lang) throws QueryException {
        return getLabelsByLang(queryFile, lang, Function.identity());
    }

    /**
     * Returns all labels returned by the query.
     * <p>
     * Following bindings have to be returned by the query:
     * <ul>
     * <li>element</li>
     * <li>label</li>
     * <li>description (optional)</li>
     * </ul>
     *
     * @param queryFile                        The path and name to the query file.
     * @param lang                             The requested language.
     * @param additionalQueryStringManipulator Additional query string manipulator to manipulate the query
     *                                         text before executing (may be {@code null}).
     * @return List with found labels in the requested language
     * @throws IllegalArgumentException If {@code queryFile} or {@code lang} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query (e.g. query file not found).
     */
    protected List<Label> getLabelsByLang(String queryFile, String lang, Function<String, String> additionalQueryStringManipulator) throws QueryException {
        if (StringUtils.isBlank(queryFile)) throw new IllegalArgumentException("queryFile must not be null or empty");
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");

        logger.debug("Querying labels of language {} using query file {}.", lang, queryFile);
        final Function<String, String> manipulator = Objects.requireNonNullElse(additionalQueryStringManipulator, Function.identity());

        return connection.getQueryResult(
                queryFile,
                s -> manipulator.apply(s.replaceAll("###LANG###", lang))
        )
                .stream()
                .map(x ->
                        new Label(
                                x.getValue("element").stringValue(),
                                lang,
                                x.getValue("label").stringValue(),
                                x.hasBinding("description") ? x.getValue("description").stringValue() : null
                        )
                ).collect(Collectors.toList());
    }
    // endregion

    // region --- getAll ---

    /**
     * Returns all resources returned by the query.
     * <p>
     * Following bindings have to be returned by the query:
     * <ul>
     * <li>element</li>
     * </ul>
     *
     * <p>
     * The query should have placeholder <b>###TYPE###</b> for the specified type.
     *
     * @param queryFile The path and name to the query file.
     * @param type      The absolute IRI of the requested type.
     * @return Set with all resource IRIs of the requested type returned by the query
     * @throws IllegalArgumentException If {@code queryFile} or {@code type} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query (e.g. query file not found).
     */
    protected Set<String> getAll(String queryFile, String type) throws QueryException {
        if (StringUtils.isBlank(type)) throw new IllegalArgumentException("type must not be null or empty");
        if (!IRIValidator.isValidAbsoluteIRI(type)) throw new IllegalArgumentException("type must be an absolute IRI");

        logger.debug("Querying all resources of type {} using query file {}.", type, queryFile);
        return getAll(queryFile, s -> s.replaceAll("###TYPE###", type));
    }

    /**
     * Returns all resources returned by the query.
     * <p>
     * Following bindings have to be returned by the query:
     * <ul>
     * <li>element</li>
     * </ul>
     *
     * @param queryFile                        The path and name to the query file.
     * @param additionalQueryStringManipulator Additional query string manipulator to manipulate the query
     *                                         text before executing (may be {@code null}).
     * @return Set with all resource IRIs returned by the query
     * @throws IllegalArgumentException If {@code queryFile} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query (e.g. query file not found).
     */
    protected Set<String> getAll(String queryFile, Function<String, String> additionalQueryStringManipulator) throws QueryException {
        if (StringUtils.isBlank(queryFile)) throw new IllegalArgumentException("queryFile must not be null or empty");

        logger.debug("Querying all resources using query file {}.", queryFile);

        return connection.getQueryResult(
                queryFile,
                Objects.requireNonNullElse(additionalQueryStringManipulator, Function.identity())
        )
                .stream()
                .map(x -> x.getValue("element").stringValue())
                .collect(Collectors.toSet());
    }
    // endregion
}
