package ui;

import model.Doctor;
import model.Patient;
import service.*;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    public DashboardPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(236, 240, 241));

        JLabel titleLabel = new JLabel("MediCare Plus Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(44, 62, 80));
        add(titleLabel, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(new Color(236, 240, 241));

        JPanel patientPanel = createStatPanel("Total Patients", String.valueOf(PatientService.getAllPatients().size()),
                new Color(52, 152, 219), new Color(41, 128, 185));
        statsPanel.add(patientPanel);

        JPanel doctorPanel = createStatPanel("Total Doctors", String.valueOf(DoctorService.getAllDoctors().size()),
                new Color(46, 204, 113), new Color(39, 174, 96));
        statsPanel.add(doctorPanel);

        JPanel appointmentPanel = createStatPanel("Total Appointments",
                String.valueOf(AppointmentService.getAllAppointments().size()),
                new Color(230, 126, 34), new Color(211, 84, 0));
        statsPanel.add(appointmentPanel);

        int unreadCount = NotificationService.getUnreadNotifications().size();
        JPanel notificationPanel = createStatPanel("Unread Notifications", String.valueOf(unreadCount),
                unreadCount > 0 ? new Color(231, 76, 60) : new Color(149, 165, 166),
                unreadCount > 0 ? new Color(192, 57, 43) : new Color(127, 140, 141));
        statsPanel.add(notificationPanel);

        add(statsPanel, BorderLayout.CENTER);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        actionsPanel.setBackground(new Color(236, 240, 241));

        JButton newPatientBtn = createStyledButton("Add New Patient", new Color(52, 152, 219));
        newPatientBtn.addActionListener(e -> showAddPatientDialog());
        actionsPanel.add(newPatientBtn);

        JButton newAppointmentBtn = createStyledButton("Schedule Appointment", new Color(46, 204, 113));
        newAppointmentBtn.addActionListener(e -> showScheduleAppointmentDialog());
        actionsPanel.add(newAppointmentBtn);

        JButton viewReportsBtn = createStyledButton("View Reports", new Color(230, 126, 34));
        viewReportsBtn.addActionListener(e -> {
            JTabbedPane parent = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class, this);
            if (parent != null) {
                parent.setSelectedIndex(4);
            }
        });
        actionsPanel.add(viewReportsBtn);

        add(actionsPanel, BorderLayout.SOUTH);
    }

    private JPanel createStatPanel(String title, String value, Color bgColor, Color textColor) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2, true));
        panel.setBackground(bgColor);
        panel.setPreferredSize(new Dimension(250, 140));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setForeground(Color.WHITE);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(180, 45));
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void showAddPatientDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Patient", true);
        dialog.setLayout(new BorderLayout(15, 15));
        dialog.setSize(450, 280);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);

        JTextField nameField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField historyField = new JTextField();

        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        contactField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        historyField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        formPanel.add(createLabel("Patient Name:"));
        formPanel.add(nameField);
        formPanel.add(createLabel("Contact Number:"));
        formPanel.add(contactField);
        formPanel.add(createLabel("Medical History:"));
        formPanel.add(historyField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        buttonPanel.setBackground(Color.WHITE);

        JButton saveBtn = createStyledButton("Save Patient", new Color(46, 204, 113));
        JButton cancelBtn = createStyledButton("Cancel", new Color(149, 165, 166));

        saveBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();

            if (name.isEmpty() || contact.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Name and Contact are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PatientService.addPatient(new model.Patient(0, name, contact, historyField.getText().trim()));
            JOptionPane.showMessageDialog(dialog, "Patient added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            refreshDashboard();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showScheduleAppointmentDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Schedule Appointment", true);
        dialog.setLayout(new BorderLayout(15, 15));
        dialog.setSize(550, 420);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);

        JComboBox<Patient> patientCombo = new JComboBox<>();
        PatientService.getAllPatients().forEach(p -> patientCombo.addItem(p));
        patientCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JComboBox<Doctor> doctorCombo = new JComboBox<>();
        DoctorService.getAllDoctors().forEach(d -> doctorCombo.addItem(d));
        doctorCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JComboBox<String> specialtyCombo = new JComboBox<>(new String[]{"Cardiology", "Neurology", "General", "Pediatrics", "Dermatology"});
        specialtyCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTextField dateTimeField = new JTextField();
        dateTimeField.setText(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        dateTimeField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Scheduled", "Completed", "Canceled", "Delayed"});
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        formPanel.add(createLabel("Patient:"));
        formPanel.add(patientCombo);
        formPanel.add(createLabel("Doctor:"));
        formPanel.add(doctorCombo);
        formPanel.add(createLabel("Specialty (Auto-assign):"));
        formPanel.add(specialtyCombo);
        formPanel.add(createLabel("Date/Time (yyyy-MM-dd HH:mm):"));
        formPanel.add(dateTimeField);
        formPanel.add(createLabel("Status:"));
        formPanel.add(statusCombo);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        buttonPanel.setBackground(Color.WHITE);

        JButton autoAssignBtn = createStyledButton("Auto-Assign Doctor", new Color(52, 152, 219));
        JButton scheduleBtn = createStyledButton("Schedule", new Color(46, 204, 113));
        JButton cancelBtn = createStyledButton("Cancel", new Color(149, 165, 166));

        autoAssignBtn.addActionListener(e -> {
            String specialty = (String) specialtyCombo.getSelectedItem();
            Doctor assignedDoctor = DoctorService.assignDoctor(specialty);

            if (assignedDoctor != null) {
                doctorCombo.setSelectedItem(assignedDoctor);
                JOptionPane.showMessageDialog(dialog,
                        "Doctor assigned: " + assignedDoctor.getName() +
                                "\nSpecialty: " + assignedDoctor.getSpecialty() +
                                "\nAvailability: " + assignedDoctor.getAvailability(),
                        "Auto-Assignment Complete", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "No available doctors for specialty: " + specialty,
                        "Assignment Failed", JOptionPane.WARNING_MESSAGE);
            }
        });

        scheduleBtn.addActionListener(e -> {
            try {
                Patient patient = (Patient) patientCombo.getSelectedItem();
                Doctor doctor = (Doctor) doctorCombo.getSelectedItem();
                String dateTimeStr = dateTimeField.getText().trim();
                String status = (String) statusCombo.getSelectedItem();

                if (patient == null || doctor == null) {
                    JOptionPane.showMessageDialog(dialog, "Please select valid patient and doctor!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                java.time.LocalDateTime dateTime = java.time.LocalDateTime.parse(dateTimeStr, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                AppointmentService.scheduleAppointment(patient.getId(), doctor.getId(), dateTime, status);
                JOptionPane.showMessageDialog(dialog, "Appointment scheduled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                refreshDashboard();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid date/time format! Use yyyy-MM-dd HH:mm", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(autoAssignBtn);
        buttonPanel.add(scheduleBtn);
        buttonPanel.add(cancelBtn);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(52, 73, 94));
        return label;
    }

    private void refreshDashboard() {
        removeAll();
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(236, 240, 241));

        JLabel titleLabel = new JLabel("MediCare Plus Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(44, 62, 80));
        add(titleLabel, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(new Color(236, 240, 241));

        JPanel patientPanel = createStatPanel("Total Patients", String.valueOf(PatientService.getAllPatients().size()),
                new Color(52, 152, 219), new Color(41, 128, 185));
        statsPanel.add(patientPanel);

        JPanel doctorPanel = createStatPanel("Total Doctors", String.valueOf(DoctorService.getAllDoctors().size()),
                new Color(46, 204, 113), new Color(39, 174, 96));
        statsPanel.add(doctorPanel);

        JPanel appointmentPanel = createStatPanel("Total Appointments",
                String.valueOf(AppointmentService.getAllAppointments().size()),
                new Color(230, 126, 34), new Color(211, 84, 0));
        statsPanel.add(appointmentPanel);

        int unreadCount = NotificationService.getUnreadNotifications().size();
        JPanel notificationPanel = createStatPanel("Unread Notifications", String.valueOf(unreadCount),
                unreadCount > 0 ? new Color(231, 76, 60) : new Color(149, 165, 166),
                unreadCount > 0 ? new Color(192, 57, 43) : new Color(127, 140, 141));
        statsPanel.add(notificationPanel);

        add(statsPanel, BorderLayout.CENTER);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        actionsPanel.setBackground(new Color(236, 240, 241));

        JButton newPatientBtn = createStyledButton("Add New Patient", new Color(52, 152, 219));
        newPatientBtn.addActionListener(e -> showAddPatientDialog());
        actionsPanel.add(newPatientBtn);

        JButton newAppointmentBtn = createStyledButton("Schedule Appointment", new Color(46, 204, 113));
        newAppointmentBtn.addActionListener(e -> showScheduleAppointmentDialog());
        actionsPanel.add(newAppointmentBtn);

        JButton viewReportsBtn = createStyledButton("View Reports", new Color(230, 126, 34));
        viewReportsBtn.addActionListener(e -> {
            JTabbedPane parent = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class, this);
            if (parent != null) {
                parent.setSelectedIndex(4);
            }
        });
        actionsPanel.add(viewReportsBtn);

        add(actionsPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }
}