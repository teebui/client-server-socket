package com.collibra.codechallenge.commandexecution.graph;

import com.collibra.codechallenge.commandexecution.DefaultCommandExecutor;
import com.collibra.codechallenge.exceptions.NodeNotFoundException;
import com.collibra.codechallenge.graph.Graph;

import java.util.regex.Matcher;

import static java.lang.String.format;
import static com.collibra.codechallenge.messages.Responses.ERROR_NODE_NOT_FOUND;

/**
 * Extracts two node names from the given command and find the shortest path (i.e. path with minimal total weight)
 * between the two nodes.
 * Returns an error message if any the given nodes does not exist.
 */
public class ShortestPathCommandExecutor extends DefaultCommandExecutor {

    public ShortestPathCommandExecutor(final Matcher command) {
        super(command);
    }

    @Override
    public String getResponse() {

        final String sourceNode = command.group(1);
        final String targetNode = command.group(2);

        int shortestPath;
        try {
            shortestPath = Graph.getInstance().getShortestPath(sourceNode, targetNode);
        } catch (final NodeNotFoundException e) {
            return ERROR_NODE_NOT_FOUND;
        }

        return format("%d", shortestPath);
    }
}