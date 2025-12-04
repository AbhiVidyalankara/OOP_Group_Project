package notifications;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AddPatientPanel extends JPanel {

    private JTextField patientIdField;
    private JTextField nameField;
    private JTextField contactField;
    private JTextField situationField;
    private JComboBox<String> doctorDropdown;
    private JButton addButton;

    private PatientService patientService;

    public AddPatientPanel(List<String> doctorIds, PatientService patientService) {
        this.patientService = patientService;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Add Patient (Creates Doctor Notification)");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 10, 0);
        add(titleLabel, gbc);

        JLabel patientIdLabel = new JLabel("Patient ID:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(patientIdLabel, gbc);

        patientIdField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(patientIdField, gbc);

        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(nameLabel, gbc);

        nameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(nameField, gbc);

        JLabel contactLabel = new JLabel("Contact:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(contactLabel, gbc);

        contactField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(contactField, gbc);

        JLabel situationLabel = new JLabel("Situation:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(situationLabel, gbc);

        situationField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(situationField, gbc);

        JLabel doctorLabel = new JLabel("Doctor ID:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(doctorLabel, gbc);

        doctorDropdown = new JComboBox<>(doctorIds.toArray(new String[0]));
        gbc.gridx = 1;
        gbc.gridy = 5;
        add(doctorDropdown, gbc);

        addButton = new JButton("Add Patient");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 5, 5);
        add(addButton, gbc);

        addButton.addActionListener(e -> {
            String patientId = patientIdField.getText();
            String name = nameField.getText();
            String contact = contactField.getText();
            String situation = situationField.getText();
            String doctorId = (String) doctorDropdown.getSelectedItem();

            if (patientId == null || patientId.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Patient ID");
                return;
            }

            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Name");
                return;
            }

            if (doctorId == null || doctorId.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select Doctor ID");
                return;
            }

            patientService.addPatient(patientId, name, contact, doctorId, situation);

            JOptionPane.showMessageDialog(this, "Patient added and doctor notified.");
            patientIdField.setText("");
            nameField.setText("");
            contactField.setText("");
            situationField.setText("");
        });
    }
}
