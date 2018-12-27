import Server.TCPServer;

public class Program {
    private static final int PORT_NUMBER = 50000;

    public static void main(String[] args) {
        final TCPServer tcpServer = new TCPServer();
        tcpServer.start(PORT_NUMBER);
    }
}
