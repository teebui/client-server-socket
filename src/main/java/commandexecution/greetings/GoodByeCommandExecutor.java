package commandexecution.greetings;

import commandexecution.CommandExecutor;
import communication.Session;

import static java.lang.String.format;
import static messages.Responses.SERVER_BYE;


/**
 * Terminates the current session of a client and sends a goodbye with the duration of the communication
 */
public class GoodByeCommandExecutor extends SessionBasedCommandExecutor implements CommandExecutor {

    public GoodByeCommandExecutor(final Session session) {
        super(session);
    }

    @Override
    public String getResponse() {
        session.terminate();
        return format(SERVER_BYE, session.getClientName(), session.getDuration());
    }
}