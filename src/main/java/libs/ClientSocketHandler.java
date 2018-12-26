package libs;

import java.io.*;
import java.net.Socket;

import static java.lang.String.format;
import static libs.CommunicationManager.*;

public class ClientSocketHandler implements Runnable {

    private final Socket clientSocket;
    private final Session session;
    private final Graph graph;


    public ClientSocketHandler(final Socket clientSkt, final Graph aGraph) {
        clientSocket = clientSkt;
        session = new Session();
        graph = aGraph;
    }


    public void run() {
        System.out.println("Start handling client with session ID " + session.getSessionID());
        PrintWriter out = null;
        BufferedReader in = null;
        String command, response;

        try {
            clientSocket.setSoTimeout(30000);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(format("HI, I'M %s", session.getSessionID()));
            System.out.println("S: " + format("HI, I'M %s", session.getSessionID()));

            while ((command = in.readLine()) != null) {
                System.out.println("C: " + command);
                response = getResponse(command, session, graph);
                System.out.println("S: " + response);
                out.println(response);

                if (command.equals(clientSaysGoodBye())) {
                    return;
                }
            }
        } catch (InterruptedIOException e) {
            session.terminate();
            out.println(format(serverSaysGoodBye(), session.getClientName(), session.getDuration()));
        } catch (IOException e) {
            e.printStackTrace();
        }
//
//        try {
//            in.close();
//            out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
