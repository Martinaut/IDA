package at.jku.dke.ida.data.repositories.base;

import at.jku.dke.ida.data.GraphDbConnection;
import at.jku.dke.ida.data.models.Label;
import at.jku.dke.ida.data.repositories.RepositoryHelpers;
import org.eclipse.rdf4j.query.BindingSet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Repository for repositories querying simple cube elements (elements not belonging to a dimension).
 */
public abstract class SimpleCubeElementRepository extends CubeElementRepository<String, Label> {

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
    protected List<Label> mapResultToLabel(String lang, Stream<BindingSet> stream) {
        return stream
                .map(x -> RepositoryHelpers.convert(lang, x))
                .collect(Collectors.toList());
    }

}
