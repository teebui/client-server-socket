package libs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    private static final int PORT_NUMBER = 50000;
    private ServerSocket serverSocket;

    public void start() {
        System.out.println("Server running on port " + PORT_NUMBER);
        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
            Socket clientSocket = serverSocket.accept();
            while (true) {
                final ClientSocketHandler handler = new ClientSocketHandler(clientSocket);
                handler.run();
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