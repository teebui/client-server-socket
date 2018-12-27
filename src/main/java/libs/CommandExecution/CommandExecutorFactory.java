package libs.CommandExecution;

import libs.Graph;
import libs.Session;

import static libs.Commands.*;

public class CommandExecutorFactory {

    private final Session session;
    private final Graph graph;

    public CommandExecutorFactory(final Session session, final Graph graph) {

        this.session = session;
        this.graph = graph;
    }

    public CommandExecutor getExecutor(final String command) {

        if (command.startsWith(CMD_CLIENT_GREETING)) {
            return new GreetingCommandExecutor(command, session);
        } else if (command.equals(CMD_CLIENT_BYE)) {
            return new GoodByeCommandExecutor(session);
        } else if (command.startsWith(CMD_ADD_NODE)) {
            return new AddNodeCommandExecutor(command, graph);
        } else if (command.startsWith(CMD_ADD_EDGE)) {
            return new AddEdgeCommandExecutor(command, graph);
        } else if (command.startsWith(CMD_REMOVE_NODE)) {
            return new RemoveNodeCommandExecutor(command, graph);
        } else if (command.startsWith(CMD_REMOVE_EDGE)) {
            return new RemoveEdgeCommandExecutor(command, graph);
        } else if (command.startsWith(CMD_SHORTEST_PATH)) {
            return new ShortestPathCommandExecutor(command, graph);
        } else if (command.startsWith(CMD_CLOSER_THAN)) {
            return new CloserThanCommandExecutor(command, graph);
        }

        return new DefaultCommandExecutor();
    }
}
