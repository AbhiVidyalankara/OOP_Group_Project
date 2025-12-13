package notifications;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class DoctorNotification {
    public String notificationId;
    public String doctorId;
    public String patientId;
    public String patientName;
    public String situation;
    public String category;
    public LocalDateTime timeCreated;
    public boolean read;

    public DoctorNotification(String doctorId, String patientId, String patientName, String situation, String category) {
        this.notificationId = UUID.randomUUID().toString();
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.situation = situation;
        this.category = category;
        this.timeCreated = LocalDateTime.now();
        this.read = false;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getSituation() {
        return situation;
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
