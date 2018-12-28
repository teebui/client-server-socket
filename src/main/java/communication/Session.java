package communication;

import java.util.UUID;


/**
 * Represents a session during which the server and client are communicating. One session is identified by
 * a unique session ID. Other pieces of information include: client name, start time, end time.
 */
public class Session {
    private final UUID sessionID;
    private String clientName;
    private final long startTime;
    private long endTime;

    public Session() {
        sessionID = UUID.randomUUID();
        startTime = System.currentTimeMillis();
    }

    public UUID getSessionID() {
        return sessionID;
    }

    public void terminate() {
        endTime = System.currentTimeMillis();
    }

    public long getDuration() {
        return endTime - startTime;
    }

    public void setClientName(final String name) {
        this.clientName = name;
    }

    public String getClientName() {
        return clientName;
    }
}