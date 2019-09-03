package at.jku.dke.ida.data.repositories;

import at.jku.dke.ida.data.IRIValidator;
import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.GraphDbConnection;
import at.jku.dke.ida.data.models.similarity.CubeSimilarity;
import at.jku.dke.ida.data.repositories.base.BaseRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.rdf4j.query.BindingSet;
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

    // region --- getTermSimilarity ---

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
    public List<CubeSimilarity> getTermSimilarity(String lang, String term) throws QueryException {
        if (!lang.equals("en"))
            throw new UnsupportedOperationException("This method currently only supports english language.");

        logger.debug("Querying similarities for term '{}' in language {}.", term, lang);
        String queryFile = "/repo_nlp/similarity_single_" + lang + ".sparql";

        return getTermSimilarity(queryFile, lang, term, null);
    }

    /**
     * Returns the cube elements whose labels are similar to the given term.
     *
     * @param lang    The language. (currently only english is supported)
     * @param term    The search term.
     * @param cubeIri The full IRI of the cube.
     * @return The list with similar elements.
     * @throws IllegalArgumentException      If any of the parameters is {@code null} or blank.
     * @throws QueryException                If an exception occurred while executing the query.
     * @throws UnsupportedOperationException If the {@code lang} is not {@code en}.
     */
    public List<CubeSimilarity> getTermSimilarity(String lang, String term, String cubeIri) throws QueryException {
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null nor empty");
        if (!lang.equals("en"))
            throw new UnsupportedOperationException("This method currently only supports english language.");

        logger.debug("Querying similarities for term '{}' in cube {} in language {}.", term, cubeIri, lang);
        String queryFile = "/repo_nlp/cube_similarity_single_" + lang + ".sparql";

        return getTermSimilarity(queryFile, lang, term, cubeIri);
    }

    private List<CubeSimilarity> getTermSimilarity(String queryFile, String lang, String term, String cubeIri) throws QueryException {
        if (StringUtils.isBlank(term)) throw new IllegalArgumentException("term must not be null or empty");
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (cubeIri != null && !IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");

        return connection.getQueryResult(queryFile, s -> {
            s = s.replaceAll("###TERM###", term);
            if (cubeIri != null) {
                s = s.replaceAll("###CUBE###", cubeIri);
            }
            return s;
        })
                .stream()
                .filter(x -> x.hasBinding("cube") && x.hasBinding("element") && x.hasBinding("score"))
                .map(x -> convert(term, x))
                .collect(Collectors.toList());
    }
    // endregion

    // region --- getWordSimilarity ---

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
    public List<CubeSimilarity> getWordSimilarity(String lang, String term) throws QueryException {
        logger.debug("Querying similarities for words '{}' in language {}.", term, lang);
        return getWordSimilarityInternal(lang, term, null);
    }

    /**
     * Returns the cube elements whose labels are similar to the given term by splitting the term into single words.
     *
     * @param lang    The language. (currently only english is supported)
     * @param term    The search term.
     * @param cubeIri The full IRI of the cube.
     * @return The list with similar elements.
     * @throws IllegalArgumentException      If any of the parameters is {@code null} or blank.
     * @throws QueryException                If an exception occurred while executing the query.
     * @throws UnsupportedOperationException If the {@code lang} is not {@code en}.
     */
    public List<CubeSimilarity> getWordSimilarity(String lang, String term, String cubeIri) throws QueryException {
        logger.debug("Querying similarities for words '{}' for cube {} in language {}.", term, cubeIri, lang);
        return getWordSimilarityInternal(lang, term, cubeIri);
    }

    private List<CubeSimilarity> getWordSimilarityInternal(String lang, String term, String cubeIri) throws QueryException {
        if (StringUtils.isBlank(term)) throw new IllegalArgumentException("term must not be null or empty");
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (cubeIri != null && !IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");

        final String queryFile = "/repo_nlp/similarity_multiple_" + lang + ".sparql";

        // Prepare query parts
        final String[] splitted = term.split(" ");
        String simParts = String.join(" ", getSimilarityParts(lang, splitted));
        String mappingParts = String.join(" ", getMappingParts(splitted, cubeIri));
        String wordnetSelect = IntStream.range(0, splitted.length).mapToObj(x -> "?w" + x).collect(Collectors.joining(" "));
        String wordnetScoreSelect = IntStream.range(0, splitted.length).mapToObj(x -> "?score" + x).collect(Collectors.joining(" * "));
        String mappingScoreSelect = IntStream.range(0, splitted.length).mapToObj(x -> "?scoreM" + x).collect(Collectors.joining(" * "));

        return connection.getQueryResult(queryFile, s -> s
                .replaceAll("###WORDNETS###", wordnetSelect)
                .replaceAll("###SCORE_SIM_MULT###", wordnetScoreSelect)
                .replaceAll("###SCORE_MAP_MULT###", mappingScoreSelect)
                .replaceAll("###PARTS_SIM###", simParts)
                .replaceAll("###PARTS_MAPPING###", mappingParts)
        )
                .stream()
                .filter(x -> x.hasBinding("cube") && x.hasBinding("element") && x.hasBinding("score"))
                .map(x -> convert(term, x))
                .collect(Collectors.toList());
    }

    private String[] getSimilarityParts(String lang, String[] splitted) throws QueryException {
        String queryFile = "/repo_nlp/similarity_multiple_" + lang + "_part_sim.sparql";
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

    private String[] getMappingParts(String[] splitted, String cube) throws QueryException {
        String queryFile = cube == null ?
                "/repo_nlp/similarity_multiple_part_mapping.sparql" :
                "/repo_nlp/similarity_multiple_part_mapping_with_filter.sparql";
        try {
            final String queryText = IOUtils.toString(GraphDbConnection.class.getResourceAsStream(queryFile), StandardCharsets.UTF_8);

            String[] queryParts = new String[splitted.length];
            for (int i = 0; i < splitted.length; i++) {
                queryParts[i] = queryText.replaceAll("###NO###", Integer.toString(i));
                if (cube != null)
                    queryParts[i] = queryParts[i].replaceAll("###CUBE###", cube);
            }

            return queryParts;
        } catch (IOException ex) {
            logger.error("Could not load the query file " + queryFile, ex);
            throw new QueryException("Could not load query file " + queryFile, ex);
        }
    }
    // endregion

    private CubeSimilarity convert(String term, BindingSet set) {
        if (set.hasBinding("part")) {
            if (set.hasBinding("measure")) return RepositoryHelpers.convertToComparativeMeasureSimilarity(term, set);
            return RepositoryHelpers.convertToComparativeSimilarity(term, set);
        }
        if (set.hasBinding("dimension")) {
            if (set.hasBinding("level")) return RepositoryHelpers.convertToLevelSimilarity(term, set);
            return RepositoryHelpers.convertToDimSimilarity(term, set);
        }
        return RepositoryHelpers.convertToSimilarity(term, set);
    }
}
