package communication;

import commandexecution.CommandExecutorFactory;
import graph.Graph;

import static java.lang.String.format;
import static messages.Commands.CMD_CLIENT_BYE;
import static messages.Responses.RSP_SERVER_BYE;
import static messages.Responses.RSP_SERVER_INTRO;


/**
 * Acts as the real boss of communication, takes the commands, tells {@link CommandExecutorFactory} to find the
 * right command executor and returns its response.
 *
 * Provides abstraction on messages used.
 */
public class CommunicationManager {

    private Session session;
    private Graph graph;

    /**
     * Receives a client's command, acts accordingly and dispatches responses
     *
     * @param graph
     */
    public CommunicationManager(final Graph graph) {
        this.graph = graph;
        this.session = new Session();
    }

    public boolean clientSaysGoodBye(final String command) {
        return command.equals(CMD_CLIENT_BYE);
    }

    public String getServerGreeting() {
        return format(RSP_SERVER_INTRO, session.getSessionID());
    }

    public String getServerGoodbye() {
        session.terminate();
        return format(RSP_SERVER_BYE, session.getClientName(), session.getDuration());
    }

    public String getResponse(final String command) {
        return CommandExecutorFactory.getExecutor(command, session, graph).getResponse();
    }
}