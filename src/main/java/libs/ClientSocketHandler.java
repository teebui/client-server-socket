package libs;

import java.io.*;
import java.net.Socket;

import static java.lang.String.format;

public class ClientSocketHandler implements Runnable {
    public static final String HI_I_M = "HI, I'M ";
    public static final String HI = "HI %s";
    public static final String SERVER_BYE = "BYE %s, WE SPOKE FOR %d MS";
    public static final String CLIENT_BYE = "BYE MATE!";
    public static final String SERVER_UNKNOWN_REQUEST = "SORRY, I DIDN'T UNDERSTAND THAT";
    public static final String ERROR_NODE_ALREADY_EXISTS = "ERROR: NODE ALREADY EXISTS";
    public static final String NODE_ADDED = "NODE ADDED";
    public static final String ERROR_NODE_NOT_FOUND = "ERROR: NODE NOT FOUND";
    public static final String CMD_REMOVE_EDGE = "REMOVE EDGE ";
    public static final String RSP_EDGE_REMOVED = "EDGE REMOVED";
    public static final String CMD_ADD_NODE = "ADD NODE ";
    public static final String CMD_ADD_EDGE = "ADD EDGE ";
    public static final String RSP_EDGE_ADDED = "EDGE ADDED";
    public static final String CMD_REMOVE_NODE = "REMOVE NODE ";
    public static final String RSP_NODE_REMOVED = "NODE REMOVED";

    private final Socket clientSocket;
    private final Session session;
    private final MyGraph graph;


    public ClientSocketHandler(final Socket clientSkt, MyGraph gr) {
        clientSocket = clientSkt;
        session = new Session();
        graph = gr;
    }


    public void run() {
        System.out.println("Start handling client with session ID " + session.getSessionID());
        PrintWriter out = null;
        BufferedReader in = null;
        String request, response;

        try {
            clientSocket.setSoTimeout(30000);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(format("HI, I'M %s", session.getSessionID()));
            System.out.println("S: " + format("HI, I'M %s", session.getSessionID()));

            while ((request = in.readLine()) != null) {
                System.out.println("C: " + request);
                response = getResponse(request);
                System.out.println("S: " + response);
                out.println(response);

                if (request.equals(CLIENT_BYE)) {
                    return;
                }
            }
        } catch (InterruptedIOException e) {
            session.terminate();
            out.println(format(SERVER_BYE, session.getClientName(), session.getDuration()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private String getResponse(final String command) {
        if (command.startsWith(HI_I_M)) {
            final String clientID = command.replace(HI_I_M, "");
            session.setClientName(clientID);
            return format(HI, clientID);
        } else if (command.equals(CLIENT_BYE)) {
            session.terminate();
            return format(SERVER_BYE, session.getClientName(), session.getDuration());
        } else if (command.startsWith(CMD_ADD_NODE)) {
            String nodeName = command.replace(CMD_ADD_NODE, "");
            try {
                graph.addNode(nodeName);
            } catch (NodeAlreadyExistsException e) {
                return ERROR_NODE_ALREADY_EXISTS;
            }

            return NODE_ADDED;
        } else if (command.startsWith(CMD_ADD_EDGE)) {
            String edge = command.replace(CMD_ADD_EDGE, "");
            String[] s = edge.split(" ");
            try {
                graph.addEdge(s[0], s[1], Integer.parseInt(s[2]));

            } catch (NodeNotFoundException e) {
                return ERROR_NODE_NOT_FOUND;
            }

            return RSP_EDGE_ADDED;
        } else if (command.startsWith(CMD_REMOVE_NODE)) {
            String node = command.replace(CMD_REMOVE_NODE, "");
            try {
                graph.removeNode(node);
            } catch (NodeNotFoundException e) {
                return ERROR_NODE_NOT_FOUND;
            }

            return RSP_NODE_REMOVED;
        } else if (command.startsWith(CMD_REMOVE_EDGE)) {
            String edge = command.replace(CMD_REMOVE_EDGE, "");
            String[] s = edge.split(" ");
            try {
                graph.removeEdge(s[0], s[1]);
            } catch (NodeNotFoundException e) {
                return ERROR_NODE_NOT_FOUND;
            }

            return RSP_EDGE_REMOVED;
        }

        return SERVER_UNKNOWN_REQUEST;
    }

}
