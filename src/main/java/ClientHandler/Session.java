package ClientHandler;

import java.util.UUID;

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

    public void setClientName(String name) {
        this.clientName = name;
    }

    public String getClientName() {
        return clientName;
    }
}
