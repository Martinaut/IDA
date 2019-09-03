package at.jku.dke.ida.data.repositories.base;

import at.jku.dke.ida.data.GraphDbConnection;
import at.jku.dke.ida.data.IRIValidator;
import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.models.labels.CubeLabel;
import at.jku.dke.ida.data.repositories.RepositoryHelpers;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.query.BindingSet;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Repository for repositories querying simple cube elements (elements not belonging to a dimension).
 */
public abstract class SimpleCubeElementRepository extends CubeElementRepository<String, CubeLabel> {

    /**
     * Instantiates a new instance of class {@linkplain SimpleCubeElementRepository}.
     *
     * @param connection    The GraphDB connection service class.
     * @param typeIri       The IRI of the type.
     * @param queryFolder   The folder-name for the folder containing the query files of this repository.
     * @param pluralLogName The plural name of the type used for log-messages.
     * @throws IllegalArgumentException If {@code queryFolder} is {@code null} or empty.
     */
    public SimpleCubeElementRepository(GraphDbConnection connection, String typeIri, String queryFolder, String pluralLogName) {
        super(connection, typeIri, queryFolder, pluralLogName);
    }

    @Override
    protected Set<String> mapResultToType(Stream<BindingSet> stream) {
        return stream
                .map(x -> x.getValue("element").stringValue())
                .collect(Collectors.toSet());
    }

    @Override
    protected List<CubeLabel> mapResultToLabel(String lang, Stream<BindingSet> stream) {
        return stream
                .map(x -> RepositoryHelpers.convertToCubeLabel(lang, typeIri, x))
                .collect(Collectors.toList());
    }

    /**
     * Returns the labels of all elements of the type of this repository.
     *
     * @param lang The requested language.
     * @return List with elements labels in the requested language
     * @throws IllegalArgumentException If {@code lang} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<? extends CubeLabel> getAllLabelsByLang(String lang) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");

        logger.debug("Querying labels of language {} for levels.", lang);

        return mapResultToLabel(lang, connection.getQueryResult(
                "/repo_base/getAllLabelsByLang.sparql",
                s -> s.replaceAll("###LANG###", lang)
                        .replace("###TYPE###", typeIri)
        ).stream());
    }

    /**
     * Returns the labels of all elements of the type of this repository,
     * except those specified in the exclusions collection.
     *
     * @param lang      The requested language.
     * @param exclusion The collection with IRIs to exclude from the result.
     * @return List with elements labels of the cube in the requested language except the specified ones
     * @throws IllegalArgumentException If {@code lang} or {@code exclusion} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<? extends CubeLabel> getAllLabelsByLang(String lang, Collection<String> exclusion) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (exclusion == null) throw new IllegalArgumentException("excluded must not be null");
        if (exclusion.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("exclusion contains at least one invalid IRI");

        logger.debug("Querying labels of language {} for levels with exclusions.", lang);

        return mapResultToLabel(lang, connection.getQueryResult(
                "/repo_base/getAllLabelsByLangExcept.sparql",
                s -> s.replaceAll("###LANG###", lang)
                        .replace("###NOTIN###", convertToFullIriString(exclusion))
                        .replace("###TYPE###", typeIri)
        ).stream());
    }
}
