package commandexecution.graph;

import commandexecution.CommandExecutor;
import graph.Graph;
import exceptions.NodeNotFoundException;

import static messages.Commands.CMD_REMOVE_NODE;
import static messages.Responses.RSP_ERROR_NODE_NOT_FOUND;
import static messages.Responses.RSP_NODE_REMOVED;

public class RemoveNodeCommandExecutor implements CommandExecutor {
    private final String command;

    public RemoveNodeCommandExecutor(final String command) {
        this.command = command;
    }

    @Override
    public String getResponse() {
        String node = command.replace(CMD_REMOVE_NODE, "");
        try {
            Graph.getInstance().removeNode(node);
        } catch (NodeNotFoundException e) {
            return RSP_ERROR_NODE_NOT_FOUND;
        }

        return RSP_NODE_REMOVED;
    }
}
