package libs;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

public class MyGraph {
    private DirectedWeightedMultigraph<String, DefaultWeightedEdge> graph;

    public MyGraph() {
        graph = new DirectedWeightedMultigraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
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

    private void assertNodeExists(String node) throws NodeNotFoundException {
        if (!graph.containsVertex(node))
            throw new NodeNotFoundException();
    }
}
