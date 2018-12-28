package commandexecution.greetings;

import commandexecution.CommandExecutor;
import communication.Session;

import static java.lang.String.format;
import static messages.Commands.CMD_CLIENT_GREETING;
import static messages.Responses.RSP_SERVER_GREETING;


/**
 * Extracts client name from the command and sends back a greeting with that name
 */
public class GreetingCommandExecutor extends SessionBasedCommandExecutor implements CommandExecutor {

    private final String command;

    public GreetingCommandExecutor(final String command, final Session session) {
        super(session);
        this.command = command;
    }

    @Override
    public String getResponse() {
            final String clientID = command.replace(CMD_CLIENT_GREETING, "");
            session.setClientName(clientID);
            return format(RSP_SERVER_GREETING, clientID);
    }
}