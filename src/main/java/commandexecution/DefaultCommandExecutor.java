package commandexecution;

import static messages.Responses.RSP_UNKNOWN_REQUEST;

public class DefaultCommandExecutor implements CommandExecutor {
    protected String command;

    public DefaultCommandExecutor() {
        this(null);
    }

    public DefaultCommandExecutor(final String command) {
        this.command = command;
    }

    @Override
    public String getResponse() {
        return RSP_UNKNOWN_REQUEST;
    }
}
