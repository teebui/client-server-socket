package libs;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.Executors;

import static java.lang.String.*;

public class ClientSocketHandler {
    public static final String HI_I_M = "HI, I'M ";

    private Socket clientSocket;
    private Session session;


    public ClientSocketHandler(Socket clientSkt) {
        clientSocket = clientSkt;
        session = new Session();

    }

    public void handle() {
        System.out.println("Start handling client with session ID " + session.getSessionID());
        PrintWriter out = null;
        BufferedReader in;
        String request, response;
        try {
            clientSocket.setSoTimeout(30000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            String connectionStartText = format("HI, I'M %s", session.getSessionID());
            out.println(connectionStartText);
            System.out.println("S: " + connectionStartText);

            while ((request = in.readLine()) != null) {
                System.out.println("C: " + request);
                response = getResponse(request, session);
                System.out.println("S: " + response);
                out.println(response);
            }
        } catch (SocketTimeoutException e) {
            session.terminate();
            out.println(format("BYE %s, WE SPOKE FOR %d MS", session.getClientName(), session.getDuration()));
        } catch (InterruptedIOException e) {
            session.terminate();
            out.println(format("BYE %s, WE SPOKE FOR %d MS", session.getClientName(), session.getDuration()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private String getResponse(final String request, final Session session) {
        if (request.startsWith(HI_I_M)) {
            final String clientID = request.replace(HI_I_M, "");
            session.setClientName(clientID);

            return format("HI %s", clientID);
        } else if (request.equals("BYE MATE!")) {
            session.terminate();
            return format("BYE %s, WE SPOKE FOR %d MS", session.getClientName(), session.getDuration());
        }

        return "SORRY, I DIDN'T UNDERSTAND THAT";
    }
}
