package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Notification implements Serializable {
    private int id;
    private String type; // "PATIENT" or "DOCTOR"
    private String recipient;
    private String message;
    private LocalDateTime timestamp;
    private boolean isRead;

    public Notification(int id, String type, String recipient, String message, LocalDateTime timestamp) {
        this.id = id;
        this.type = type;
        this.recipient = recipient;
        this.message = message;
        this.timestamp = timestamp;
        this.isRead = false;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}