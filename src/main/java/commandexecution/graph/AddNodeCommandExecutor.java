package commandexecution.graph;

import commandexecution.CommandExecutor;
import graph.Graph;
import graph.NodeAlreadyExistsException;

import static messages.Commands.CMD_ADD_NODE;
import static messages.Responses.RSP_ERROR_NODE_ALREADY_EXISTS;
import static messages.Responses.RSP_NODE_ADDED;

public class AddNodeCommandExecutor implements CommandExecutor {
    private String command;
    private Graph graph;

    public AddNodeCommandExecutor(final String command, final Graph graph) {
        this.command = command;
        this.graph = graph;
    }

    @Override
    public String getResponse() {
        final String nodeName = command.replace(CMD_ADD_NODE, "");
        try {
            graph.addNode(nodeName);
        } catch (NodeAlreadyExistsException e) {
            return RSP_ERROR_NODE_ALREADY_EXISTS;
        }

        return RSP_NODE_ADDED;
    }
}
