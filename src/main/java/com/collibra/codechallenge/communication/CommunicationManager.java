package com.collibra.codechallenge.communication;

import com.collibra.codechallenge.commandexecution.CommandExecutorFactory;

import static java.lang.String.format;
import static com.collibra.codechallenge.messages.CommandPatterns.BYE;
import static com.collibra.codechallenge.messages.Responses.SERVER_BYE;
import static com.collibra.codechallenge.messages.Responses.SERVER_INTRO;

/**
 * Acts as the real boss of communication, takes the commands, tells {@link CommandExecutorFactory} to find the
 * right command executor and returns its response.
 *
 * Keeps track of the communication between the client and the server using a session.
 *
 * Provides abstraction on messages used.
 */
public class CommunicationManager {

    private Session session = new Session();

    public boolean clientSaysGoodBye(final String command) {
        return command.equals(BYE);
    }

    public String getServerGreeting() {
        return format(SERVER_INTRO, session.getSessionID());
    }

    public String getServerGoodbye() {
        return format(SERVER_BYE, session.getClientName(), session.getDuration());
    }

    public String getResponse(final String command) {
        return CommandExecutorFactory.getExecutor(command, session).getResponse();
    }
}