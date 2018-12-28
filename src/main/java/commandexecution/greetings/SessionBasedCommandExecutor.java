package commandexecution.greetings;

import communication.Session;

public class SessionBasedCommandExecutor {
    Session session;

    SessionBasedCommandExecutor(final Session session) {
        this.session = session;
    }
}
