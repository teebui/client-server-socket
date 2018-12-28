package commandexecution.graph;

import commandexecution.DefaultCommandExecutor;
import exceptions.NodeNotFoundException;
import graph.Graph;

import static messages.Commands.CMD_CLOSER_THAN;
import static messages.Responses.RSP_ERROR_NODE_NOT_FOUND;

/**
 * Extracts a node name and a weight (distance) from the command and returns a list of all nodes whose weights are
 * smaller than the given weight.
 * Returns an error message if the given node does not yet exist.
 */
public class CloserThanCommandExecutor extends DefaultCommandExecutor {

    public CloserThanCommandExecutor(final String command) {
        super(command);
    }

    @Override
    public String getResponse() {
        final String nodes = command.replace(CMD_CLOSER_THAN, "");
        final String[] s = nodes.split(" ");

        String closerNodes;
        try {
            closerNodes = Graph.getInstance().findNodesCloserThan(Integer.parseInt(s[0]), s[1]);
        } catch (NodeNotFoundException e) {
            return RSP_ERROR_NODE_NOT_FOUND;
        }

        return closerNodes;
    }
}