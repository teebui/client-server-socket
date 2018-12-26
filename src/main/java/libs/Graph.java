package libs;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import java.util.stream.Collectors;

public class Graph {
    private DirectedWeightedMultigraph<String, DefaultWeightedEdge> graph;

    public Graph() {
        graph = new DirectedWeightedMultigraph<>(DefaultWeightedEdge.class);
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

    public synchronized void addEdge(final String source, final String target, int weight)
            throws NodeNotFoundException {
        try {
            DefaultWeightedEdge edge = graph.addEdge(source, target);
            graph.setEdgeWeight(edge, weight);

        } catch (IllegalArgumentException e) {
            throw new NodeNotFoundException();
        }
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

        ShortestPathAlgorithm.SingleSourcePaths<String, DefaultWeightedEdge> paths = new DijkstraShortestPath<>(graph)
                .getPaths(node);

        return paths
                .getGraph()
                .vertexSet()
                .stream()
                .filter(v -> paths.getWeight(v) < weight)
                .filter(v -> !v.equals(node))
                .sorted()
                .collect(Collectors.joining(","));
    }



    private void assertNodeExists(String node) throws NodeNotFoundException {
        if (!graph.containsVertex(node))
            throw new NodeNotFoundException();
    }
}
