package libs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class ClientSocketHandler {
    private Socket clientSocket;
    private UUID sessionID;

    public ClientSocketHandler(Socket clientSkt) {
        clientSocket = clientSkt;
        sessionID = UUID.randomUUID();
    }

    public void handle() {
        System.out.println("Start handling client with session ID " + sessionID);
        PrintWriter out = null;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(("Hi, i'm " + sessionID).toUpperCase());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
