package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.QueryException;
import at.jku.dke.inga.data.configuration.GraphDbConnection;
import at.jku.dke.inga.data.models.Similarity;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.model.Literal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository for querying similarities.
 */
@Service
public class SimilarityRepository extends BaseRepository {

    /**
     * Instantiates a new instance of class {@linkplain SimilarityRepository}.
     *
     * @param connection The GraphDB connection service class.
     */
    public SimilarityRepository(GraphDbConnection connection) {
        super(connection);
    }

    /**
     * Returns the cube elements whose labels are similar to the given term.
     *
     * @param lang The language. (currently only english is supported)
     * @param term The search term.
     * @return The list with similar elements.
     * @throws IllegalArgumentException      If any of the parameters is {@code null} or blank.
     * @throws QueryException                If an exception occurred while executing the query.
     * @throws UnsupportedOperationException If the {@code lang} is not {@code en}.
     */
    public List<Similarity> getSimilarity(String lang, String term) throws QueryException {
        if (StringUtils.isBlank(term)) throw new IllegalArgumentException("term must not be null or empty");
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (!lang.equals("en"))
            throw new UnsupportedOperationException("This method currently only supports english language.");

        logger.debug("Querying similarities for term '{}' in language {}.", term, lang);
        String queryFile = "/repo_nlp/similarity_en.sparql"; // TODO: adjust for language

        return connection.getQueryResult(queryFile, s -> s.replaceAll("###TERM###", term))
                .stream()
                .filter(x -> x.hasBinding("cube") && x.hasBinding("element") && x.hasBinding("score"))
                .map(x -> new Similarity(
                        term,
                        x.getValue("cube").stringValue(),
                        x.getValue("element").stringValue(),
                        ((Literal) x.getValue("score")).doubleValue()
                ))
                .collect(Collectors.toList());
    }
}
