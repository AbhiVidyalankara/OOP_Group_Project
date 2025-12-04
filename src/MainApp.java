package main;

import service.*;
import ui.*;

import javax.swing.*;
import java.awt.*;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MediCare Plus - Hospital Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 750);
            frame.setLocationRelativeTo(null);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            tabbedPane.setBackground(new Color(245, 245, 245));

            tabbedPane.addTab("Dashboard", new DashboardPanel());
            tabbedPane.addTab("Patients", new PatientPanel());
            tabbedPane.addTab("Doctors", new DoctorPanel());
            tabbedPane.addTab("Appointments", new AppointmentPanel());
            tabbedPane.addTab("Reports", new ReportPanel());
            tabbedPane.addTab("Notifications", new NotificationPanel());

            JPanel statusPanel = new JPanel(new BorderLayout());
            statusPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
            statusPanel.setBackground(new Color(52, 73, 94));

            JLabel statusLabel = new JLabel("System Ready");
            statusLabel.setForeground(Color.WHITE);
            statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            JLabel notificationIndicator = new JLabel("0 Unread");
            notificationIndicator.setForeground(new Color(46, 204, 113));
            notificationIndicator.setFont(new Font("Segoe UI", Font.BOLD, 12));

            statusPanel.add(statusLabel, BorderLayout.WEST);
            statusPanel.add(notificationIndicator, BorderLayout.EAST);

            Timer timer = new Timer(2000, e -> {
                int count = NotificationService.getUnreadNotifications().size();
                notificationIndicator.setText(count + " Unread");
                notificationIndicator.setForeground(count > 0 ? new Color(231, 76, 60) : new Color(46, 204, 113));
            });
            timer.start();

            frame.add(tabbedPane, BorderLayout.CENTER);
            frame.add(statusPanel, BorderLayout.SOUTH);
            frame.setVisible(true);

            if (PatientService.getAllPatients().isEmpty()) {
                createSampleData();
            }
        });
    }

    private static void createSampleData() {
        PatientService.addPatient(new model.Patient(0, "John Doe", "555-1234", "Hypertension"));
        PatientService.addPatient(new model.Patient(0, "Jane Smith", "555-5678", "Diabetes"));

        DoctorService.addDoctor(new model.Doctor(0, "Dr. Alan Smith", "Cardiology", "Mon-Fri 9AM-5PM"));
        DoctorService.addDoctor(new model.Doctor(0, "Dr. Sarah Johnson", "Neurology", "Tue-Thu 10AM-6PM"));

        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        AppointmentService.scheduleAppointment(1, 1, now.plusDays(1), "Scheduled");
        AppointmentService.scheduleAppointment(2, 2, now.plusDays(2), "Completed");

        NotificationService.addNotification("PATIENT", "John Doe", "Your appointment is confirmed for tomorrow");
        NotificationService.addNotification("DOCTOR", "Dr. Alan Smith", "New appointment with John Doe tomorrow");
    }
}