import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class PatientPanel extends JPanel {
    private PatientService patientService;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, phoneField, emailField;
    private JTextArea situationArea;
    
    public PatientPanel(PatientService service) {
        this.patientService = service;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        add(createFormPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        
        refreshTable();
        updateNextId();
    }
    
    private void updateNextId() {
        if (idField.getText().isEmpty() || idField.getText().startsWith("P")) {
            idField.setText("P" + String.format("%03d", Datastore.store.patients.size() + 1));
        }
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
            "Patient Management",
            0, 0, new Font("Segoe UI", Font.BOLD, 16), new Color(46, 204, 113)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        idField = new JTextField(12);
        nameField = new JTextField(12);
        phoneField = new JTextField(12);
        emailField = new JTextField(12);
        situationArea = new JTextArea(3, 12);
        situationArea.setLineWrap(true);
        situationArea.setWrapStyleWord(true);
        JScrollPane situationScroll = new JScrollPane(situationArea);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Patient ID:"), gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 3;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Email (Optional):"), gbc);
        gbc.gridx = 3;
        panel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Situation:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(situationScroll, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setOpaque(false);
        
        JButton addBtn = createStyledButton("Add", new Color(46, 204, 113));
        JButton updateBtn = createStyledButton("Update", new Color(52, 152, 219));
        JButton updateSituationBtn = createStyledButton("Update Situation", new Color(243, 156, 18));
        JButton deleteBtn = createStyledButton("Delete", new Color(231, 76, 60));
        JButton clearBtn = createStyledButton("Clear", new Color(149, 165, 166));
        
        addBtn.addActionListener(e -> addPatient());
        updateBtn.addActionListener(e -> updatePatient());
        updateSituationBtn.addActionListener(e -> updatePatientSituation());
        deleteBtn.addActionListener(e -> deletePatient());
        clearBtn.addActionListener(e -> clearFields());
        
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(updateSituationBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columns = {"ID", "Name", "Phone", "Email", "Situation"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setForeground(Color.BLACK);
        table.setBackground(Color.WHITE);
        table.setGridColor(new Color(200, 200, 200));
        table.setShowGrid(true);
        
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setForeground(Color.BLACK);
        cellRenderer.setBackground(Color.WHITE);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        headerRenderer.setForeground(Color.BLACK);
        headerRenderer.setBackground(new Color(46, 204, 113));
        
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(46, 204, 113));
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(184, 207, 229));
        table.setSelectionForeground(Color.BLACK);
        table.setDragEnabled(false);
        table.getTableHeader().setResizingAllowed(false);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                idField.setText(table.getValueAt(row, 0).toString());
                nameField.setText(table.getValueAt(row, 1).toString());
                phoneField.setText(table.getValueAt(row, 2).toString());
                emailField.setText(table.getValueAt(row, 3) != null ? table.getValueAt(row, 3).toString() : "");
                situationArea.setText(table.getValueAt(row, 4) != null ? table.getValueAt(row, 4).toString() : "");
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Registered Patients"));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(140, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    private void addPatient() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String situation = situationArea.getText().trim();
        
        if (id.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID, Name and Phone are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!phone.matches("[0-9\\-\\+\\(\\)\\s]+")) {
            JOptionPane.showMessageDialog(this, "Phone number can only contain digits, spaces, and symbols (+, -, (, ))!\nInvalid phone: " + phone, "Invalid Phone", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!email.isEmpty() && !email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Email must contain '@' symbol!\nInvalid email: " + email, "Invalid Email", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        for (Patient p : Datastore.store.patients) {
            if (p.getId().equals(id)) {
                JOptionPane.showMessageDialog(this, "Patient ID " + id + " already exists! Please use a different ID.", "Duplicate ID", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (p.getName().equalsIgnoreCase(name) && p.getPhone().equals(phone)) {
                JOptionPane.showMessageDialog(this, "A patient with the same name and phone number already exists!\nName: " + name + "\nPhone: " + phone, "Duplicate Patient", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (p.getPhone().equals(phone)) {
                JOptionPane.showMessageDialog(this, "Phone number " + phone + " is already registered to patient " + p.getName() + "!", "Duplicate Phone", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!email.isEmpty() && p.getEmail() != null && p.getEmail().equalsIgnoreCase(email)) {
                JOptionPane.showMessageDialog(this, "Email " + email + " is already registered to patient " + p.getName() + "!", "Duplicate Email", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        for (Doctor d : Datastore.store.doctors) {
            if (d.getPhone().equals(phone)) {
                JOptionPane.showMessageDialog(this, "Phone number " + phone + " is already registered to Dr. " + d.getName() + "!", "Duplicate Phone", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!email.isEmpty() && d.getEmail().equalsIgnoreCase(email)) {
                JOptionPane.showMessageDialog(this, "Email " + email + " is already registered to Dr. " + d.getName() + "!", "Duplicate Email", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        patientService.addPatient(new Patient(id, name, phone, email, situation));
        JOptionPane.showMessageDialog(this, "Patient added successfully! ID: " + id, "Success", JOptionPane.INFORMATION_MESSAGE);
        MedicareApp.refreshAllPanels();
        clearFields();
        updateNextId();
    }
    
    private void updatePatient() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String situation = situationArea.getText().trim();
        
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a patient to update!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        for (Patient p : Datastore.store.patients) {
            if (p.getId().equals(id)) {
                for (Patient other : Datastore.store.patients) {
                    if (!other.getId().equals(id)) {
                        if (other.getName().equalsIgnoreCase(name) && other.getPhone().equals(phone)) {
                            JOptionPane.showMessageDialog(this, "A patient with the same name and phone number already exists!\nName: " + name + "\nPhone: " + phone, "Duplicate Patient", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }
                }
                p.setName(name);
                p.setPhone(phone);
                p.setEmail(email);
                p.setSituation(situation);
                JOptionPane.showMessageDialog(this, "Patient updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                MedicareApp.refreshAllPanels();
                clearFields();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Patient not found!", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void updatePatientSituation() {
        String id = idField.getText().trim();
        String situation = situationArea.getText().trim();
        
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a patient to update situation!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Patient patient = null;
        for (Patient p : Datastore.store.patients) {
            if (p.getId().equals(id)) {
                patient = p;
                p.setSituation(situation);
                break;
            }
        }
        
        if (patient != null) {
            for (Appointment apt : Datastore.store.appointments) {
                if (apt.patientId.equals(id) && "Scheduled".equals(apt.status)) {
                    String message = "Patient " + patient.getName() + " (ID: " + id + ") situation updated:\n" + situation +
                                   "\nAppointment: " + apt.id + " on " + apt.date;
                    NotificationManager.sendToDoctor(apt.doctorId, message);
                }
            }
            JOptionPane.showMessageDialog(this, "Patient situation updated! Doctors notified.", "Success", JOptionPane.INFORMATION_MESSAGE);
            MedicareApp.refreshAllPanels();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Patient not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deletePatient() {
        String id = idField.getText().trim();
        
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a patient to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Delete patient " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (patientService.deletePatient(id)) {
                JOptionPane.showMessageDialog(this, "Patient deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                MedicareApp.refreshAllPanels();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Patient not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Patient p : patientService.getPatients()) {
            tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getPhone(), 
                p.getEmail() != null ? p.getEmail() : "",
                p.getSituation() != null ? p.getSituation() : ""});
        }
    }
    
    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        situationArea.setText("");
        table.clearSelection();
        updateNextId();
    }
}
