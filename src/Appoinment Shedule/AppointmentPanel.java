import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public class AppointmentPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField, timeField;
    private JButton dateButton, timeButton;
    private LocalDate selectedDate;
    private LocalTime selectedTime;
    private JComboBox<String> doctorCombo, patientCombo, statusCombo;
    
    public AppointmentPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        add(createFormPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        
        refreshTable();
        updateNextId();
    }
    
    private void updateNextId() {
        if (idField.getText().isEmpty() || idField.getText().startsWith("A")) {
            idField.setText("A" + String.format("%03d", Datastore.store.appointments.size() + 1));
        }
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
            "Appointment Scheduling",
            0, 0, new Font("Segoe UI", Font.BOLD, 16), new Color(155, 89, 182)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        idField = new JTextField(12);
        
        dateButton = new JButton("Select Date");
        dateButton.setPreferredSize(new Dimension(150, 30));
        dateButton.setBackground(new Color(52, 152, 219));
        dateButton.setForeground(Color.WHITE);
        dateButton.setFocusPainted(false);
        dateButton.setBorderPainted(false);
        dateButton.setOpaque(true);
        dateButton.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        dateButton.addActionListener(e -> showDatePicker());
        
        timeField = new JTextField(10);
        timeField.setToolTipText("HH:MM format");
        
        timeButton = new JButton("Pick");
        timeButton.setPreferredSize(new Dimension(60, 30));
        timeButton.setBackground(new Color(52, 152, 219));
        timeButton.setForeground(Color.WHITE);
        timeButton.setFocusPainted(false);
        timeButton.setBorderPainted(false);
        timeButton.setOpaque(true);
        timeButton.setFont(new Font("Segoe UI", Font.BOLD, 10));
        timeButton.addActionListener(e -> showTimePicker());
        
        doctorCombo = new JComboBox<>();
        doctorCombo.setEditable(true);
        doctorCombo.setPreferredSize(new Dimension(200, 30));
        
        patientCombo = new JComboBox<>();
        patientCombo.setEditable(true);
        patientCombo.setPreferredSize(new Dimension(200, 30));
        
        statusCombo = new JComboBox<>(new String[]{"Scheduled", "Completed", "Canceled", "Delayed"});
        
        updateDoctorCombo();
        updatePatientCombo();
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Appointment ID:"), gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Doctor:"), gbc);
        gbc.gridx = 3;
        panel.add(doctorCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Patient:"), gbc);
        gbc.gridx = 1;
        panel.add(patientCombo, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 3;
        panel.add(dateButton, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Time (HH:MM):"), gbc);
        gbc.gridx = 1;
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        timePanel.setOpaque(false);
        timePanel.add(timeField);
        timePanel.add(timeButton);
        panel.add(timePanel, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 3;
        panel.add(statusCombo, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setOpaque(false);
        
        JButton scheduleBtn = createStyledButton("Schedule", new Color(46, 204, 113));
        JButton updateBtn = createStyledButton("Update Status", new Color(52, 152, 219));
        JButton selectAllBtn = createStyledButton("Select All", new Color(155, 89, 182));
        JButton deleteBtn = createStyledButton("Delete Selected", new Color(231, 76, 60));
        JButton clearBtn = createStyledButton("Clear", new Color(149, 165, 166));
        
        scheduleBtn.addActionListener(e -> scheduleAppointment());
        updateBtn.addActionListener(e -> updateStatus());
        selectAllBtn.addActionListener(e -> table.selectAll());
        deleteBtn.addActionListener(e -> deleteSelectedAppointments());
        clearBtn.addActionListener(e -> clearFields());
        
        buttonPanel.add(scheduleBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(selectAllBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private void updateDoctorCombo() {
        doctorCombo.removeAllItems();
        for (Doctor d : Datastore.store.doctors) {
            doctorCombo.addItem(d.getId() + " - " + d.getName() + " (" + d.getSpecialty() + ")");
        }
    }
    
    private void updatePatientCombo() {
        patientCombo.removeAllItems();
        for (Patient p : Datastore.store.patients) {
            patientCombo.addItem(p.getId() + " - " + p.getName());
        }
    }
    
    private String extractId(String comboText) {
        if (comboText == null || comboText.isEmpty()) return "";
        return comboText.split(" - ")[0].trim();
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columns = {"ID", "Doctor", "Patient", "Date", "Time", "Status"};
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
        headerRenderer.setBackground(new Color(155, 89, 182));
        
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(155, 89, 182));
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setSelectionBackground(new Color(184, 207, 229));
        table.setSelectionForeground(Color.BLACK);
        table.setDragEnabled(false);
        table.getTableHeader().setResizingAllowed(false);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                idField.setText(table.getValueAt(row, 0).toString());
                doctorCombo.setSelectedItem(table.getValueAt(row, 1).toString());
                patientCombo.setSelectedItem(table.getValueAt(row, 2).toString());
                selectedDate = LocalDate.parse(table.getValueAt(row, 3).toString());
                selectedTime = LocalTime.parse(table.getValueAt(row, 4).toString());
                updateDateButton();
                timeField.setText(selectedTime.toString());
                statusCombo.setSelectedItem(table.getValueAt(row, 5).toString());
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Scheduled Appointments"));
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
    
    private void scheduleAppointment() {
        String id = idField.getText().trim();
        String doctorText = (String) doctorCombo.getSelectedItem();
        String patientText = (String) patientCombo.getSelectedItem();
        
        if (id.isEmpty() || doctorText == null || patientText == null || doctorText.isEmpty() || patientText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a date!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String timeText = timeField.getText().trim();
        if (timeText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter or select a time!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        LocalTime time;
        try {
            time = LocalTime.parse(timeText);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid time! Use HH:MM (e.g., 14:30)", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String doctorId = extractId(doctorText);
        String patientId = extractId(patientText);
        LocalDate date = selectedDate;
        
        for (Appointment a : Datastore.store.appointments) {
            if (a.id.equals(id)) {
                JOptionPane.showMessageDialog(this, "Appointment ID " + id + " already exists! Please use a different ID.", "Duplicate ID", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        if (Scheduler.isAvailable(doctorId, date, time)) {
            Datastore.store.appointments.add(new Appointment(id, doctorId, patientId, date, time, "Scheduled"));
            NotificationService.sendAppointmentNotifications(doctorId, patientId, date.toString(), time.toString());
            JOptionPane.showMessageDialog(this, "Appointment scheduled successfully! ID: " + id + "\nNotifications sent via Email & SMS", "Success", JOptionPane.INFORMATION_MESSAGE);
            MedicareApp.refreshAllPanels();
            clearFields();
            updateNextId();
        } else {
            JOptionPane.showMessageDialog(this, "Time slot not available!\nReasons:\n- Weekend (Sat/Sun)\n- Doctor already booked\n- Outside hours (9 AM - 5 PM)", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateStatus() {
        String id = idField.getText().trim();
        String status = statusCombo.getSelectedItem().toString();
        
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an appointment from the table to update status!", "No Appointment Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        boolean found = false;
        String currentStatus = "";
        for (Appointment a : Datastore.store.appointments) {
            if (a.id.equals(id)) {
                currentStatus = a.status;
                found = true;
                break;
            }
        }
        
        if (!found) {
            JOptionPane.showMessageDialog(this, "Appointment not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (currentStatus.equals(status)) {
            JOptionPane.showMessageDialog(this, "Status is already '" + status + "'!\nPlease select a different status to update.", "No Change", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        for (Appointment a : Datastore.store.appointments) {
            if (a.id.equals(id)) {
                a.setStatus(status);
                
                String doctorName = "";
                String patientName = "";
                for (Doctor d : Datastore.store.doctors) {
                    if (d.getId().equals(a.doctorId)) {
                        doctorName = d.getName();
                        break;
                    }
                }
                for (Patient p : Datastore.store.patients) {
                    if (p.getId().equals(a.patientId)) {
                        patientName = p.getName();
                        break;
                    }
                }
                
                String doctorMsg = "Appointment Status Updated\n" +
                                  "Appointment ID: " + id + "\n" +
                                  "Patient: " + patientName + " (" + a.patientId + ")\n" +
                                  "Date: " + a.date + "\n" +
                                  "Time: " + a.time + "\n" +
                                  "Status: " + status;
                
                String patientMsg = "Appointment Status Updated\n" +
                                   "Appointment ID: " + id + "\n" +
                                   "Doctor: " + doctorName + " (" + a.doctorId + ")\n" +
                                   "Date: " + a.date + "\n" +
                                   "Time: " + a.time + "\n" +
                                   "Status: " + status;
                
                NotificationManager.sendToDoctor(a.doctorId, doctorMsg);
                NotificationManager.sendToPatient(a.patientId, patientMsg);
                Datastore.store.save();
                JOptionPane.showMessageDialog(this, "Status updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                MedicareApp.refreshAllPanels();
                clearFields();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Appointment not found!", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void deleteSelectedAppointments() {
        int[] selectedRows = table.getSelectedRows();
        
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select appointments to delete!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Delete " + selectedRows.length + " selected appointment(s)?\nThis action cannot be undone!", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            java.util.List<String> idsToDelete = new java.util.ArrayList<>();
            for (int row : selectedRows) {
                idsToDelete.add(table.getValueAt(row, 0).toString());
            }
            
            Datastore.store.appointments.removeIf(a -> idsToDelete.contains(a.id));
            
            Datastore.store.save();
            MedicareApp.refreshAllPanels();
            clearFields();
            JOptionPane.showMessageDialog(this, selectedRows.length + " appointment(s) deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void refreshTable() {
        tableModel.setRowCount(0);
        updateDoctorCombo();
        updatePatientCombo();
        
        for (Appointment a : Datastore.store.appointments) {
            String doctorInfo = a.doctorId;
            String patientInfo = a.patientId;
            
            for (Doctor d : Datastore.store.doctors) {
                if (d.getId().equals(a.doctorId)) {
                    doctorInfo = d.getId() + " - " + d.getName();
                    break;
                }
            }
            
            for (Patient p : Datastore.store.patients) {
                if (p.getId().equals(a.patientId)) {
                    patientInfo = p.getId() + " - " + p.getName();
                    break;
                }
            }
            
            tableModel.addRow(new Object[]{a.id, doctorInfo, patientInfo, a.date, a.time, a.status});
        }
    }
    
    private void clearFields() {
        doctorCombo.setSelectedIndex(-1);
        patientCombo.setSelectedIndex(-1);
        selectedDate = null;
        selectedTime = null;
        dateButton.setText("Select Date");
        dateButton.setBackground(new Color(52, 152, 219));
        timeField.setText("");
        statusCombo.setSelectedIndex(0);
        table.clearSelection();
        updateNextId();
    }
    
    private void showDatePicker() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Select Date", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        
        LocalDate now = LocalDate.now();
        final LocalDate[] tempDate = {selectedDate != null ? selectedDate : now};
        
        JPanel calendarPanel = new JPanel(new BorderLayout(5, 5));
        calendarPanel.setBackground(Color.WHITE);
        calendarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        JButton prevMonth = new JButton("◀");
        prevMonth.setBackground(new Color(52, 152, 219));
        prevMonth.setForeground(Color.WHITE);
        prevMonth.setFocusPainted(false);
        prevMonth.setBorderPainted(false);
        prevMonth.setOpaque(true);
        JButton nextMonth = new JButton("▶");
        nextMonth.setBackground(new Color(52, 152, 219));
        nextMonth.setForeground(Color.WHITE);
        nextMonth.setFocusPainted(false);
        nextMonth.setBorderPainted(false);
        nextMonth.setOpaque(true);
        JLabel monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        monthLabel.setForeground(Color.BLACK);
        
        headerPanel.add(prevMonth, BorderLayout.WEST);
        headerPanel.add(monthLabel, BorderLayout.CENTER);
        headerPanel.add(nextMonth, BorderLayout.EAST);
        
        JPanel daysPanel = new JPanel(new GridLayout(0, 7, 3, 3));
        daysPanel.setBackground(Color.WHITE);
        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : dayNames) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setForeground(Color.BLACK);
            daysPanel.add(label);
        }
        
        Runnable updateCalendar = new Runnable() {
            public void run() {
                monthLabel.setText(tempDate[0].format(DateTimeFormatter.ofPattern("MMMM yyyy")));
                
                for (int i = daysPanel.getComponentCount() - 1; i >= 7; i--) {
                    daysPanel.remove(i);
                }
                
                LocalDate firstDay = tempDate[0].withDayOfMonth(1);
                int startDay = firstDay.getDayOfWeek().getValue() % 7;
                int daysInMonth = tempDate[0].lengthOfMonth();
                
                for (int i = 0; i < startDay; i++) {
                    daysPanel.add(new JLabel(""));
                }
                
                for (int day = 1; day <= daysInMonth; day++) {
                    final LocalDate date = LocalDate.of(tempDate[0].getYear(), tempDate[0].getMonth(), day);
                    JButton dayBtn = new JButton(String.valueOf(day));
                    dayBtn.setFocusPainted(false);
                    dayBtn.setBorderPainted(false);
                    dayBtn.setOpaque(true);
                    dayBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    dayBtn.setPreferredSize(new Dimension(50, 35));
                    dayBtn.setMinimumSize(new Dimension(50, 35));
                    dayBtn.setHorizontalAlignment(SwingConstants.CENTER);
                    dayBtn.setMargin(new Insets(2, 2, 2, 2));
                    
                    if (date.equals(selectedDate)) {
                        dayBtn.setBackground(new Color(46, 204, 113));
                        dayBtn.setForeground(Color.WHITE);
                    } else if (date.equals(now)) {
                        dayBtn.setBackground(new Color(52, 152, 219));
                        dayBtn.setForeground(Color.WHITE);
                    } else if (date.isBefore(now)) {
                        dayBtn.setBackground(new Color(220, 220, 220));
                        dayBtn.setForeground(Color.GRAY);
                        dayBtn.setEnabled(false);
                    } else {
                        dayBtn.setBackground(Color.WHITE);
                        dayBtn.setForeground(Color.BLACK);
                        dayBtn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
                        dayBtn.setBorderPainted(true);
                    }
                    
                    dayBtn.addActionListener(e -> {
                        selectedDate = date;
                        updateDateButton();
                        dialog.dispose();
                    });
                    
                    daysPanel.add(dayBtn);
                }
                
                daysPanel.revalidate();
                daysPanel.repaint();
            }
        };
        
        prevMonth.addActionListener(e -> {
            tempDate[0] = tempDate[0].minusMonths(1);
            updateCalendar.run();
        });
        
        nextMonth.addActionListener(e -> {
            tempDate[0] = tempDate[0].plusMonths(1);
            updateCalendar.run();
        });
        
        updateCalendar.run();
        
        calendarPanel.add(headerPanel, BorderLayout.NORTH);
        calendarPanel.add(daysPanel, BorderLayout.CENTER);
        
        dialog.add(calendarPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    private void showTimePicker() {
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a date first!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String doctorText = (String) doctorCombo.getSelectedItem();
        if (doctorText == null || doctorText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a doctor first!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String doctorId = extractId(doctorText);
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Select Time", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(300, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel title = new JLabel("Available Time Slots (9 AM - 5 PM)", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(Color.BLACK);
        panel.add(title, BorderLayout.NORTH);
        
        JPanel slotsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        slotsPanel.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(slotsPanel);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        for (LocalTime slot : Scheduler.generateSlots()) {
            boolean isBooked = !Scheduler.isAvailable(doctorId, selectedDate, slot);
            
            JButton slotBtn = new JButton(slot.format(DateTimeFormatter.ofPattern("hh:mm a")));
            slotBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            slotBtn.setFocusPainted(false);
            slotBtn.setBorderPainted(false);
            slotBtn.setOpaque(true);
            
            if (isBooked) {
                slotBtn.setBackground(new Color(231, 76, 60));
                slotBtn.setForeground(Color.WHITE);
                slotBtn.setText(slot.format(DateTimeFormatter.ofPattern("hh:mm a")) + " ✗");
                slotBtn.setEnabled(false);
            } else if (slot.equals(selectedTime)) {
                slotBtn.setBackground(new Color(46, 204, 113));
                slotBtn.setForeground(Color.WHITE);
            } else {
                slotBtn.setBackground(new Color(52, 152, 219));
                slotBtn.setForeground(Color.WHITE);
            }
            
            if (!isBooked) {
                slotBtn.addActionListener(e -> {
                    selectedTime = slot;
                    timeField.setText(slot.toString());
                    dialog.dispose();
                });
            }
            
            slotsPanel.add(slotBtn);
        }
        
        panel.add(scrollPane, BorderLayout.CENTER);
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void updateDateButton() {
        if (selectedDate != null) {
            String shortDate = selectedDate.format(DateTimeFormatter.ofPattern("dd-MMM-yy"));
            dateButton.setText(shortDate);
            dateButton.setBackground(new Color(46, 204, 113));
            dateButton.setToolTipText(selectedDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
        }
    }
}
