package libs.CommandExecution;

import libs.Graph;
import libs.NodeNotFoundException;

import static libs.Commands.CMD_CLOSER_THAN;
import static libs.Responses.RSP_ERROR_NODE_NOT_FOUND;

public class CloserThanCommandExecutor implements CommandExecutor {
    private final String command;
    private final Graph graph;

    public CloserThanCommandExecutor(final String command, final Graph graph) {
        this.command = command;
        this.graph = graph;
    }

    @Override
    public String getResponse() {
        String nodes = command.replace(CMD_CLOSER_THAN, "");
        String[] s = nodes.split(" ");
        String closerNodes;
        try {
            closerNodes = graph.findNodesCloserThan(Integer.parseInt(s[0]), s[1]);
        } catch (NodeNotFoundException e) {
            return RSP_ERROR_NODE_NOT_FOUND;
        }

        return closerNodes;
    }
}
