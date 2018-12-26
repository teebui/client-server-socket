package libs;

import static java.lang.String.format;

public class CommunicationManager {

    // List of client commands
    private static final String CMD_CLIENT_GREETING = "HI, I'M ";
    private static final String CMD_CLIENT_BYE = "BYE MATE!";
    private static final String CMD_REMOVE_EDGE = "REMOVE EDGE ";
    private static final String CMD_ADD_NODE = "ADD NODE ";
    private static final String CMD_ADD_EDGE = "ADD EDGE ";
    private static final String CMD_REMOVE_NODE = "REMOVE NODE ";
    private static final String CMD_SHORTEST_PATH = "SHORTEST PATH ";
    private static final String CMD_CLOSER_THAN = "CLOSER THAN ";

    // List of server responses
    private static final String RSP_SERVER_INTRO = "HI, I'M %s";
    private static final String RSP_SERVER_GREETING = "HI %s";
    private static final String RSP_SERVER_BYE = "BYE %s, WE SPOKE FOR %d MS";
    private static final String RSP_UNKNOWN_REQUEST = "SORRY, I DIDN'T UNDERSTAND THAT";
    private static final String RSP_ERROR_NODE_ALREADY_EXISTS = "ERROR: NODE ALREADY EXISTS";
    private static final String RSP_NODE_ADDED = "NODE ADDED";
    private static final String RSP_ERROR_NODE_NOT_FOUND = "ERROR: NODE NOT FOUND";
    private static final String RSP_EDGE_REMOVED = "EDGE REMOVED";
    private static final String RSP_EDGE_ADDED = "EDGE ADDED";
    private static final String RSP_NODE_REMOVED = "NODE REMOVED";

    public static boolean clientSaysGoodBye(String command) {
        return command.equals(CMD_CLIENT_BYE);
    }

    public static String getServerGreeting(final Session session) {
        return format(RSP_SERVER_INTRO, session.getSessionID());
    }

    public static String getServerGoodbye(final Session session) {
        return format(RSP_SERVER_BYE, session.getClientName(), session.getDuration());
    }

    public static String getResponse(String command, Session session, Graph graph) {

        if (command.startsWith(CMD_CLIENT_GREETING)) {
            final String clientID = command.replace(CMD_CLIENT_GREETING, "");
            session.setClientName(clientID);
            return format(RSP_SERVER_GREETING, clientID);
        } else if (command.equals(CMD_CLIENT_BYE)) {
            session.terminate();
            return format(RSP_SERVER_BYE, session.getClientName(), session.getDuration());
        } else if (command.startsWith(CMD_ADD_NODE)) {
            String nodeName = command.replace(CMD_ADD_NODE, "");
            try {
                graph.addNode(nodeName);
            } catch (NodeAlreadyExistsException e) {
                return RSP_ERROR_NODE_ALREADY_EXISTS;
            }

            return RSP_NODE_ADDED;
        } else if (command.startsWith(CMD_ADD_EDGE)) {
            String edge = command.replace(CMD_ADD_EDGE, "");
            String[] s = edge.split(" ");
            try {
                int weight = Integer.parseInt(s[2]);
                graph.addEdge(s[0], s[1], weight);

            } catch (NodeNotFoundException e) {
                return RSP_ERROR_NODE_NOT_FOUND;
            }

            return RSP_EDGE_ADDED;
        } else if (command.startsWith(CMD_REMOVE_NODE)) {
            String node = command.replace(CMD_REMOVE_NODE, "");
            try {
                graph.removeNode(node);
            } catch (NodeNotFoundException e) {
                return RSP_ERROR_NODE_NOT_FOUND;
            }

            return RSP_NODE_REMOVED;
        } else if (command.startsWith(CMD_REMOVE_EDGE)) {
            String edge = command.replace(CMD_REMOVE_EDGE, "");
            String[] s = edge.split(" ");
            try {
                graph.removeEdge(s[0], s[1]);
            } catch (NodeNotFoundException e) {
                return RSP_ERROR_NODE_NOT_FOUND;
            }

            return RSP_EDGE_REMOVED;
        } else if (command.startsWith(CMD_SHORTEST_PATH)) {
            String nodes = command.replace(CMD_SHORTEST_PATH, "");
            String[] s = nodes.split(" ");
            int shortestPath;
            try {
                shortestPath = graph.getShortestPath(s[0], s[1]);
            } catch (NodeNotFoundException e) {
                return RSP_ERROR_NODE_NOT_FOUND;
            }

            return format("%d", shortestPath);

        } else if (command.startsWith(CMD_CLOSER_THAN)) {
            String nodes = command.replace(CMD_CLOSER_THAN, "");
            String[] s = nodes.split(" ");
            String closerNodes;
            try {
                closerNodes = graph.findNodesCloserThan(Integer.parseInt(s[0]), s[1]);
            } catch (NodeNotFoundException e) {
                return RSP_ERROR_NODE_NOT_FOUND;
            }

            return closerNodes;

        }

        return RSP_UNKNOWN_REQUEST;
    }
}
