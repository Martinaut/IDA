package at.jku.dke.ida.data.repositories;

import at.jku.dke.ida.data.IRIValidator;
import at.jku.dke.ida.data.QueryException;
import at.jku.dke.ida.data.configuration.GraphDbConnection;
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

import java.util.HashSet;
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
        super(connection, "repo_levelpred", "level predicates");
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
    @SuppressWarnings("Duplicates")
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
}
