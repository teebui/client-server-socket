package libs;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import java.util.stream.Collectors;

public class Graph {

    // The DirectedWeightedPseudograph allows loops, multiple edges
    private final DirectedWeightedPseudograph<String, DefaultWeightedEdge> graph;

    public Graph() {

        graph = new DirectedWeightedPseudograph<>(DefaultWeightedEdge.class);
    }

    public synchronized void addNode(final String nodeName) throws NodeAlreadyExistsException {

        if (!graph.addVertex(nodeName)) {
            throw new NodeAlreadyExistsException();
        }
    }

    public synchronized void removeNode(final String nodeName) throws NodeNotFoundException {
        if (!graph.removeVertex(nodeName)) {
            throw new NodeNotFoundException();
        }
    }

    public synchronized void addEdge(final String source, final String target, final int weight)
            throws NodeNotFoundException {

        assertNodeExists(source);
        assertNodeExists(target);

        DefaultWeightedEdge edge = graph.addEdge(source, target);
        graph.setEdgeWeight(edge, weight);
    }

    public synchronized void removeEdge(final String source, final String target) throws NodeNotFoundException {

        assertNodeExists(source);
        assertNodeExists(target);

        if (graph.containsEdge(source, target)) {
            graph.removeEdge(source, target);
        }
    }

    public synchronized int getShortestPath(final String source, final String target) throws NodeNotFoundException {

        assertNodeExists(source);
        assertNodeExists(target);

        final GraphPath<String, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph).getPath(source, target);
        return path == null ? Integer.MAX_VALUE : (int) path.getWeight();
    }

    public synchronized String findNodesCloserThan(final int weight, final String node)
            throws NodeNotFoundException {

        assertNodeExists(node);

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

    private synchronized void assertNodeExists(final String node) throws NodeNotFoundException {

        if (!graph.containsVertex(node))
            throw new NodeNotFoundException();
    }
}
