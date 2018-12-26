package libs;

import static java.lang.String.format;

public class CommunicationManager {

    private static final String HI_I_M = "HI, I'M ";
    private static final String HI = "HI %s";
    private static final String SERVER_BYE = "BYE %s, WE SPOKE FOR %d MS";
    private static final String CLIENT_BYE = "BYE MATE!";
    private static final String SERVER_UNKNOWN_REQUEST = "SORRY, I DIDN'T UNDERSTAND THAT";
    private static final String ERROR_NODE_ALREADY_EXISTS = "ERROR: NODE ALREADY EXISTS";
    private static final String NODE_ADDED = "NODE ADDED";
    private static final String ERROR_NODE_NOT_FOUND = "ERROR: NODE NOT FOUND";
    private static final String CMD_REMOVE_EDGE = "REMOVE EDGE ";
    private static final String RSP_EDGE_REMOVED = "EDGE REMOVED";
    private static final String CMD_ADD_NODE = "ADD NODE ";
    private static final String CMD_ADD_EDGE = "ADD EDGE ";
    private static final String RSP_EDGE_ADDED = "EDGE ADDED";
    private static final String CMD_REMOVE_NODE = "REMOVE NODE ";
    private static final String RSP_NODE_REMOVED = "NODE REMOVED";
    private static final String CMD_SHORTEST_PATH = "SHORTEST PATH ";
    private static final String CMD_CLOSER_THAN = "CLOSER THAN ";

    public static String clientSaysGoodBye() {
        return CLIENT_BYE;
    }

    public static String serverSaysGoodBye() {
        return SERVER_BYE;
    }

    public static String getResponse(String command, Session session, Graph graph) {

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
        } else if (command.startsWith(CMD_SHORTEST_PATH)) {
            String nodes = command.replace(CMD_SHORTEST_PATH, "");
            String[] s = nodes.split(" ");
            int shortestPath;
            try {
                shortestPath = graph.getShortestPath(s[0], s[1]);
            } catch (NodeNotFoundException e) {
                return ERROR_NODE_NOT_FOUND;
            }

            return format("%d", shortestPath);

        } else if (command.startsWith(CMD_CLOSER_THAN)) {
            String nodes = command.replace(CMD_CLOSER_THAN, "");
            String[] s = nodes.split(" ");
            String closerNodes;
            try {
                closerNodes = graph.findNodesCloserThan(Integer.parseInt(s[0]), s[1]);
            } catch (NodeNotFoundException e) {
                return ERROR_NODE_NOT_FOUND;
            }

            return closerNodes;

        }

        return SERVER_UNKNOWN_REQUEST;
    }
}
