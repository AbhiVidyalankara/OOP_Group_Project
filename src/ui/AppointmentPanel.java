package ui;

import model.Appointment;
import model.Doctor;
import model.Patient;
import service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AppointmentPanel extends JPanel {
    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    private JComboBox<Patient> patientCombo;
    private JComboBox<Doctor> doctorCombo;
    private JComboBox<String> statusCombo;
    private JTextField dateTimeField;
    private int selectedAppointmentId = -1;

    public AppointmentPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(236, 240, 241));

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(230, 126, 34), 2),
                "Appointment Details",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(52, 73, 94)));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(createLabel("Patient:"));
        patientCombo = new JComboBox<>();
        patientCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loadPatients();
        formPanel.add(patientCombo);

        formPanel.add(createLabel("Doctor:"));
        doctorCombo = new JComboBox<>();
        doctorCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loadDoctors();
        formPanel.add(doctorCombo);

        formPanel.add(createLabel("Date/Time (yyyy-MM-dd HH:mm):"));
        dateTimeField = new JTextField();
        dateTimeField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateTimeField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        formPanel.add(dateTimeField);

        formPanel.add(createLabel("Status:"));
        statusCombo = new JComboBox<>(new String[]{"Scheduled", "Completed", "Canceled", "Delayed"});
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(statusCombo);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonPanel.setBackground(Color.WHITE);

        JButton scheduleButton = createStyledButton("Schedule", new Color(46, 204, 113));
        JButton updateButton = createStyledButton("Update Status", new Color(52, 152, 219));

        buttonPanel.add(scheduleButton);
        buttonPanel.add(updateButton);
        formPanel.add(buttonPanel);

        String[] columns = {"ID", "Patient", "Doctor", "Date/Time", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        appointmentTable = new JTable(tableModel);
        appointmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        appointmentTable.setRowHeight(25);
        appointmentTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        appointmentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        appointmentTable.getTableHeader().setBackground(new Color(52, 73, 94));
        appointmentTable.getTableHeader().setForeground(Color.WHITE);

        appointmentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = appointmentTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedAppointmentId = (int) tableModel.getValueAt(selectedRow, 0);
                    String statusValue = (String) tableModel.getValueAt(selectedRow, 4);
                    statusCombo.setSelectedItem(statusValue);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(230, 126, 34), 2),
                "Appointment Records",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(52, 73, 94)));

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        scheduleButton.addActionListener(e -> scheduleAppointment());
        updateButton.addActionListener(e -> updateStatus());

        loadAppointmentData();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(52, 73, 94));
        return label;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 35));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadPatients() {
        patientCombo.removeAllItems();
        List<Patient> patients = PatientService.getAllPatients();
        for (Patient p : patients) {
            patientCombo.addItem(p);
        }
    }

    private void loadDoctors() {
        doctorCombo.removeAllItems();
        List<Doctor> doctors = DoctorService.getAllDoctors();
        for (Doctor d : doctors) {
            doctorCombo.addItem(d);
        }
    }

    private void scheduleAppointment() {
        Patient patient = (Patient) patientCombo.getSelectedItem();
        Doctor doctor = (Doctor) doctorCombo.getSelectedItem();
        String dateTimeStr = dateTimeField.getText().trim();
        String status = (String) statusCombo.getSelectedItem();

        if (patient == null || doctor == null) {
            JOptionPane.showMessageDialog(this, "Please select patient and doctor!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);

            AppointmentService.scheduleAppointment(patient.getId(), doctor.getId(), dateTime, status);
            loadAppointmentData();
            JOptionPane.showMessageDialog(this, "Appointment scheduled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date/time format! Use yyyy-MM-dd HH:mm", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStatus() {
        if (selectedAppointmentId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to update!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newStatus = (String) statusCombo.getSelectedItem();
        AppointmentService.updateAppointmentStatus(selectedAppointmentId, newStatus);
        loadAppointmentData();
        JOptionPane.showMessageDialog(this, "Status updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadAppointmentData() {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<Appointment> appointments = AppointmentService.getAllAppointments();
        for (Appointment a : appointments) {
            Patient patient = PatientService.getPatientById(a.getPatientId());
            Doctor doctor = DoctorService.getDoctorById(a.getDoctorId());

            if (patient != null && doctor != null) {
                tableModel.addRow(new Object[]{
                        a.getId(),
                        patient.getName(),
                        doctor.getName(),
                        a.getDateTime().format(formatter),
                        a.getStatus()
                });
            }
        }
    }
}