package com.collibra.codechallenge.commandexecution.graph;

import com.collibra.codechallenge.commandexecution.DefaultCommandExecutor;
import com.collibra.codechallenge.exceptions.NodeNotFoundException;
import com.collibra.codechallenge.graph.Graph;

import java.util.regex.Matcher;

import static com.collibra.codechallenge.messages.Responses.ERROR_NODE_NOT_FOUND;

/**
 * Extracts a node name and a weight (distance) from the command and returns a list of all nodes whose weights are
 * smaller than the given weight.
 * Returns an error message if the given node does not yet exist.
 */
public class CloserThanCommandExecutor extends DefaultCommandExecutor {

    public CloserThanCommandExecutor(final Matcher command) {
        super(command);
    }

    @Override
    public String getResponse() {
        final int weight = Integer.parseInt(command.group(1));
        final String node = command.group(2);

        String closerNodes;
        try {
            closerNodes = Graph.getInstance().findNodesCloserThan(weight, node);
        } catch (final NodeNotFoundException e) {
            return ERROR_NODE_NOT_FOUND;
        }

        return closerNodes;
    }
}