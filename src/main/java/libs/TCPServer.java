package libs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    private static final int PORT_NUMBER = 50000;

    public void start() {
        ServerSocket serverSocket;
        System.out.println("Server running on port " + PORT_NUMBER);
        try {
            serverSocket = new ServerSocket(PORT_NUMBER);
            Socket clientSocket = serverSocket.accept();
            ClientSocketHandler clientSocketHandler = new ClientSocketHandler(clientSocket);
            clientSocketHandler.handle();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
