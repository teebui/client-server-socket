package libs.CommandExecution;

import libs.Graph;
import libs.NodeNotFoundException;

import static libs.Commands.CMD_ADD_EDGE;
import static libs.Responses.RSP_EDGE_ADDED;
import static libs.Responses.RSP_ERROR_NODE_NOT_FOUND;

public class AddEdgeCommandExecutor implements CommandExecutor {
    private String command;
    private Graph graph;

    public AddEdgeCommandExecutor(final String command, final Graph graph) {
        this.command = command;
        this.graph = graph;
    }

    @Override
    public String getResponse() {
        String edge = command.replace(CMD_ADD_EDGE, "");
        String[] s = edge.split(" ");
        try {
            int weight = Integer.parseInt(s[2]);
            graph.addEdge(s[0], s[1], weight);

        } catch (NodeNotFoundException e) {
            return RSP_ERROR_NODE_NOT_FOUND;
        }

        return RSP_EDGE_ADDED;
    }
}
