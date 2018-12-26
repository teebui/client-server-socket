package libs;

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


public class TCPServerTest {
    private static final String HOST = "localhost";
    private static final int PORT_NUMBER = 50000;
    private static Socket clientSocket;
    private static BufferedReader in;
    private static PrintWriter out;
    private static long startTime;

    @Before
    public void setUp() throws IOException, InterruptedException {
//        TCPServer tcpServer = new TCPServer();
//        tcpServer.start();
//        Thread.sleep(2000);
        System.out.println("Connect now to the server...");
        startTime = System.currentTimeMillis();
        clientSocket = new Socket(HOST, PORT_NUMBER);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    @Test
    public void connectSuccessfullyToTCPServerOnPort50000() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        assertTrue(in.readLine().startsWith("HI, I'M "));
    }

    @Test
    public void sayHiToServer() throws IOException {
        // Arrange
        in.readLine();

        // Act
        out.println(format("HI, I'M %s", UUID.randomUUID()));

        // Assert
        assertEquals(format("HI %s", UUID.randomUUID()), in.readLine());
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
        assertEquals(format("BYE %s, WE SPOKE FOR %d MS", clientUUID, System.currentTimeMillis() - startTime), in.readLine());
    }

    @Test
    public void addNode_NodeAdded() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        assertTrue(in.readLine().startsWith("HI, I'M "));
        // Arrange
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);


        out.println("ADD NODE A");
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
        out.println("ADD EDGE A B 1");


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

        //  Act
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

        // Assert
        out.println("SHORTEST PATH A D");
        assertEquals("2", in.readLine());
    }
}