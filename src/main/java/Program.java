import Server.TCPServer;

public class Program {
    public static void main(String[] args) {
        TCPServer tcpServer = new TCPServer();
        tcpServer.start();
    }
}
