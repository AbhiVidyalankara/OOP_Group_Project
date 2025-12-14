import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class DoctorPanel extends JPanel {
    private DoctorService doctorService;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, specialtyField, emailField, phoneField;
    private JComboBox<String> statusCombo;
    private JButton scheduleBtn;
    
    public DoctorPanel(DoctorService service) {
        this.doctorService = service;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        add(createFormPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        
        refreshTable();
        updateNextId();
    }
    
    private void updateNextId() {
        if (idField.getText().isEmpty() || idField.getText().startsWith("D")) {
            idField.setText("D" + String.format("%03d", Datastore.store.doctors.size() + 1));
        }
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "Doctor Management",
            0, 0, new Font("Segoe UI", Font.BOLD, 16), new Color(52, 152, 219)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        idField = new JTextField(12);
        nameField = new JTextField(12);
        specialtyField = new JTextField(12);
        emailField = new JTextField(12);
        phoneField = new JTextField(12);
        statusCombo = new JComboBox<>(new String[]{"Available", "Unavailable", "On Leave"});
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Doctor ID:"), gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 3;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Specialty:"), gbc);
        gbc.gridx = 1;
        panel.add(specialtyField, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3;
        panel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 3;
        panel.add(statusCombo, gbc);
        
        scheduleBtn = createStyledButton("Manage Schedule", new Color(142, 68, 173));
        scheduleBtn.setPreferredSize(new Dimension(140, 35));
        gbc.gridx = 4;
        panel.add(scheduleBtn, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setOpaque(false);
        
        JButton addBtn = createStyledButton("Add", new Color(46, 204, 113));
        JButton updateBtn = createStyledButton("Update", new Color(52, 152, 219));
        JButton deleteBtn = createStyledButton("Delete", new Color(231, 76, 60));
        JButton updateStatusBtn = createStyledButton("Update Status", new Color(243, 156, 18));
        JButton clearBtn = createStyledButton("Clear", new Color(149, 165, 166));
        
        addBtn.addActionListener(e -> addDoctor());
        updateBtn.addActionListener(e -> updateDoctor());
        deleteBtn.addActionListener(e -> deleteDoctor());
        updateStatusBtn.addActionListener(e -> updateDoctorStatus());
        scheduleBtn.addActionListener(e -> manageSchedule());
        clearBtn.addActionListener(e -> clearFields());
        
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(updateStatusBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columns = {"ID", "Name", "Specialty", "Email", "Phone", "Status", "Schedule"};
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
        headerRenderer.setBackground(new Color(52, 152, 219));
        
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
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
                String id = table.getValueAt(row, 0).toString();
                idField.setText(id);
                nameField.setText(table.getValueAt(row, 1).toString());
                specialtyField.setText(table.getValueAt(row, 2).toString());
                emailField.setText(table.getValueAt(row, 3) != null ? table.getValueAt(row, 3).toString() : "");
                phoneField.setText(table.getValueAt(row, 4) != null ? table.getValueAt(row, 4).toString() : "");
                statusCombo.setSelectedItem(table.getValueAt(row, 5) != null ? table.getValueAt(row, 5).toString() : "Available");
                updateScheduleButtonState(id);
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Registered Doctors"));
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
        btn.setPreferredSize(new Dimension(120, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    private void addDoctor() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String specialty = specialtyField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        
        if (id.isEmpty() || name.isEmpty() || specialty.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!phone.matches("[0-9\\-\\+\\(\\)\\s]+")) {
            JOptionPane.showMessageDialog(this, "Phone number can only contain digits, spaces, and symbols (+, -, (, ))!\nInvalid phone: " + phone, "Invalid Phone", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Email must contain '@' symbol!\nInvalid email: " + email, "Invalid Email", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        for (Doctor d : Datastore.store.doctors) {
            if (d.getId().equals(id)) {
                JOptionPane.showMessageDialog(this, "Doctor ID " + id + " already exists! Please use a different ID.", "Duplicate ID", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (d.getName().equalsIgnoreCase(name) && d.getPhone().equals(phone)) {
                JOptionPane.showMessageDialog(this, "A doctor with the same name and phone number already exists!\nName: " + name + "\nPhone: " + phone, "Duplicate Doctor", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (d.getPhone().equals(phone)) {
                JOptionPane.showMessageDialog(this, "Phone number " + phone + " is already registered to Dr. " + d.getName() + "!", "Duplicate Phone", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (d.getEmail().equalsIgnoreCase(email)) {
                JOptionPane.showMessageDialog(this, "Email " + email + " is already registered to Dr. " + d.getName() + "!", "Duplicate Email", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        for (Patient p : Datastore.store.patients) {
            if (p.getPhone().equals(phone)) {
                JOptionPane.showMessageDialog(this, "Phone number " + phone + " is already registered to patient " + p.getName() + "!", "Duplicate Phone", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (p.getEmail() != null && p.getEmail().equalsIgnoreCase(email)) {
                JOptionPane.showMessageDialog(this, "Email " + email + " is already registered to patient " + p.getName() + "!", "Duplicate Email", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        Doctor doctor = new Doctor(id, name, specialty, email, phone);
        doctor.setStatus(statusCombo.getSelectedItem().toString());
        doctorService.addDoctor(doctor);
        JOptionPane.showMessageDialog(this, "Doctor added successfully! ID: " + id, "Success", JOptionPane.INFORMATION_MESSAGE);
        MedicareApp.refreshAllPanels();
        clearFields();
        updateNextId();
    }
    
    private void updateDoctor() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String specialty = specialtyField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String status = statusCombo.getSelectedItem().toString();
        
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a doctor to update!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        for (Doctor d : Datastore.store.doctors) {
            if (d.getId().equals(id)) {
                for (Doctor other : Datastore.store.doctors) {
                    if (!other.getId().equals(id)) {
                        if (other.getName().equalsIgnoreCase(name) && other.getPhone().equals(phone)) {
                            JOptionPane.showMessageDialog(this, "A doctor with the same name and phone number already exists!\nName: " + name + "\nPhone: " + phone, "Duplicate Doctor", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        if (other.getPhone().equals(phone)) {
                            JOptionPane.showMessageDialog(this, "Phone number " + phone + " is already registered to Dr. " + other.getName() + "!", "Duplicate Phone", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }
                }
                d.setName(name);
                d.setSpecialty(specialty);
                d.setEmail(email);
                d.setPhone(phone);
                d.setStatus(status);
                JOptionPane.showMessageDialog(this, "Doctor updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                MedicareApp.refreshAllPanels();
                clearFields();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Doctor not found!", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void updateDoctorStatus() {
        String id = idField.getText().trim();
        String status = statusCombo.getSelectedItem().toString();
        
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a doctor to update status!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Doctor doctor = null;
        for (Doctor d : Datastore.store.doctors) {
            if (d.getId().equals(id)) {
                doctor = d;
                d.setStatus(status);
                break;
            }
        }
        
        if (doctor != null) {
            for (Appointment apt : Datastore.store.appointments) {
                if (apt.doctorId.equals(id) && "Scheduled".equals(apt.status)) {
                    String message = "Doctor " + doctor.getName() + " status updated to: " + status + 
                                   "\nAppointment ID: " + apt.id + " on " + apt.date;
                    NotificationManager.sendToPatient(apt.patientId, message);
                }
            }
            JOptionPane.showMessageDialog(this, "Doctor status updated! Patients notified.", "Success", JOptionPane.INFORMATION_MESSAGE);
            MedicareApp.refreshAllPanels();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Doctor not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteDoctor() {
        String id = idField.getText().trim();
        
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a doctor to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Delete doctor " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (doctorService.deleteDoctor(id)) {
                JOptionPane.showMessageDialog(this, "Doctor deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                MedicareApp.refreshAllPanels();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Doctor not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Doctor d : doctorService.getDoctors()) {
            String scheduleInfo = d.getSchedule() != null ? d.getSchedule().getScheduleSummary() : "No schedule set";
            tableModel.addRow(new Object[]{d.getId(), d.getName(), d.getSpecialty(), 
                d.getEmail() != null ? d.getEmail() : "", 
                d.getPhone() != null ? d.getPhone() : "",
                d.getStatus() != null ? d.getStatus() : "Available",
                scheduleInfo});
        }
    }
    
    private void manageSchedule() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor from the table to manage schedule!", "No Doctor Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a doctor to manage schedule!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Doctor doctor = null;
        for (Doctor d : Datastore.store.doctors) {
            if (d.getId().equals(id)) {
                doctor = d;
                break;
            }
        }
        
        if (doctor != null) {
            ScheduleDialog dialog = new ScheduleDialog((JFrame) SwingUtilities.getWindowAncestor(this), doctor);
            dialog.setVisible(true);
            
            if (dialog.isSaved()) {
                JOptionPane.showMessageDialog(this, "Schedule updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                MedicareApp.refreshAllPanels();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Doctor not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateScheduleButtonState(String doctorId) {
        boolean hasAppointments = false;
        for (Appointment apt : Datastore.store.appointments) {
            if (apt.doctorId.equals(doctorId)) {
                hasAppointments = true;
                break;
            }
        }
        
        if (hasAppointments) {
            scheduleBtn.setEnabled(false);
            scheduleBtn.setBackground(Color.GRAY);
        } else {
            scheduleBtn.setEnabled(true);
            scheduleBtn.setBackground(new Color(142, 68, 173));
        }
    }
    
    private void clearFields() {
        nameField.setText("");
        specialtyField.setText("");
        emailField.setText("");
        phoneField.setText("");
        statusCombo.setSelectedIndex(0);
        table.clearSelection();
        scheduleBtn.setEnabled(true);
        scheduleBtn.setBackground(new Color(142, 68, 173));
        updateNextId();
    }
}
