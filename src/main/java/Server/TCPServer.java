package Server;

import ClientHandler.ClientSocketHandler;
import Graph.Graph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;

public class TCPServer {
    private static final Logger LOGGER = LogManager.getLogger(TCPServer.class);
    private ServerSocket serverSocket;
    private Graph graph;

    public void start(final int portNumber) {

        try {
            serverSocket = new ServerSocket(portNumber);
            LOGGER.info(String.format("Server running on port %d...", portNumber));

            graph = new Graph();
            while (true) {
                ClientSocketHandler handler = new ClientSocketHandler(serverSocket.accept(), graph);
                new Thread(handler).start();
            }
        } catch (final IOException e) {
            LOGGER.error("Error starting the TCP server", e);
        } finally {
            terminate();
        }
    }

    private void terminate() {
        try {
            serverSocket.close();
        } catch (final IOException e) {
            LOGGER.error("Error terminating the TCP server", e);
        }
    }
}