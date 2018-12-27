package libs;

import libs.CommandExecution.CommandExecutorFactory;

import static java.lang.String.format;
import static libs.Commands.CMD_CLIENT_BYE;
import static libs.Responses.RSP_SERVER_BYE;
import static libs.Responses.RSP_SERVER_INTRO;


public class CommunicationManager {

    private Session session;
    private Graph graph;

    /**
     * Receives a client's command, acts accordingly and dispatches responses
     * @param session session information of the current client
     * @param graph
     */
    public CommunicationManager(final Session session, final Graph graph) {

        this.session = session;
        this.graph = graph;
    }

    public boolean clientSaysGoodBye(String command) {
        return command.equals(CMD_CLIENT_BYE);
    }

    public String getServerGreeting() {
        return format(RSP_SERVER_INTRO, session.getSessionID());
    }

    public String getServerGoodbye() {
        session.terminate();
        return format(RSP_SERVER_BYE, session.getClientName(), session.getDuration());
    }

    public String getResponse(String command) {
        return CommandExecutorFactory.getExecutor(command, session, graph).getResponse();
    }
}
