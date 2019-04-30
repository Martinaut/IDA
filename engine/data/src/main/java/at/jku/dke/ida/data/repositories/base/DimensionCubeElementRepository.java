package at.jku.dke.ida.data.repositories.base;

import at.jku.dke.ida.data.IRIValidator;
import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.configuration.GraphDbConnection;
import at.jku.dke.ida.data.models.DimensionLabel;
import at.jku.dke.ida.data.repositories.RepositoryHelpers;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.rdf4j.query.BindingSet;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Repository for repositories querying cube elements in dimensions.
 */
public abstract class DimensionCubeElementRepository extends CubeElementRepository<Pair<String, String>, DimensionLabel> {

    /**
     * Instantiates a new instance of class {@linkplain DimensionCubeElementRepository}.
     *
     * @param connection    The GraphDB connection service class.
     * @param queryFolder   The folder-name for the folder containing the query files of this repository.
     * @param pluralLogName The plural name of the type used for log-messages.
     * @throws IllegalArgumentException If {@code queryFolder} is {@code null} or empty.
     */
    protected DimensionCubeElementRepository(GraphDbConnection connection, String queryFolder, String pluralLogName) {
        super(connection, queryFolder, pluralLogName);
    }

    @Override
    protected Set<Pair<String, String>> mapResultToType(Stream<BindingSet> stream) {
        return stream
                .map(x -> new ImmutablePair<>(
                        x.getValue("dimension").stringValue(),
                        x.getValue("element").stringValue()
                ))
                .collect(Collectors.toSet());
    }

    @Override
    protected List<DimensionLabel> mapResultToLabel(String lang, Stream<BindingSet> stream) {
        return stream
                .map(x -> RepositoryHelpers.convertToDimensionLabel(lang, x))
                .collect(Collectors.toList());
    }

    /**
     * Returns elements with the specified IRIs.
     *
     * @param iris The absolute IRIs to query.
     * @return Set with element IRIs. The key of the pair represents the dimension, the value is the element.
     * @throws IllegalArgumentException If {@code iris} is {@code null} or contains at least one invalid IRI.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public Set<Pair<String, String>> getByIri(Set<String> iris) throws QueryException {
        if (iris == null) throw new IllegalArgumentException("iris must not be null");
        if (iris.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("iris contains at least one invalid IRI");

        logger.debug("Querying {}: {}.", pluralLogName, iris);
        return mapResultToType(connection.getQueryResult(
                "/" + queryFolder + "/getbyIris.sparql",
                s -> s.replaceAll("###IN###", iris.stream()
                        .map(x -> '(' + convertToFullIriString(x) + ')')
                        .collect(Collectors.joining(" ")))).stream());
    }


    /**
     * Returns the labels of all elements of the type of this repository of the specified dimensions,
     * except those specified in the exclusions collection.
     *
     * @param lang         The requested language.
     * @param dimensionIri The absolute IRI of the dimension.
     * @param exclusion    The collection with IRIs to exclude from the result.
     * @return List with element labels of the dimension in the requested language
     * @throws IllegalArgumentException If {@code lang} or {@code dimensionIri} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public List<DimensionLabel> getLabelsByLangAndDimension(String lang, String dimensionIri, Collection<String> exclusion) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (StringUtils.isBlank(dimensionIri))
            throw new IllegalArgumentException("dimensionIri must not be null or empty");
        if (!IRIValidator.isValidAbsoluteIRI(dimensionIri))
            throw new IllegalArgumentException("dimensionIri must be an absolute IRI");
        if (exclusion != null && exclusion.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("exclusion contains at least one invalid IRI");

        logger.debug("Querying labels of {} of dimension {} in language {} with exclusions {}.", pluralLogName, dimensionIri, lang, exclusion);
        return mapResultToLabel(lang, connection.getQueryResult(
                "/" + queryFolder + "/getLabelsByLangAndDimension.sparql",
                s -> s.replaceAll("###LANG###", lang)
                        .replaceAll("###DIMENSION###", dimensionIri)
                        .replaceAll("###NOTIN###", convertToFullIriString(exclusion))
        ).stream());
    }
}
