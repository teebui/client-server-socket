package libs;

import static java.lang.String.format;
import static libs.Responses.*;
import static libs.Commands.*;

public class CommunicationManager {

    private Session session;
    private Graph graph;

    /**
     * Receives a client's command, acts accordingly and dispatches responses
     * @param session session information of the current client
     * @param graph
     */
    public CommunicationManager(final Session session, final Graph graph) {

        this.session = session;
        this.graph = graph;
    }

    public boolean clientSaysGoodBye(String command) {
        return command.equals(CMD_CLIENT_BYE);
    }

    public String getServerGreeting() {
        return format(RSP_SERVER_INTRO, session.getSessionID());
    }

    public String getServerGoodbye() {
        session.terminate();
        return format(RSP_SERVER_BYE, session.getClientName(), session.getDuration());
    }

    public String getResponse(String command) {
        CommandFactory.getCommand(command);

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
