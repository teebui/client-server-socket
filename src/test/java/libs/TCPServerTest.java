package libs;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class TCPServerTest {
    private static final String HOST = "localhost";
    private static final int PORT_NUMBER = 50000;

    @Test
    public void connectSuccessfullyToTCPServerOnPort50000() throws IOException {
        Socket socket = new Socket(HOST, PORT_NUMBER);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        assertTrue(in.readLine().startsWith("HI, I'M "));
    }
}