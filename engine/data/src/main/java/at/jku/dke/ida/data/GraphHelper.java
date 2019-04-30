package at.jku.dke.ida.data;

import com.google.common.graph.Graph;

import java.util.HashSet;
import java.util.Set;

/**
 * Contains helper methods for accessing graphs.
 */
public final class GraphHelper {
    /**
     * Prevents creation of instances of this class.
     */
    private GraphHelper() {
    }

    /**
     * Returns all successors of the specified node.
     *
     * @param graph The graph.
     * @param node  The node for which to get the successors.
     * @param <T>   The type of the node.
     * @return Set with all successors.
     */
    public static <T> Set<T> getAllSuccessors(Graph<T> graph, T node) {
        Set<T> succ = new HashSet<>(graph.successors(node));

        Set<T> toAdd = new HashSet<>();
        for (T s : succ) {
            toAdd.addAll(getAllSuccessors(graph, s));
        }
        succ.addAll(toAdd);

        return succ;
    }

    /**
     * Returns all predecessors of the specified node.
     *
     * @param graph The graph.
     * @param node  The node for which to get the predecessors.
     * @param <T>   The type of the node.
     * @return Set with all predecessors.
     */
    public static <T> Set<T> getAllPredecessors(Graph<T> graph, T node) {
        Set<T> succ = new HashSet<>(graph.predecessors(node));

        Set<T> toAdd = new HashSet<>();
        for (T s : succ) {
            toAdd.addAll(getAllPredecessors(graph, s));
        }
        succ.addAll(toAdd);

        return succ;
    }
}
