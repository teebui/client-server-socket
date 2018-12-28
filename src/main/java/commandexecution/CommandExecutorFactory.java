package commandexecution;

import commandexecution.graph.*;
import commandexecution.greetings.GoodByeCommandExecutor;
import commandexecution.greetings.GreetingCommandExecutor;
import communication.Session;

import static messages.Commands.*;

/**
 * Analyses client commands and picks the right {@link CommandExecutor} instances to handle them.
 * If a command is not recognisable, an {@link DefaultCommandExecutor} instance is used.
 */
public class CommandExecutorFactory {
    public static CommandExecutor getExecutor(final String command, final Session session) {

        if (command.startsWith(CMD_CLIENT_GREETING)) {
            return new GreetingCommandExecutor(command, session);
        } else if (command.equals(CMD_CLIENT_BYE)) {
            return new GoodByeCommandExecutor(session);
        } else if (command.startsWith(CMD_ADD_NODE)) {
            return new AddNodeCommandExecutor(command);
        } else if (command.startsWith(CMD_ADD_EDGE)) {
            return new AddEdgeCommandExecutor(command);
        } else if (command.startsWith(CMD_REMOVE_NODE)) {
            return new RemoveNodeCommandExecutor(command);
        } else if (command.startsWith(CMD_REMOVE_EDGE)) {
            return new RemoveEdgeCommandExecutor(command);
        } else if (command.startsWith(CMD_SHORTEST_PATH)) {
            return new ShortestPathCommandExecutor(command);
        } else if (command.startsWith(CMD_CLOSER_THAN)) {
            return new CloserThanCommandExecutor(command);
        }

        return new DefaultCommandExecutor();
    }
}