package com.collibra.codechallenge.server;

import com.collibra.codechallenge.clienthandler.ClientSocketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;


/**
 * Acts as the server to which clients are connected and send their commands.
 * Upon a client connection, delegates the job of client command handling to a  dedicated
 * {@link ClientSocketHandler} instance.
 */
public class TCPServer {

    private static final Logger LOGGER = LogManager.getLogger(TCPServer.class);
    private static final String MSG_SERVER_RUNNING_ON_PORT = "Server running on port %d...";
    private static final String MSG_ERROR_STARTING_SERVER = "Error starting the TCP server.";
    private static final String MSG_ERROR_TERMINATING_SERVER = "Error terminating the TCP server.";
    private static final String MSG_GENERIC_ERROR = "Oops, some problem occurred while processing clients' commands";

    private ServerSocket serverSocket;

    public void start(final int portNumber) {
        try {
            serverSocket = new ServerSocket(portNumber);
            LOGGER.info(String.format(MSG_SERVER_RUNNING_ON_PORT, portNumber));

            while (true) {
                // for each client connected, create a new handler
                // and handle communication with that client in a new thread
                final ClientSocketHandler handler = new ClientSocketHandler(serverSocket.accept());
                new Thread(handler).start();
            }
        } catch (final IOException e) {
            LOGGER.error(MSG_ERROR_STARTING_SERVER, e);
        } catch (final Exception e) { // catch any errors that weren't not caught
            LOGGER.error(MSG_GENERIC_ERROR, e);
        } finally {
            terminate();
        }
    }

    private void terminate() {
        try {
            serverSocket.close();
        } catch (final IOException e) {
            LOGGER.error(MSG_ERROR_TERMINATING_SERVER, e);
        }
    }
}