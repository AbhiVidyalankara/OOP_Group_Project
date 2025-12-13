package notifications;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class PatientNotification {
    public String notificationId;
    public String patientId;
    public String patientName;
    public String message;
    public String category;
    public LocalDateTime timeCreated;
    public boolean read;

    public PatientNotification(String patientId, String patientName, String message, String category) {
        this.notificationId = UUID.randomUUID().toString();
        this.patientId = patientId;
        this.patientName = patientName;
        this.message = message;
        this.category = category;
        this.timeCreated = LocalDateTime.now();
        this.read = false;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getMessage() {
        return message;
    }

    public String getCategory() {
        return category;
    }

    public boolean isRead() {
        return read;
    }

    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String text = timeCreated.format(formatter);
        return text;
    }

    public void markAsRead() {
        this.read = true;
    }
}