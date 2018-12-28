package commandexecution.greetings;

import communication.Session;


/**
 * Is implemented mainly to provide a default constructor for children classes
 */
class SessionBasedCommandExecutor {
    Session session;

    SessionBasedCommandExecutor(final Session session) {
        this.session = session;
    }
}