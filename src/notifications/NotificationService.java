package notifications;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {

    private static List<DoctorNotification> notifications = new ArrayList<>();

    public void sendDoctorNotification(String doctorId, String patientId, String patientName, String situation, String category) {
        DoctorNotification newNotification = new DoctorNotification(doctorId, patientId, patientName, situation, category);
        notifications.add(0, newNotification);

        String message = "Patient ID: " + patientId + "\n"
                + "Name: " + patientName + "\n"
                + "Situation: " + situation;

        JOptionPane.showMessageDialog(
                null,
                message,
                "Doctor Alert: " + category,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public List<DoctorNotification> getForDoctor(String doctorId) {
        List<DoctorNotification> doctorNotes = new ArrayList<>();

        int i = 0;
        while (i < notifications.size()) {
            DoctorNotification n = notifications.get(i);
            if (n.getDoctorId().equals(doctorId)) {
                doctorNotes.add(n);
            }
            i = i + 1;
        }

        return doctorNotes;
    }

    public void markAsRead(String notificationId) {
        int i = 0;
        while (i < notifications.size()) {
            DoctorNotification n = notifications.get(i);
            if (n.notificationId.equals(notificationId)) {
                n.markAsRead();
                return;
            }
            i = i + 1;
        }
    }
}