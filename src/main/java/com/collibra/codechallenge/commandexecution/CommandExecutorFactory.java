package com.collibra.codechallenge.commandexecution;

import com.collibra.codechallenge.commandexecution.graph.*;
import com.collibra.codechallenge.commandexecution.greetings.GoodByeCommandExecutor;
import com.collibra.codechallenge.commandexecution.greetings.GreetingCommandExecutor;
import com.collibra.codechallenge.communication.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.collibra.codechallenge.messages.CommandPatterns.*;

/**
 * Analyses client commands and picks the right {@link CommandExecutor} instances to handle them.
 * If a command is not recognisable, an {@link DefaultCommandExecutor} instance is used.
 */
public class CommandExecutorFactory {

    private static final Logger LOGGER = LogManager.getLogger(CommandExecutorFactory.class);
    public static final String MSG_COMMAND_IS_NOT_SUPPORTED = "Command is not supported.";

    public static CommandExecutor getExecutor(final String command, final Session session) {

        Matcher commandMatcher;

        if ((commandMatcher = getMatcher(command, GREETING)).matches()) {
            return new GreetingCommandExecutor(commandMatcher, session);
        } else if ((getMatcher(command, BYE)).matches()) {
            return new GoodByeCommandExecutor(session);
        } else if ((commandMatcher = getMatcher(command, ADD_NODE)).matches()) {
            return new AddNodeCommandExecutor(commandMatcher);
        } else if ((commandMatcher = getMatcher(command, ADD_EDGE)).matches()) {
            return new AddEdgeCommandExecutor(commandMatcher);
        } else if ((commandMatcher = getMatcher(command, REMOVE_NODE)).matches()) {
            return new RemoveNodeCommandExecutor(commandMatcher);
        } else if ((commandMatcher = getMatcher(command, REMOVE_EDGE)).matches()) {
            return new RemoveEdgeCommandExecutor(commandMatcher);
        } else if ((commandMatcher = getMatcher(command, SHORTEST_PATH)).matches()) {
            return new ShortestPathCommandExecutor(commandMatcher);
        } else if ((commandMatcher = getMatcher(command, CLOSER_THAN)).matches()) {
            return new CloserThanCommandExecutor(commandMatcher);
        }

        LOGGER.warn(MSG_COMMAND_IS_NOT_SUPPORTED);
        return new DefaultCommandExecutor();
    }

    private static Matcher getMatcher(final String command, final String regex) {
        return Pattern.compile(regex).matcher(command);
    }
}