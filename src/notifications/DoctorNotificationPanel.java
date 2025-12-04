package notifications;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorNotificationPanel extends JPanel {

    private NotificationService notificationService = new NotificationService();
    private DefaultTableModel notificationTableModel;
    private JTable notificationTable;
    private JComboBox<String> doctorDropdown;
    private String selectedDoctorId = null;

    public DoctorNotificationPanel(List<String> doctorIds) {
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        doctorDropdown = new JComboBox<>(doctorIds.toArray(new String[0]));

        doctorDropdown.addActionListener(e -> {
            selectedDoctorId = (String) doctorDropdown.getSelectedItem();
            if (selectedDoctorId != null) {
                loadNotifications(selectedDoctorId);
            }
        });

        JLabel doctorLabel = new JLabel("Select Doctor ID:");
        headerPanel.add(doctorLabel);
        headerPanel.add(doctorDropdown);
        add(headerPanel, BorderLayout.NORTH);

        String[] columnNames = {"Time", "Status", "Category", "Patient ID", "Patient Name", "Situation"};

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
            if (selectedDoctorId != null) {
                loadNotifications(selectedDoctorId);
            }
        });

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.add(refreshButton);
        footerPanel.add(markReadButton);
        add(footerPanel, BorderLayout.SOUTH);
    }

    public void loadNotifications(String doctorId) {
        if (doctorId == null) {
            return;
        }

        notificationTableModel.setRowCount(0);

        List<DoctorNotification> notifications = notificationService.getForDoctor(doctorId);

        int i = 0;
        while (i < notifications.size()) {
            DoctorNotification note = notifications.get(i);

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
                    note.getPatientId(),
                    note.getPatientName(),
                    note.getSituation()
            });

            i = i + 1;
        }
    }

    private void markSelectedAsRead() {
        int selectedRow = notificationTable.getSelectedRow();
        if (selectedRow != -1) {
            if (selectedDoctorId != null) {
                String noteTimeString = (String) notificationTableModel.getValueAt(selectedRow, 0);

                String noteIdToMark = null;
                List<DoctorNotification> notifications = notificationService.getForDoctor(selectedDoctorId);

                int i = 0;
                while (i < notifications.size()) {
                    DoctorNotification n = notifications.get(i);
                    String timeString = n.getFormattedTime();
                    if (timeString.equals(noteTimeString)) {
                        noteIdToMark = n.notificationId;
                        break;
                    }
                    i = i + 1;
                }

                if (noteIdToMark != null) {
                    notificationService.markAsRead(noteIdToMark);
                    loadNotifications(selectedDoctorId);
                }
            }
        }
    }
}
