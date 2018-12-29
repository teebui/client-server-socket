package com.collibra.codechallenge.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Bring some simple test cases to the TCPServer
 * For now, the main Program needs to start first, separately, then we can run these tests.
 */
public class TCPServerTest {
    private static final String HOST = "localhost";
    private static final int PORT_NUMBER = 50000;
    private static Socket clientSocket;
    private static BufferedReader in;
    private static PrintWriter out;
    private static long startTime;

    @Before
    public void setUp() throws IOException {
//        TCPServer tcpServer = new TCPServer();
//        tcpServer.start(PORT_NUMBER);

        System.out.println("Connect now to the server...");
        startTime = System.currentTimeMillis();
        clientSocket = new Socket(HOST, PORT_NUMBER);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    @After
    public void tearDown() throws IOException {
        clientSocket.close();
        in.close();
        out.close();
    }

    @Test
    public void connectSuccessfullyToTCPServerOnPort50000() throws IOException {
        assertTrue(in.readLine().startsWith("HI, I'M "));
    }

    @Test
    public void sayHiToServer() throws IOException {
        // Arrange
        in.readLine();
        UUID uuid = UUID.randomUUID();

        // Act
        out.println(format("HI, I'M %s", uuid));

        // Assert
        assertEquals(format("HI %s", uuid), in.readLine());
    }

    @Test
    public void sayByeToServer() throws IOException {
        // Arrange
        in.readLine();

        UUID clientUUID = UUID.randomUUID();
        out.println(format("HI, I'M %s", clientUUID));
        in.readLine();

        // Act
        out.println("BYE MATE!");

        // Assert
        assertTrue(in.readLine().contains(format("BYE %s, WE SPOKE FOR", clientUUID)));
    }
    @Test
    public void disconnectedAfter30Seconds() throws IOException, InterruptedException {
        // Arrange
        in.readLine();
        UUID clientUUID = UUID.randomUUID();
        out.println(format("HI, I'M %s", clientUUID));
        in.readLine();

        // Act
        Thread.sleep(30000); // sleeps like a baby, just for half a minute

        // Assert
        assertTrue(in.readLine().contains(format("BYE %s, WE SPOKE FOR", clientUUID)));
    }

    @Test
    public void addNode_NodeAdded() throws IOException {
        // Arrange
        in.readLine();

        // Act
        out.println("ADD NODE E");

        // Act
        assertEquals("NODE ADDED", in.readLine());
    }

    @Test
    public void addNode_NodeAlreadyExists() throws IOException {
        // Arrange
        in.readLine();

        out.println("ADD NODE A");
        in.readLine();

        //  Act
        out.println("ADD NODE A");

        // Assert
        assertEquals("ERROR: NODE ALREADY EXISTS", in.readLine());

    }

    @Test
    public void addEdge_EdgeAdded() throws IOException {
        // Arrange
        in.readLine();

        out.println("ADD NODE A");
        in.readLine();

        out.println("ADD NODE B");
        in.readLine();

        //  Act
        out.println("ADD EDGE A B 1");


        // Assert
        assertEquals("EDGE ADDED", in.readLine());
    }

    @Test
    public void addEdge_NodeNotFound() throws IOException {
        // Arrange
        in.readLine();

        out.println("ADD NODE A");
        in.readLine();

        //  Act
        out.println("ADD EDGE A D 1");


        // Assert
        assertEquals("ERROR: NODE NOT FOUND", in.readLine());
    }

    @Test
    public void removeNode_NodeRemoved() throws IOException {
        // Arrange
        in.readLine();

        out.println("ADD NODE A");
        in.readLine();

        //  Act
        out.println("REMOVE NODE A");


        // Assert
        assertEquals("NODE REMOVED", in.readLine());
    }

    @Test
    public void removeNode_RemoveAllConnectedEdges() throws IOException {
        // Arrange
        in.readLine();

        addNewEdge();

        //  Act
        out.println("REMOVE NODE A");

        // Assert
        assertEquals("NODE REMOVED", in.readLine());
    }

    private void addNewEdge() throws IOException {
        out.println("ADD NODE A");
        in.readLine();

        out.println("ADD NODE B");
        in.readLine();

        out.println("ADD EDGE A B 1");
        in.readLine();
    }

    @Test
    public void removeNode_NodeNotFound() throws IOException {
        // Arrange
        in.readLine();

        //  Act
        out.println("REMOVE NODE B");


        // Assert
        assertEquals("ERROR: NODE NOT FOUND", in.readLine());
    }

    @Test
    public void removeEdge_EdgeRemoved() throws IOException {
        // Arrange
        in.readLine();

        addNewEdge();

        out.println("REMOVE EDGE A B");


        // Assert
        assertEquals("EDGE REMOVED", in.readLine());
    }

    @Test
    public void removeEdge_EdgeDoesNotExist_EdgeRemoved() throws IOException {
        // Arrange
        in.readLine();

        out.println("ADD NODE A");
        in.readLine();

        out.println("ADD NODE B");
        in.readLine();

        // Act
        out.println("REMOVE EDGE A B");

        // Assert
        assertEquals("EDGE REMOVED", in.readLine());
    }


    @Test
    public void shortestPath_WeightReturned() throws IOException {
        // Arrange
        in.readLine();

        out.println("ADD NODE A");
        in.readLine();

        out.println("ADD NODE B");
        in.readLine();

        out.println("ADD NODE C");
        in.readLine();

        out.println("ADD NODE D");
        in.readLine();

        out.println("ADD EDGE A B 1");
        in.readLine();
        out.println("ADD EDGE A C 2");
        in.readLine();
        out.println("ADD EDGE B D 1");
        in.readLine();
        out.println("ADD EDGE C D 3");
        in.readLine();
        out.println("ADD EDGE A D 7");
        in.readLine();

        //  Act
        out.println("SHORTEST PATH A D");

        // Assert
        assertEquals("2", in.readLine());
    }
    @Test
    public void closerThan_NodesReturned() throws IOException {
        // Arrange
        in.readLine();

        out.println("ADD NODE Mark");
        in.readLine();

        out.println("ADD NODE Michael");
        in.readLine();

        out.println("ADD NODE Madeleine");
        in.readLine();

        out.println("ADD NODE Mufasa");
        in.readLine();

        out.println("ADD EDGE Mark Michael 5");
        in.readLine();
        out.println("ADD EDGE Michael Madeleine 2");
        in.readLine();
        out.println("ADD EDGE Madeleine Mufasa 8");
        in.readLine();


        //  Act
        out.println("CLOSER THAN 8 Mark");

        // Assert
        assertEquals("Madeleine,Michael", in.readLine());
    }
}