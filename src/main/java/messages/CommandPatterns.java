package messages;

/**
 * List of patterns  the recognised commands sent by clients
 */
public class CommandPatterns {
    private static final String VALID_NAME = "[a-zA-Z0-9-]*";
    public static final String GREETING = "HI, I'M (" + VALID_NAME + ")";
    public static final String BYE = "BYE MATE!";
    public static final String ADD_NODE = "ADD NODE (" + VALID_NAME + ")";
    public static final String REMOVE_NODE = "REMOVE NODE (" + VALID_NAME + ")";
    public static final String ADD_EDGE = "ADD EDGE (" + VALID_NAME + ") (" + VALID_NAME + ") ([0-9]*)";
    public static final String REMOVE_EDGE = "REMOVE EDGE (" + VALID_NAME + ") (" + VALID_NAME + ")";
    public static final String SHORTEST_PATH = "SHORTEST PATH (" + VALID_NAME + ") (" + VALID_NAME + ")";
    public static final String CLOSER_THAN = "CLOSER THAN ([0-9]*) (" + VALID_NAME + ")";
}