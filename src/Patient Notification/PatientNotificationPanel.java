package notifications;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientNotificationPanel extends JPanel {

    private PatientNotificationService patientNotificationService = new PatientNotificationService();
    private DefaultTableModel notificationTableModel;
    private JTable notificationTable;
    private JComboBox<String> patientDropdown;
    private String selectedPatientId = null;

    public PatientNotificationPanel(List<String> patientIds) {
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        patientDropdown = new JComboBox<>(patientIds.toArray(new String[0]));

        patientDropdown.addActionListener(e -> {
            selectedPatientId = (String) patientDropdown.getSelectedItem();
            if (selectedPatientId != null) {
                loadNotifications(selectedPatientId);
            }
        });

        JLabel patientLabel = new JLabel("Select Patient ID:");
        headerPanel.add(patientLabel);
        headerPanel.add(patientDropdown);
        add(headerPanel, BorderLayout.NORTH);

        String[] columnNames = {"Time", "Status", "Category", "Message"};

        notificationTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        notificationTable = new JTable(notificationTableModel);
        JScrollPane scrollPane = new JScrollPane(notificationTable);
        add(scrollPane, BorderLayout.CENTER);

        JButton markReadButton = new JButton("Mark Selected as Read");
        markReadButton.addActionListener(e -> markSelectedAsRead());

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            if (selectedPatientId != null) {
                loadNotifications(selectedPatientId);
            }
        });

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.add(refreshButton);
        footerPanel.add(markReadButton);
        add(footerPanel, BorderLayout.SOUTH);
    }

    public void loadNotifications(String patientId) {
        if (patientId == null) {
            return;
        }

        notificationTableModel.setRowCount(0);

        List<PatientNotification> notifications = patientNotificationService.getForPatient(patientId);

        int i = 0;
        while (i < notifications.size()) {
            PatientNotification note = notifications.get(i);

            String status;
            if (note.isRead()) {
                status = "Read";
            } else {
                status = "Unread";
            }

            notificationTableModel.addRow(new Object[]{
                    note.getFormattedTime(),
                    status,
                    note.getCategory(),
                    note.getMessage()
            });

            i = i + 1;
        }
    }

    private void markSelectedAsRead() {
        int selectedRow = notificationTable.getSelectedRow();
        if (selectedRow != -1) {
            if (selectedPatientId != null) {
                String noteTimeString = (String) notificationTableModel.getValueAt(selectedRow, 0);

                String noteIdToMark = null;
                List<PatientNotification> notifications = patientNotificationService.getForPatient(selectedPatientId);

                int i = 0;
                while (i < notifications.size()) {
                    PatientNotification n = notifications.get(i);
                    String timeString = n.getFormattedTime();
                    if (timeString.equals(noteTimeString)) {
                        noteIdToMark = n.notificationId;
                        break;
                    }
                    i = i + 1;
                }

                if (noteIdToMark != null) {
                    patientNotificationService.markAsRead(noteIdToMark);
                    loadNotifications(selectedPatientId);
                }
            }
        }
    }
}