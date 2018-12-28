package communication;

import commandexecution.CommandExecutorFactory;

import static java.lang.String.format;
import static messages.CommandPatterns.BYE;
import static messages.Responses.SERVER_BYE;
import static messages.Responses.SERVER_INTRO;

/**
 * Acts as the real boss of communication, takes the commands, tells {@link CommandExecutorFactory} to find the
 * right command executor and returns its response.
 *
 * Keeps track of the communication between the client and the server using a session.
 *
 * Provides abstraction on messages used.
 */
public class CommunicationManager {

    private Session session;

    public CommunicationManager() {
        this.session = new Session();
    }

    public boolean clientSaysGoodBye(final String command) {
        return command.equals(BYE);
    }

    public String getServerGreeting() {
        return format(SERVER_INTRO, session.getSessionID());
    }

    public String getServerGoodbye() {
        session.terminate();
        return format(SERVER_BYE, session.getClientName(), session.getDuration());
    }

    public String getResponse(final String command) {
        return CommandExecutorFactory.getExecutor(command, session).getResponse();
    }
}