package commandexecution.graph;

import commandexecution.DefaultCommandExecutor;
import exceptions.NodeAlreadyExistsException;
import graph.Graph;

import static messages.Commands.CMD_ADD_NODE;
import static messages.Responses.RSP_ERROR_NODE_ALREADY_EXISTS;
import static messages.Responses.RSP_NODE_ADDED;

/**
 * Extracts a node name from a given command and a a new node with that name to the global graph.
 * Returns an error message if a node with the same name already exists.
 */
public class AddNodeCommandExecutor extends DefaultCommandExecutor {

    public AddNodeCommandExecutor(final String command) {
        super(command);
    }

    @Override
    public String getResponse() {
        final String nodeName = command.replace(CMD_ADD_NODE, "");
        try {
            Graph.getInstance().addNode(nodeName);
        } catch (final NodeAlreadyExistsException e) {
            return RSP_ERROR_NODE_ALREADY_EXISTS;
        }

        return RSP_NODE_ADDED;
    }
}