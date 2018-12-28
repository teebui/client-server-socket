package com.collibra.codechallenge.commandexecution.greetings;

import com.collibra.codechallenge.commandexecution.CommandExecutor;
import com.collibra.codechallenge.communication.Session;

import java.util.regex.Matcher;

import static java.lang.String.format;
import static com.collibra.codechallenge.messages.Responses.SERVER_GREETING;

/**
 * Extracts client name from the command and sends back a greeting with that name.
 * Saves client ID into the session.
 */
public class GreetingCommandExecutor extends SessionBasedCommandExecutor implements CommandExecutor {

    private final Matcher command;

    public GreetingCommandExecutor(final Matcher command, final Session session) {
        super(session);
        this.command = command;
    }

    @Override
    public String getResponse() {
        final String clientID = command.group(1);
        session.setClientName(clientID);

        return format(SERVER_GREETING, clientID);
    }
}