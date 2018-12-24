package libs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

import static java.lang.String.*;

public class ClientSocketHandler {
    public static final String HI_I_M = "HI, I'M ";
    private Socket clientSocket;
    private UUID sessionID;

    public ClientSocketHandler(Socket clientSkt) {
        clientSocket = clientSkt;
        sessionID = UUID.randomUUID();
    }

    public void handle() {
        System.out.println("Start handling client with session ID " + sessionID);
        PrintWriter out = null;
        BufferedReader in = null;
        String request, response;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while ((request = in.readLine()) != null) {
                response = getResponse(request);
                out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private String getResponse(final String request) {
        String clientName = request.replace(HI_I_M, "");

        if (request.length() == 0) {
            return format("HI, I'M %s", sessionID);
        }
        else if (request.startsWith(HI_I_M)) {
            return format("HI %s", clientName);
        }

        return "SORRY, I DIDN'T UNDERSTAND THAT";
    }
}
