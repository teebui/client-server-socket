package commandexecution.greetings;

import communication.Session;

class SessionBasedCommandExecutor {
    Session session;

    SessionBasedCommandExecutor(final Session session) {
        this.session = session;
    }
}
