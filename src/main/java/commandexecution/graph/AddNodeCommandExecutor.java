package commandexecution.graph;

import commandexecution.DefaultCommandExecutor;
import exceptions.NodeAlreadyExistsException;
import graph.Graph;

import java.util.regex.Matcher;

import static messages.Responses.ERROR_NODE_ALREADY_EXISTS;
import static messages.Responses.NODE_ADDED;

/**
 * Extracts a node name from a given command and a a new node with that name to the global graph.
 * Returns an error message if a node with the same name already exists.
 */
public class AddNodeCommandExecutor extends DefaultCommandExecutor {

    public AddNodeCommandExecutor(final Matcher command) {
        super(command);
    }

    @Override
    public String getResponse() {
        final String node = command.group(1);
        try {
            Graph.getInstance().addNode(node);
        } catch (final NodeAlreadyExistsException e) {
            return ERROR_NODE_ALREADY_EXISTS;
        }

        return NODE_ADDED;
    }
}