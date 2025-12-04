package ui;

import model.Patient;
import service.PatientService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientPanel extends JPanel {
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, contactField, historyField;
    private int selectedPatientId = -1;

    public PatientPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(236, 240, 241));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                "Patient Details",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(52, 73, 94)));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(createLabel("Name:"));
        nameField = new JTextField();
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(nameField);

        formPanel.add(createLabel("Contact:"));
        contactField = new JTextField();
        contactField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(contactField);

        formPanel.add(createLabel("Medical History:"));
        historyField = new JTextField();
        historyField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(historyField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = createStyledButton("Add", new Color(46, 204, 113));
        JButton updateButton = createStyledButton("Update", new Color(52, 152, 219));
        JButton deleteButton = createStyledButton("Delete", new Color(231, 76, 60));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        formPanel.add(buttonPanel);

        String[] columns = {"ID", "Name", "Contact", "Medical History"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        patientTable = new JTable(tableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patientTable.setRowHeight(25);
        patientTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        patientTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        patientTable.getTableHeader().setBackground(new Color(52, 73, 94));
        patientTable.getTableHeader().setForeground(Color.WHITE);

        patientTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = patientTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedPatientId = (int) tableModel.getValueAt(selectedRow, 0);
                    nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
                    contactField.setText((String) tableModel.getValueAt(selectedRow, 2));
                    historyField.setText((String) tableModel.getValueAt(selectedRow, 3));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(patientTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                "Patient Records",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(52, 73, 94)));

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> addPatient());
        updateButton.addActionListener(e -> updatePatient());
        deleteButton.addActionListener(e -> deletePatient());

        loadPatientData();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(52, 73, 94));
        return label;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 35));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void addPatient() {
        String name = nameField.getText().trim();
        String contact = contactField.getText().trim();
        String history = historyField.getText().trim();

        if (name.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Contact are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PatientService.addPatient(new model.Patient(0, name, contact, history));
        clearForm();
        loadPatientData();
        JOptionPane.showMessageDialog(this, "Patient added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updatePatient() {
        if (selectedPatientId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to update!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = nameField.getText().trim();
        String contact = contactField.getText().trim();
        String history = historyField.getText().trim();

        if (name.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Contact are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Patient updatedPatient = new model.Patient(selectedPatientId, name, contact, history);
        PatientService.updatePatient(updatedPatient);
        clearForm();
        loadPatientData();
        JOptionPane.showMessageDialog(this, "Patient updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deletePatient() {
        if (selectedPatientId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this patient?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (PatientService.deletePatient(selectedPatientId)) {
                clearForm();
                loadPatientData();
                JOptionPane.showMessageDialog(this, "Patient deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Cannot delete patient with existing appointments!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadPatientData() {
        tableModel.setRowCount(0);
        List<Patient> patients = PatientService.getAllPatients();

        for (Patient p : patients) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getContact(),
                    p.getMedicalHistory()
            });
        }
    }

    private void clearForm() {
        nameField.setText("");
        contactField.setText("");
        historyField.setText("");
        selectedPatientId = -1;
        patientTable.clearSelection();
    }
}