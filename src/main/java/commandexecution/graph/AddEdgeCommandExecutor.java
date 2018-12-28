package commandexecution.graph;

import commandexecution.DefaultCommandExecutor;
import exceptions.NodeNotFoundException;
import graph.Graph;

import java.util.regex.Matcher;

import static messages.Responses.EDGE_ADDED;
import static messages.Responses.ERROR_NODE_NOT_FOUND;

/**
 * Extracts two node names and weight from a command then creates an edge between the two nodes with the given weight.
 * Returns an error message if any of the nodes does not exist.
 */
public class AddEdgeCommandExecutor extends DefaultCommandExecutor {

    public AddEdgeCommandExecutor(final Matcher command) {
        super(command);
    }

    @Override
    public String getResponse() {

        final String sourceNode = command.group(1);
        final String targetNode = command.group(2);
        final int weight = Integer.parseInt(command.group(3));

        try {
            Graph.getInstance().addEdge(sourceNode, targetNode, weight);
        } catch (final NodeNotFoundException e) {
            return ERROR_NODE_NOT_FOUND;
        }

        return EDGE_ADDED;
    }
}