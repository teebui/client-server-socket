package com.collibra.codechallenge.commandexecution.greetings;

import com.collibra.codechallenge.commandexecution.CommandExecutor;
import com.collibra.codechallenge.communication.Session;

import static java.lang.String.format;
import static com.collibra.codechallenge.messages.Responses.SERVER_BYE;

/**
 * Terminates the current session of a client and sends a goodbye with the duration of the communication
 */
public class GoodByeCommandExecutor extends SessionBasedCommandExecutor implements CommandExecutor {

    public GoodByeCommandExecutor(final Session session) {
        super(session);
    }

    @Override
    public String getResponse() {
        return format(SERVER_BYE, session.getClientName(), session.getDuration());
    }
}