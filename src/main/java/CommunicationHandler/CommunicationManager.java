package CommunicationHandler;

import ClientHandler.Session;
import CommandExecution.CommandExecutorFactory;
import Graph.Graph;

import static java.lang.String.format;
import static Messages.Commands.CMD_CLIENT_BYE;
import static Messages.Responses.RSP_SERVER_BYE;
import static Messages.Responses.RSP_SERVER_INTRO;


public class CommunicationManager {

    private Session session;
    private CommandExecutorFactory factory;

    /**
     * Receives a client's command, acts accordingly and dispatches responses
     * @param session session information of the current client
     * @param graph
     */
    public CommunicationManager(final Session session, final Graph graph) {

        this.session = session;
        this.factory = new CommandExecutorFactory(session, graph);
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
        return factory.getExecutor(command).getResponse();
    }
}