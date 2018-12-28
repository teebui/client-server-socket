package com.collibra.codechallenge.graph;

import com.collibra.codechallenge.exceptions.NodeAlreadyExistsException;
import com.collibra.codechallenge.exceptions.NodeNotFoundException;
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
 *
 * This class is also implemented as a singleton to 1) mitigate the need for object instantiation, thus
 * avoid chaining; and 2) guarantee better thread-safety
 */
public class Graph {
    private static volatile Graph instance;
    private static final Object mutex = new Object();

    // The DirectedWeightedPseudograph allows loops, multiple edges
    // This field is purposely made protected so it's more testable
    protected volatile DirectedWeightedPseudograph<String, DefaultWeightedEdge> graph =
            new DirectedWeightedPseudograph<>(DefaultWeightedEdge.class);

    // Inspired by https://www.journaldev.com/171/thread-safety-in-java-singleton-classes-with-example-code
    public static Graph getInstance() {
        Graph result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null)
                    instance = result = new Graph();
            }
        }

        return result;
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

        final DefaultWeightedEdge edge = graph.addEdge(source, target);
        graph.setEdgeWeight(edge, weight);
    }

    public synchronized void removeEdge(final String source, final String target) throws NodeNotFoundException {
        assertNodeExists(source);
        assertNodeExists(target);

        if (graph.containsEdge(source, target)) {
            graph.removeEdge(source, target);
        }
    }

    /**
     * Uses {@link DijkstraShortestPath) algorithm to find the shortest path between two nodes
     */
    public synchronized int findShortestPath(final String source, final String target) throws NodeNotFoundException {
        assertNodeExists(source);
        assertNodeExists(target);

        final GraphPath<String, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph).getPath(source, target);
        return path == null ? Integer.MAX_VALUE : (int) path.getWeight();
    }


    /**
     * From a given node, the {@link BellmanFordShortestPath} algorithm is used to find all reachable nodes, among which
     * only those whose weight (or distance) is less than the given weight are returned.
     * Returns the list of nodes found.
     */
    public synchronized String findNodesCloserThan(final int weight, final String node)
            throws NodeNotFoundException {

        assertNodeExists(node);

        // it could be interesting to see if DijkstraShortestPath might have a different result
        final SingleSourcePaths<String, DefaultWeightedEdge> paths = new BellmanFordShortestPath<>(graph).getPaths(node);

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
        if (!graph.containsVertex(node)) {
            throw new NodeNotFoundException();
        }
    }
}