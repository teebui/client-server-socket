package commandexecution.graph;

import commandexecution.DefaultCommandExecutor;
import exceptions.NodeNotFoundException;
import graph.Graph;

import static java.lang.String.format;
import static messages.Commands.CMD_SHORTEST_PATH;
import static messages.Responses.RSP_ERROR_NODE_NOT_FOUND;

/**
 * Extracts two node names from the given command and find the shortest path (i.e. path with minimal total weight)
 * between the two nodes.
 * Returns an error message if any the given nodes does not exist.
 */
public class ShortestPathCommandExecutor extends DefaultCommandExecutor {

    public ShortestPathCommandExecutor(final String command) {
        super(command);
    }

    @Override
    public String getResponse() {
        String nodes = command.replace(CMD_SHORTEST_PATH, "");
        String[] s = nodes.split(" ");
        int shortestPath;
        try {
            shortestPath = Graph.getInstance().getShortestPath(s[0], s[1]);
        } catch (NodeNotFoundException e) {
            return RSP_ERROR_NODE_NOT_FOUND;
        }

        return format("%d", shortestPath);
    }
}