package at.jku.dke.inga.data.repositories;

import at.jku.dke.inga.data.QueryException;
import at.jku.dke.inga.data.configuration.GraphDbConnection;
import at.jku.dke.inga.data.models.Similarity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.model.Literal;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public List<Similarity> getTermSimilarity(String lang, String term) throws QueryException {
        if (StringUtils.isBlank(term)) throw new IllegalArgumentException("term must not be null or empty");
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (!lang.equals("en"))
            throw new UnsupportedOperationException("This method currently only supports english language.");

        logger.debug("Querying similarities for term '{}' in language {}.", term, lang);
        String queryFile = "/repo_nlp/similarity_single_en.sparql"; // TODO: adjust for language

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

    /**
     * Returns the cube elements whose labels are similar to the given term by splitting the term into single words.
     *
     * @param lang The language. (currently only english is supported)
     * @param term The search term.
     * @return The list with similar elements.
     * @throws IllegalArgumentException      If any of the parameters is {@code null} or blank.
     * @throws QueryException                If an exception occurred while executing the query.
     * @throws UnsupportedOperationException If the {@code lang} is not {@code en}.
     */
    public List<Similarity> getWordSimilarity(String lang, String term) throws QueryException {
        if (StringUtils.isBlank(term)) throw new IllegalArgumentException("term must not be null or empty");
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (!lang.equals("en"))
            throw new UnsupportedOperationException("This method currently only supports english language.");

        logger.debug("Querying similarities for words '{}' in language {}.", term, lang);
        final String queryFile = "/repo_nlp/similarity_multiple_en.sparql"; // TODO: adjust for language

        // Prepare query parts
        final String[] splitted = term.split(" ");
        String simParts = String.join(" ", getSimilarityParts(lang, splitted));
        String mappingParts = String.join(" ", getMappingParts(splitted));
        String filters = String.join(" ", getFilters(splitted));
        String wordnetSelect = IntStream.range(0, splitted.length).mapToObj(x -> "?w" + x).collect(Collectors.joining(" "));
        String wordnetScoreSelect = IntStream.range(0, splitted.length).mapToObj(x -> "?score" + x).collect(Collectors.joining(" * "));
        String mappingScoreSelect = IntStream.range(0, splitted.length).mapToObj(x -> "?scoreM" + x).collect(Collectors.joining(" * "));

        return connection.getQueryResult(queryFile, s -> s
                .replaceAll("###WORDNETS###", wordnetSelect)
                .replaceAll("###SCORE_SIM_MULT###", wordnetScoreSelect)
                .replaceAll("###SCORE_MAP_MULT###", mappingScoreSelect)
                .replaceAll("###PARTS_SIM###", simParts)
                .replaceAll("###PARTS_MAPPING###", mappingParts)
                .replaceAll("###FILTERS###", filters)
        )
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

    private String[] getSimilarityParts(String lang, String[] splitted) throws QueryException {
        String queryFile = "/repo_nlp/similarity_multiple_en_part_sim.sparql"; // TODO: adjust for language
        try {
            final String queryText = IOUtils.toString(GraphDbConnection.class.getResourceAsStream(queryFile), StandardCharsets.UTF_8);

            String[] queryParts = new String[splitted.length];
            for (int i = 0; i < splitted.length; i++) {
                queryParts[i] = queryText
                        .replaceAll("###NO###", Integer.toString(i))
                        .replaceAll("###TERM###", splitted[i]);
            }

            return queryParts;
        } catch (IOException ex) {
            logger.error("Could not load the query file " + queryFile, ex);
            throw new QueryException("Could not load query file " + queryFile, ex);
        }
    }

    private String[] getMappingParts(String[] splitted) throws QueryException {
        String queryFile = "/repo_nlp/similarity_multiple_part_mapping.sparql";
        try {
            final String queryText = IOUtils.toString(GraphDbConnection.class.getResourceAsStream(queryFile), StandardCharsets.UTF_8);

            String[] queryParts = new String[splitted.length];
            for (int i = 0; i < splitted.length; i++) {
                queryParts[i] = queryText.replaceAll("###NO###", Integer.toString(i));
            }

            return queryParts;
        } catch (IOException ex) {
            logger.error("Could not load the query file " + queryFile, ex);
            throw new QueryException("Could not load query file " + queryFile, ex);
        }
    }

    private String[] getFilters(String[] splitted) throws QueryException {
        String queryFile = "/repo_nlp/similarity_multiple_cube_filter.sparql";
        try {
            final String queryText = IOUtils.toString(GraphDbConnection.class.getResourceAsStream(queryFile), StandardCharsets.UTF_8);

            String[] queryParts = new String[splitted.length - 1];
            for (int i = 1; i < splitted.length; i++) {
                queryParts[i - 1] = queryText.replaceAll("###NO1###", Integer.toString(i - 1))
                        .replaceAll("###NO2###", Integer.toString(i));
            }

            return queryParts;
        } catch (IOException ex) {
            logger.error("Could not load the query file " + queryFile, ex);
            throw new QueryException("Could not load query file " + queryFile, ex);
        }
    }
}
