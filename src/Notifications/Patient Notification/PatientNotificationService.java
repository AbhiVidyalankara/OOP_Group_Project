package notifications;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class PatientNotificationService {

    private static List<PatientNotification> notifications = new ArrayList<>();

    public void sendPatientNotification(String patientId, String patientName, String message, String category) {
        PatientNotification newNotification = new PatientNotification(patientId, patientName, message, category);
        notifications.add(0, newNotification);

        String displayMessage = "Patient: " + patientName + "\n"
                + "Message: " + message;

        JOptionPane.showMessageDialog(
                null,
                displayMessage,
                "Patient Alert: " + category,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public List<PatientNotification> getForPatient(String patientId) {
        List<PatientNotification> patientNotes = new ArrayList<>();

        int i = 0;
        while (i < notifications.size()) {
            PatientNotification n = notifications.get(i);
            if (n.getPatientId().equals(patientId)) {
                patientNotes.add(n);
            }
            i = i + 1;
        }

        return patientNotes;
    }

    public void markAsRead(String notificationId) {
        int i = 0;
        while (i < notifications.size()) {
            PatientNotification n = notifications.get(i);
            if (n.notificationId.equals(notificationId)) {
                n.markAsRead();
                return;
            }
            i = i + 1;
        }
    }
}