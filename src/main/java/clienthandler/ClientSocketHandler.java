package clienthandler;

import communication.CommunicationManager;
import communication.Session;
import graph.Graph;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.String.format;

/**
 * Takes care of conversation between the server and a client connected.
 * Delegates the job of executing clients'commands and getting right responses to the
 * {@link CommunicationManager}.
 *
 * This class is meant to run as a thread.
 */
public class ClientSocketHandler implements Runnable {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(ClientSocketHandler.class);
    private static final int CLIENT_TIMEOUT = 30000;
    private static final String CLIENT_SAYS = "Client: %s";
    private static final String SERVER_SAYS = "Server: %s";
    private static final String MSG_TIMED_OUT = "Timed out.";
    private static final String MSG_ERROR_READING_CLIENT_COMMAND = "Error reading client's command...";
    private static final String MSG_ERROR_TERMINATE_CLIENT_SOCKET = "Error terminating client socket...";
    private static final String MSG_ERROR_INITIALISING_CLIENT_SOCKET = "Error initialising client socket...";

    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private CommunicationManager comm;

    public ClientSocketHandler(final Socket clientSkt, final Graph graph) {
        clientSocket = clientSkt;
        comm = new CommunicationManager(graph);
        initialize();
    }

    @Override
    public void run() {
        sendResponse(comm.getServerGreeting());
        LOGGER.info(format("Server: %s", comm.getServerGreeting()));

        String command, response;
        try {
            // Reads client's command and looks for the right response
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
            LOGGER.error(MSG_ERROR_READING_CLIENT_COMMAND, e);
        } finally {
            terminate();
        }
    }

    private void terminate() {
        try {
            clientSocket.close();
            in.close();
            out.close();
        } catch (final IOException e) {
            LOGGER.error(MSG_ERROR_TERMINATE_CLIENT_SOCKET, e);
        }
    }

    private void sendResponse(final String response) {
        out.println(response);
    }


    /**
     * Gets ready for the incoming and outgoing messages
     */
    private void initialize()  {
        try {
            clientSocket.setSoTimeout(CLIENT_TIMEOUT);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            LOGGER.error(MSG_ERROR_INITIALISING_CLIENT_SOCKET, e);
        }
    }
}