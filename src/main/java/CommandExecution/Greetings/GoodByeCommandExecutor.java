package CommandExecution.Greetings;

import CommandExecution.CommandExecutor;
import ClientHandler.Session;

import static java.lang.String.format;
import static Messages.Responses.RSP_SERVER_BYE;

public class GoodByeCommandExecutor implements CommandExecutor {

    private Session session;

    public GoodByeCommandExecutor(final Session session) {
        this.session = session;
    }

    @Override
    public String getResponse() {
        session.terminate();
        return format(RSP_SERVER_BYE, session.getClientName(), session.getDuration());
    }
}
