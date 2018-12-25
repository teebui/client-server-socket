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

    @Before
    public void setUp() throws IOException, InterruptedException {
//        TCPServer tcpServer = new TCPServer();
//        tcpServer.start();
//        Thread.sleep(2000);
        System.out.println("Connect now to the server...");
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
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        UUID clientUUID = UUID.randomUUID();
        String sayHi = format("HI, I'M %s", clientUUID);

        // Act
        out.println(sayHi);

        // Assert
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        assertEquals(format("HI %s", clientUUID), in.readLine());
    }

    @Test
    public void sayByeToServer() throws IOException {
        // Arrange
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        UUID clientUUID = UUID.randomUUID();
        String bye = "BYE MATE!";

        // Act
        out.println(bye);

        // Assert
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        assertEquals(format("BYE %s, WE SPOKE FOR %d MS", clientUUID, 12345), in.readLine());
    }

    @Test
    public void addNodeSuccessfully() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        assertTrue(in.readLine().startsWith("HI, I'M "));
        // Arrange
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);


        out.println("ADD NODE A");
        assertEquals("NODE ADDED", in.readLine());

    }

    @Test
    public void addNodeFailed_NodeAlreadyExists() throws IOException {
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
    public void addEdge_Success() throws IOException {
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
}