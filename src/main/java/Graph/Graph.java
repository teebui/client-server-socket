package Graph;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import java.util.stream.Collectors;

/**
 * Serves as a wrapper class for the Jgrapht library. This class performs different operations on a graph instance
 * As a graph object is highly likely shared by different threads, every operation is thread-safe
 */
public class  Graph {

    // The DirectedWeightedPseudograph allows loops, multiple edges
    private final DirectedWeightedPseudograph<String, DefaultWeightedEdge> graph;

    public Graph() {
        graph = new DirectedWeightedPseudograph<>(DefaultWeightedEdge.class);
    }

    public synchronized void addNode(final String nodeName) {
        if (!graph.addVertex(nodeName)) {
            throw new NodeAlreadyExistsException();
        }
    }

    public synchronized void removeNode(final String nodeName) {
        if (!graph.removeVertex(nodeName)) {
            throw new NodeNotFoundException();
        }
    }

    public synchronized void addEdge(final String source, final String target, final int weight) {
        assertNodeExists(source);
        assertNodeExists(target);

        DefaultWeightedEdge edge = graph.addEdge(source, target);
        graph.setEdgeWeight(edge, weight);
    }

    public synchronized void removeEdge(final String source, final String target) {
        assertNodeExists(source);
        assertNodeExists(target);

        if (graph.containsEdge(source, target)) {
            graph.removeEdge(source, target);
        }
    }

    public synchronized int getShortestPath(final String source, final String target) {
        assertNodeExists(source);
        assertNodeExists(target);

        final GraphPath<String, DefaultWeightedEdge> path = new BellmanFordShortestPath<>(graph).getPath(source, target);
        return path == null ? Integer.MAX_VALUE : (int) path.getWeight();
    }

    public synchronized String findNodesCloserThan(final int weight, final String node) {
        assertNodeExists(node);

        // From a given node, the BellmanFordShortestPath algorithm is used to find all reachable nodes, among which
        // only those whose weight (or distance) is less than the given weight are returned
        SingleSourcePaths<String, DefaultWeightedEdge> paths = new BellmanFordShortestPath<>(graph).getPaths(node);

        return paths
            .getGraph()
            .vertexSet()
            .stream()
            .filter(v -> paths.getWeight(v) < weight)
            .filter(v -> !v.equals(node))
            .sorted()
            .collect(Collectors.joining(","));
    }

    private synchronized void assertNodeExists(final String node) {
        if (!graph.containsVertex(node)) {
            throw new NodeNotFoundException();
        }
    }
}
