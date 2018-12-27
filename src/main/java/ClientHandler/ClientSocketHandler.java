package ClientHandler;

import CommunicationHandler.CommunicationManager;
import Graph.Graph;
import Server.TCPServer;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

import static java.lang.String.*;
import static java.lang.String.format;

public class ClientSocketHandler implements Runnable {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(ClientSocketHandler.class);
    private static final int CLIENT_TIMEOUT = 30000;
    private static final String CLIENT_SAYS = "Client: %s";
    private static final String SERVER_SAYS = "Server: %s";
    private static final String MSG_TIMED_OUT = "Timed out.";

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
        LOGGER.info(format("Server: %s", comm.getServerGreeting()));

        String command, response;
        try {
            // Read client's command and look for the right response
            // until the client wants to stop the conversation
            while ((command = in.readLine()) != null) {
                LOGGER.info(format(CLIENT_SAYS, command));
                response = comm.getResponse(command);
                sendResponse(response);
                LOGGER.info(format(SERVER_SAYS, response));

                if (comm.clientSaysGoodBye(command)) {
                    return;
                }
            }
        } catch (final InterruptedIOException e) { // if client says nothing after 30 seconds
            LOGGER.debug(MSG_TIMED_OUT, e);
            sendResponse(comm.getServerGoodbye());
        } catch (final IOException e) {
            LOGGER.error("Error reading client's command...", e);
        } finally {
            terminateClient();
        }
    }

    private void terminateClient() {
        try {
            clientSocket.close();
            in.close();
            out.close();
        } catch (final IOException e) {
            LOGGER.error("Error terminating client socket handler...", e);
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
