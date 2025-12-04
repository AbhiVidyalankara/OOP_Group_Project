package notifications;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            List<String> doctorIds = Arrays.asList("DOC001", "DOC002", "DOC003");

            JFrame frame = new JFrame("MediCare Plus - Doctor Notifications");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 500);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout(10, 10));

            DoctorNotificationPanel notificationPanel = new DoctorNotificationPanel(doctorIds);
            frame.add(notificationPanel, BorderLayout.CENTER);

            PatientService patientService = new PatientService();
            AddPatientPanel addPatientPanel = new AddPatientPanel(doctorIds, patientService);
            frame.add(addPatientPanel, BorderLayout.EAST);

            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton refreshButton = new JButton("Refresh Notifications For Doctor");
            topPanel.add(refreshButton);
            frame.add(topPanel, BorderLayout.NORTH);

            refreshButton.addActionListener((ActionEvent e) -> {
                JComboBox<String> dropdown = null;
                try {
                    java.lang.reflect.Field field = DoctorNotificationPanel.class.getDeclaredField("doctorDropdown");
                    field.setAccessible(true);
                    dropdown = (JComboBox<String>) field.get(notificationPanel);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Unable to access doctor selection.");
                    return;
                }

                if (dropdown != null && dropdown.getSelectedItem() != null) {
                    String selectedDoctor = (String) dropdown.getSelectedItem();
                    notificationPanel.loadNotifications(selectedDoctor);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a doctor in the notifications panel.");
                }
            });

            frame.setVisible(true);
        });
    }
}
