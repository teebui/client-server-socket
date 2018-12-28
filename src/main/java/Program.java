import Server.TCPServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Program {
    private static final int PORT_NUMBER = 50000;
    private static final Logger LOGGER = LogManager.getLogger(TCPServer.class);

    public static void main(String[] args) {

        LOGGER.info("Starting server...");
        final TCPServer tcpServer = new TCPServer();
        tcpServer.start(PORT_NUMBER);
    }
}
