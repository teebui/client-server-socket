package commandexecution.graph;

import commandexecution.DefaultCommandExecutor;
import graph.Graph;
import exceptions.NodeNotFoundException;

import static messages.Commands.CMD_ADD_EDGE;
import static messages.Responses.RSP_EDGE_ADDED;
import static messages.Responses.RSP_ERROR_NODE_NOT_FOUND;

/**
 * Extracts two node names and weight from a command then creates an edge between the two nodes with the given weight.
 * Returns an error message if any of the nodes does not exist.
 */
public class AddEdgeCommandExecutor extends DefaultCommandExecutor {

    public AddEdgeCommandExecutor(final String command) {
        super(command);
    }

    @Override
    public String getResponse() {
        String edge = command.replace(CMD_ADD_EDGE, "");
        String[] s = edge.split(" ");
        try {
            int weight = Integer.parseInt(s[2]);
            Graph.getInstance().addEdge(s[0], s[1], weight);

        } catch (final NodeNotFoundException e) {
            return RSP_ERROR_NODE_NOT_FOUND;
        }

        return RSP_EDGE_ADDED;
    }
}