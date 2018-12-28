package commandexecution.graph;

import commandexecution.DefaultCommandExecutor;
import exceptions.NodeNotFoundException;
import graph.Graph;

import java.util.regex.Matcher;

import static messages.Responses.EDGE_REMOVED;
import static messages.Responses.ERROR_NODE_NOT_FOUND;

/**
 * Extracts two node names from the given command and remove the edge between these two nodes.
 * Returns an error message if any of the nodes does not exist.
 */
public class RemoveEdgeCommandExecutor extends DefaultCommandExecutor {

    public RemoveEdgeCommandExecutor(final Matcher command) {
        super(command);
    }

    @Override
    public String getResponse() {

        final String sourceNode = command.group(1);
        final String targetNode = command.group(2);

        try {
            Graph.getInstance().removeEdge(sourceNode, targetNode);
        } catch (final NodeNotFoundException e) {
            return ERROR_NODE_NOT_FOUND;
        }

        return EDGE_REMOVED;
    }
}