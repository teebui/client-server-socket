package libs;

import java.io.IOException;
import java.net.ServerSocket;

public class TCPServer {
    private static final int PORT_NUMBER = 50000;
    private ServerSocket serverSocket;

    public void start() {
        Graph graph = new Graph();
        System.out.println("Server running on port " + PORT_NUMBER);
        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
            while (true) {
                ClientSocketHandler handler = new ClientSocketHandler(serverSocket.accept(), graph);
                Thread t = new Thread(handler);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    private void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}