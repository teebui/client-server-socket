package libs.CommandExecution;

import static libs.Responses.RSP_UNKNOWN_REQUEST;

public class DefaultCommandExecutor implements CommandExecutor {
    @Override
    public String getResponse() {
        return RSP_UNKNOWN_REQUEST;
    }
}
