package libs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocketHandler implements Runnable {

    private static final int CLIENT_TIMEOUT = 30000;
    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private CommunicationManager comm;

    public ClientSocketHandler(final Socket clientSkt, final Graph graph) throws IOException {
        clientSocket = clientSkt;
        comm = new CommunicationManager(new Session(), graph);
        initializeClientSocket();
    }

    public void run() {
        sendResponse(comm.getServerGreeting());
        System.out.println("S: " + comm.getServerGreeting());

        String command, response;
        try {
            while ((command = in.readLine()) != null) {
                System.out.println("C: " + command);
                response = comm.getResponse(command);
                System.out.println("S: " + response);
                sendResponse(response);

                if (comm.clientSaysGoodBye(command)) {
                    return;
                }
            }
        } catch (InterruptedIOException e) {
            sendResponse(comm.getServerGoodbye());
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
