package ui;

import model.Doctor;
import service.DoctorService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorPanel extends JPanel {
    private JTable doctorTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, specialtyField, availabilityField;
    private int selectedDoctorId = -1;

    public DoctorPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(236, 240, 241));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
                "Doctor Details",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(52, 73, 94)));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(createLabel("Name:"));
        nameField = new JTextField();
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(nameField);

        formPanel.add(createLabel("Specialty:"));
        specialtyField = new JTextField();
        specialtyField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(specialtyField);

        formPanel.add(createLabel("Availability:"));
        availabilityField = new JTextField();
        availabilityField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(availabilityField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = createStyledButton("Add", new Color(46, 204, 113));
        JButton updateButton = createStyledButton("Update", new Color(52, 152, 219));
        JButton deleteButton = createStyledButton("Delete", new Color(231, 76, 60));
        JButton assignButton = createStyledButton("Auto-Assign", new Color(155, 89, 182));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(assignButton);
        formPanel.add(buttonPanel);

        String[] columns = {"ID", "Name", "Specialty", "Availability"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        doctorTable = new JTable(tableModel);
        doctorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        doctorTable.setRowHeight(25);
        doctorTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        doctorTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        doctorTable.getTableHeader().setBackground(new Color(52, 73, 94));
        doctorTable.getTableHeader().setForeground(Color.WHITE);

        doctorTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = doctorTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedDoctorId = (int) tableModel.getValueAt(selectedRow, 0);
                    nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
                    specialtyField.setText((String) tableModel.getValueAt(selectedRow, 2));
                    availabilityField.setText((String) tableModel.getValueAt(selectedRow, 3));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(doctorTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
                "Doctor Records",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(52, 73, 94)));

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> addDoctor());
        updateButton.addActionListener(e -> updateDoctor());
        deleteButton.addActionListener(e -> deleteDoctor());
        assignButton.addActionListener(e -> autoAssignDoctor());

        loadDoctorData();
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

    private void addDoctor() {
        String name = nameField.getText().trim();
        String specialty = specialtyField.getText().trim();
        String availability = availabilityField.getText().trim();

        if (name.isEmpty() || specialty.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Specialty are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DoctorService.addDoctor(new model.Doctor(0, name, specialty, availability));
        clearForm();
        loadDoctorData();
        JOptionPane.showMessageDialog(this, "Doctor added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateDoctor() {
        if (selectedDoctorId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to update!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = nameField.getText().trim();
        String specialty = specialtyField.getText().trim();
        String availability = availabilityField.getText().trim();

        if (name.isEmpty() || specialty.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Specialty are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Doctor updatedDoctor = new model.Doctor(selectedDoctorId, name, specialty, availability);
        DoctorService.updateDoctor(updatedDoctor);
        clearForm();
        loadDoctorData();
        JOptionPane.showMessageDialog(this, "Doctor updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteDoctor() {
        if (selectedDoctorId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this doctor?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (DoctorService.deleteDoctor(selectedDoctorId)) {
                clearForm();
                loadDoctorData();
                JOptionPane.showMessageDialog(this, "Doctor deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Cannot delete doctor with existing appointments!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void autoAssignDoctor() {
        String specialty = JOptionPane.showInputDialog(this, "Enter specialty for auto-assignment:", "Cardiology");
        if (specialty != null && !specialty.trim().isEmpty()) {
            Doctor assignedDoctor = DoctorService.assignDoctor(specialty.trim());
            if (assignedDoctor != null) {
                JOptionPane.showMessageDialog(this,
                        "Assigned doctor: " + assignedDoctor.getName() + "\n" +
                                "Specialty: " + assignedDoctor.getSpecialty() + "\n" +
                                "Availability: " + assignedDoctor.getAvailability(),
                        "Auto-Assignment Successful", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No available doctors for specialty: " + specialty,
                        "Assignment Failed", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void loadDoctorData() {
        tableModel.setRowCount(0);
        List<Doctor> doctors = DoctorService.getAllDoctors();

        for (Doctor d : doctors) {
            tableModel.addRow(new Object[]{
                    d.getId(),
                    d.getName(),
                    d.getSpecialty(),
                    d.getAvailability()
            });
        }
    }

    private void clearForm() {
        nameField.setText("");
        specialtyField.setText("");
        availabilityField.setText("");
        selectedDoctorId = -1;
        doctorTable.clearSelection();
    }
}