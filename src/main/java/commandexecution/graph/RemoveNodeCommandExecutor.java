package commandexecution.graph;

import commandexecution.CommandExecutor;
import graph.Graph;
import graph.NodeNotFoundException;

import static messages.Commands.CMD_REMOVE_NODE;
import static messages.Responses.RSP_ERROR_NODE_NOT_FOUND;
import static messages.Responses.RSP_NODE_REMOVED;

public class RemoveNodeCommandExecutor implements CommandExecutor {
    private final String command;
    private final Graph graph;

    public RemoveNodeCommandExecutor(final String command, final Graph graph) {
        this.command = command;
        this.graph = graph;
    }

    @Override
    public String getResponse() {
        String node = command.replace(CMD_REMOVE_NODE, "");
        try {
            graph.removeNode(node);
        } catch (NodeNotFoundException e) {
            return RSP_ERROR_NODE_NOT_FOUND;
        }

        return RSP_NODE_REMOVED;
    }
}
