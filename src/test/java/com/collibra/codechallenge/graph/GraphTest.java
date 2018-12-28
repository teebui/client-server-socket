package com.collibra.codechallenge.graph;

import com.collibra.codechallenge.exceptions.NodeAlreadyExistsException;
import com.collibra.codechallenge.exceptions.NodeNotFoundException;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.junit.After;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GraphTest {

    private ShadowGraph testGraph = new ShadowGraph();


    @After
    public void tearDown() {
        testGraph.reset();
    }

    @Test
    public void addNode_NodeAdded() throws NodeAlreadyExistsException {
        // Arrange
        final String name = "node-A";

        // Act
        testGraph.addNode(name);

        // Assert
        assertTrue(testGraph.containsNode(name));
    }

    @Test(expected = NodeAlreadyExistsException.class)
    public void addNode_ThrowsNodeAlreadyExistsException() throws NodeAlreadyExistsException {
        // Arrange
        final String name = "node-A";
        testGraph.addNode(name);

        // Act
        testGraph.addNode(name);
    }

    @Test
    public void removeNode_NodeRemoved() throws NodeAlreadyExistsException, NodeNotFoundException {
        // Arrange
        final String name = "node-A";
        testGraph.addNode(name);

        // Act
        testGraph.removeNode(name);

        // Assert
        assertFalse(testGraph.containsNode(name));
    }

    @Test
    public void removeNode_EdgeConnectedToNodeRemoved() throws NodeAlreadyExistsException, NodeNotFoundException {
        // Arrange
        final String nodeA = "node-A";
        final String nodeB = "node-B";
        testGraph.addNode(nodeA);
        testGraph.addNode(nodeB);
        testGraph.addEdge(nodeA, nodeB, 3);

        // Act
        testGraph.removeNode(nodeA);

        // Add node A back, but now the edge between node A & B should've disappeared
        testGraph.addNode(nodeA);

        // Assert
        assertFalse(testGraph.containsEdge(nodeA, nodeB));
    }

    @Test(expected = NodeNotFoundException.class)
    public void removeNode_ThrowsNodeNotFoundException() throws NodeNotFoundException {
        // Arrange
        final String name = "node-A";

        // Act
        testGraph.removeNode(name);
    }

    @Test
    public void addEdge_EdgeAdded() throws NodeNotFoundException, NodeAlreadyExistsException {
        // Arrange
        final String nodeA = "node-A";
        final String nodeB = "node-B";
        testGraph.addNode(nodeA);
        testGraph.addNode(nodeB);

        // Act
        testGraph.addEdge(nodeA, nodeB, 3);

        // Assert
        assertTrue(testGraph.containsEdge(nodeA, nodeB));
    }

    @Test(expected = NodeNotFoundException.class)
    public void addEdge_ThrowsNodeNotFoundException() throws NodeNotFoundException {
        final String nodeA = "node-A";
        final String nodeB = "node-B";

        testGraph.addEdge(nodeA, nodeB, 3);
    }

    @Test
    public void removeEdge_EdgeRemoved() throws NodeAlreadyExistsException, NodeNotFoundException {
        // Arrange
        final String nodeA = "node-A";
        final String nodeB = "node-B";
        testGraph.addNode(nodeA);
        testGraph.addNode(nodeB);
        testGraph.addEdge(nodeA, nodeB, 3);

        // Act
        testGraph.removeEdge(nodeA, nodeB);

        // Assert
        assertFalse(testGraph.containsEdge(nodeA, nodeB));
    }

    @Test(expected = NodeNotFoundException.class)
    public void removeEdge_ThrowsNodeNotFoundException() throws NodeNotFoundException {
        // Arrange
        final String name = "node-A";

        // Act
        testGraph.removeNode(name);
    }

    @Test
    public void findShortestPath_PathWeightReturned() throws NodeAlreadyExistsException, NodeNotFoundException {
        // Arrange
        final String nodeA = "node-A";
        final String nodeB = "node-B";
        final String nodeC = "node-C";
        final String nodeD = "node-D";

        testGraph.addNode(nodeA);
        testGraph.addNode(nodeB);
        testGraph.addNode(nodeC);
        testGraph.addNode(nodeD);

        testGraph.addEdge(nodeA, nodeB, 1);
        testGraph.addEdge(nodeA, nodeC, 2);
        testGraph.addEdge(nodeB, nodeD, 1);
        testGraph.addEdge(nodeC, nodeD, 3);
        testGraph.addEdge(nodeA, nodeD, 7);

        // Act
        int shortestPath = testGraph.findShortestPath(nodeA, nodeD);

        // Assert
        assertEquals(2, shortestPath);
    }

    @Test(expected = NodeNotFoundException.class)
    public void findShortestPath_ThrowsNodeNotFoundException() throws NodeNotFoundException {
        // Arrange
        final String nodeA = "node-A";
        final String nodeB = "node-B";

        // Act
        testGraph.findShortestPath(nodeA, nodeB);
    }
    @Test
    public void findCloserThan_ListOfNodesReturned() throws NodeAlreadyExistsException, NodeNotFoundException {
        // Arrange
        final String nodeA = "node-A";
        final String nodeB = "node-B";
        final String nodeC = "node-C";
        final String nodeD = "node-D";

        testGraph.addNode(nodeA);
        testGraph.addNode(nodeB);
        testGraph.addNode(nodeC);
        testGraph.addNode(nodeD);

        testGraph.addEdge(nodeA, nodeB, 2);
        testGraph.addEdge(nodeB, nodeC, 5);
        testGraph.addEdge(nodeC, nodeD, 8);

        // Act
        String nodes = testGraph.findNodesCloserThan(8, nodeA);

        // Assert
        assertEquals("node-B,node-C", nodes);
    }

    @Test(expected = NodeNotFoundException.class)
    public void findCloserThan_ThrowsNodeNotFoundException() throws NodeNotFoundException {
        // Arrange
        final String nodeA = "node-A";
        final String nodeB = "node-B";

        // Act
        testGraph.findShortestPath(nodeA, nodeB);
    }

    /**
     * Can reset all graph data (nodes, edge).
     * Created just for tests.
     */
    class ShadowGraph extends Graph {
        public void reset() {
            super.graph = new DirectedWeightedPseudograph<>(DefaultWeightedEdge.class);
        }

        public boolean containsNode(String node) {
            return graph.containsVertex(node);
        }

        public boolean containsEdge(final String source, final String targe) {
            return graph.containsEdge(source, targe);
        }
    }
}