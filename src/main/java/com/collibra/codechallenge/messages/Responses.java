package com.collibra.codechallenge.messages;

/**
 * List of all response templates for the server to send back to clients.
 */
public class Responses {
    public static final String SERVER_INTRO = "HI, I'M %s";
    public static final String SERVER_GREETING = "HI %s";
    public static final String SERVER_BYE = "BYE %s, WE SPOKE FOR %d MS";
    public static final String UNKNOWN_COMMAND = "SORRY, I DIDN'T UNDERSTAND THAT";
    public static final String ERROR_NODE_ALREADY_EXISTS = "ERROR: NODE ALREADY EXISTS";
    public static final String NODE_ADDED = "NODE ADDED";
    public static final String ERROR_NODE_NOT_FOUND = "ERROR: NODE NOT FOUND";
    public static final String EDGE_REMOVED = "EDGE REMOVED";
    public static final String EDGE_ADDED = "EDGE ADDED";
    public static final String NODE_REMOVED = "NODE REMOVED";
}