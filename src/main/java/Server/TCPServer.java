package Server;

import ClientHandler.ClientSocketHandler;
import Graph.Graph;

import java.io.IOException;
import java.net.ServerSocket;

public class TCPServer {
    private static final int PORT_NUMBER = 50000;
    private ServerSocket serverSocket;
    private Graph graph;

    public void start() {
        System.out.println("Server running on port " + PORT_NUMBER);

        try {
            graph = new Graph();
            serverSocket = new ServerSocket(PORT_NUMBER);
            while (true) {
                ClientSocketHandler handler = new ClientSocketHandler(serverSocket.accept(), graph);
                new Thread(handler).start();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            terminate();
        }
    }

    private void terminate() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}