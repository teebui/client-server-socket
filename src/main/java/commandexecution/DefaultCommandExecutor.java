package commandexecution;

import java.util.regex.Matcher;

import static messages.Responses.UNKNOWN_COMMAND;


/**
 * Implements a default behaviour for any command executor object, including the case of unknown command.
 */
public class DefaultCommandExecutor implements CommandExecutor {
    protected Matcher command;

    public DefaultCommandExecutor() {
        this(null);
    }

    public DefaultCommandExecutor(final Matcher command) {
        this.command = command;
    }

    @Override
    public String getResponse() {
        return UNKNOWN_COMMAND;
    }
}