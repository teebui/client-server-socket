package commandexecution.graph;

import commandexecution.CommandExecutor;
import graph.Graph;
import graph.NodeNotFoundException;

import static java.lang.String.format;
import static messages.Commands.CMD_SHORTEST_PATH;
import static messages.Responses.RSP_ERROR_NODE_NOT_FOUND;

public class ShortestPathCommandExecutor implements CommandExecutor {
    private final String command;
    private final Graph graph;

    public ShortestPathCommandExecutor(final String command, final Graph graph) {
        this.command = command;
        this.graph = graph;
    }

    @Override
    public String getResponse() {
        String nodes = command.replace(CMD_SHORTEST_PATH, "");
        String[] s = nodes.split(" ");
        int shortestPath;
        try {
            shortestPath = graph.getShortestPath(s[0], s[1]);
        } catch (NodeNotFoundException e) {
            return RSP_ERROR_NODE_NOT_FOUND;
        }

        return format("%d", shortestPath);
    }
}
