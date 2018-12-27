package CommandExecution.Greetings;

import CommandExecution.CommandExecutor;
import ClientHandler.Session;

import static java.lang.String.format;
import static Messages.Commands.CMD_CLIENT_GREETING;
import static Messages.Responses.RSP_SERVER_GREETING;

public class GreetingCommandExecutor implements CommandExecutor {

    private final String command;
    private final Session session;

    public GreetingCommandExecutor(final String command, final Session session) {

        this.command = command;
        this.session = session;
    }

    @Override
    public String getResponse() {
            final String clientID = command.replace(CMD_CLIENT_GREETING, "");
            session.setClientName(clientID);
            return format(RSP_SERVER_GREETING, clientID);
    }
}
