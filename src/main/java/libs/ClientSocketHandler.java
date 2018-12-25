package libs;

import java.io.*;
import java.net.Socket;

import static java.lang.String.format;

public class ClientSocketHandler {
    public static final String HI_I_M = "HI, I'M ";
    public static final String HI = "HI %s";
    public static final String SERVER_BYE = "BYE %s, WE SPOKE FOR %d MS";
    public static final String CLIENT_BYE = "BYE MATE!";
    public static final String SERVER_UNKNOWN_REQUEST = "SORRY, I DIDN'T UNDERSTAND THAT";

    private Socket clientSocket;
    private Session session;


    public ClientSocketHandler(Socket clientSkt) {
        clientSocket = clientSkt;
        session = new Session();
    }

    public void run() {
        System.out.println("Start handling client with session ID " + session.getSessionID());
        PrintWriter out = null;
        BufferedReader in;
        String request, response;

        try {
            clientSocket.setSoTimeout(30000);
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
            return format(HI, clientID);
        } else if (request.equals(CLIENT_BYE)) {
            session.terminate();
            return format(SERVER_BYE, session.getClientName(), session.getDuration());
        }

        return SERVER_UNKNOWN_REQUEST;
    }

}
