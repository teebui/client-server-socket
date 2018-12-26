package libs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static libs.CommunicationManager.*;

public class ClientSocketHandler implements Runnable {

    private static final int CLIENT_TIMEOUT = 30000;
    private final Socket clientSocket;
    private final Session session;
    private final Graph graph;
    private PrintWriter out;
    private BufferedReader in;

    public ClientSocketHandler(final Socket clientSkt, final Graph aGraph) throws IOException {
        session = new Session();
        graph = aGraph;
        clientSocket = clientSkt;
        initializeClientSocket();
    }

    public void run() {
        sendResponse(getServerGreeting(session));
        System.out.println("S: " + getServerGreeting(session));

        String command, response;
        try {
            while ((command = in.readLine()) != null) {
                System.out.println("C: " + command);
                response = getResponse(command, session, graph);
                System.out.println("S: " + response);
                sendResponse(response);

                if (clientSaysGoodBye(command)) {
                    return;
                }
            }
        } catch (InterruptedIOException e) {
            session.terminate();
            sendResponse(getServerGoodbye(session));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            terminateClient();
        }
    }

    private void terminateClient() {
        try {
            clientSocket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(final String response) {
        out.println(response);
    }

    private void initializeClientSocket() throws IOException {
        clientSocket.setSoTimeout(CLIENT_TIMEOUT);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }
}
