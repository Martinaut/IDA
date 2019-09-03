package at.jku.dke.ida.data.repositories;

import at.jku.dke.ida.data.IRIValidator;
import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.GraphDbConnection;
import at.jku.dke.ida.data.models.labels.DimensionLabel;
import at.jku.dke.ida.data.repositories.base.DimensionCubeElementRepository;
import at.jku.dke.ida.shared.IRIConstants;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Repository for querying level predicates.
 */
@Service
public class LevelPredicateRepository extends DimensionCubeElementRepository {

    /**
     * Instantiates a new instance of class {@linkplain LevelPredicateRepository}.
     *
     * @param connection The GraphDB connection service class.
     */
    @Autowired
    public LevelPredicateRepository(GraphDbConnection connection) {
        super(connection, IRIConstants.TYPE_LEVEL_PREDICATE, "repo_levelpred", "level predicates");
    }

    /**
     * Returns all level predicate relationships for the specified cube.
     * <p>
     * The first entry of the triple is the dimension, the second one the child and the third one the parent.
     *
     * @param cubeIri The absolute IRI of the cube.
     * @return Set with all level predicate relationships of the specified cube.
     * @throws IllegalArgumentException If {@code cubeIri} is {@code null}, blank or an invalid IRI.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public Set<Triple<String, String, String>> getAllRelationshipsByCube(String cubeIri) throws QueryException {
        if (StringUtils.isBlank(cubeIri)) throw new IllegalArgumentException("cubeIri must not be null nor empty");
        if (!IRIValidator.isValidAbsoluteIRI(cubeIri))
            throw new IllegalArgumentException("cubeIri must be an absolute IRI");

        logger.debug("Querying all level predicate relationships of cube {}.", cubeIri);
        return connection.getQueryResult("/" + queryFolder + "/getAllRelationshipsByCube.sparql", s -> s.replaceAll("###CUBE###", cubeIri))
                .stream()
                .map(x -> new ImmutableTriple<>(
                        x.getValue("dimension").stringValue(),
                        x.getValue("child").stringValue(),
                        x.hasBinding("parent") ? x.getValue("parent").stringValue() : null
                )).collect(Collectors.toSet());
    }

    /**
     * Gets the dependency graph of level predicates for the specified cube.
     *
     * @param cubeIri The absolute IRI of the cube.
     * @return The dependency graph.
     * @throws IllegalArgumentException If {@code cubeIri} is {@code null}, blank or an invalid IRI.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    public Graph<String> getDependencyGraph(String cubeIri) throws QueryException {
        var deps = getAllRelationshipsByCube(cubeIri);

        MutableGraph<String> graph = GraphBuilder.directed().build();

        for (Triple<String, String, String> pair : deps) {
            graph.addNode(pair.getMiddle());
            if (pair.getRight() != null) {
                graph.addNode(pair.getRight());
                graph.putEdge(pair.getMiddle(), pair.getRight());
            }
        }

        // Add top level
        graph.addNode(IRIConstants.RESOURCE_TOP_LEVEL);
        Set<String> nodes = new HashSet<>(graph.nodes());
        for (String node : nodes) {
            if (!node.equals(IRIConstants.RESOURCE_TOP_LEVEL) && graph.inDegree(node) == 0) {
                graph.putEdge(IRIConstants.RESOURCE_TOP_LEVEL, node);
            }
        }

        return ImmutableGraph.copyOf(graph);
    }

    /**
     * Returns the labels of all elements of the type of this repository.
     *
     * @param lang The requested language.
     * @return List with elements labels in the requested language
     * @throws IllegalArgumentException If {@code lang} is {@code null} or blank.
     * @throws QueryException           If an exception occurred while executing the query.
     */
    @Override
    public List<DimensionLabel> getAllLabelsByLang(String lang) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");

        logger.debug("Querying labels of language {} for levels.", lang);

        return mapResultToLabel(lang, connection.getQueryResult(
                "/repo_level/getAllLabelsByLang.sparql",
                s -> s.replaceAll("###LANG###", lang)
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
    @Override
    public List<DimensionLabel> getAllLabelsByLang(String lang, Collection<String> exclusion) throws QueryException {
        if (StringUtils.isBlank(lang)) throw new IllegalArgumentException("lang must not be null or empty");
        if (exclusion == null) throw new IllegalArgumentException("excluded must not be null");
        if (exclusion.stream().map(IRIValidator::isValidAbsoluteIRI).anyMatch(x -> !x))
            throw new IllegalArgumentException("exclusion contains at least one invalid IRI");

        logger.debug("Querying labels of language {} for levels with exclusions.", lang);

        return mapResultToLabel(lang, connection.getQueryResult(
                "/repo_level/getAllLabelsByLangExcept.sparql",
                s -> s.replaceAll("###LANG###", lang).replace("###NOTIN###", convertToFullIriString(exclusion))
        ).stream());
    }
}
