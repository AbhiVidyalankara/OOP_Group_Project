import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;

public class ReportPanel extends JPanel {
    private JTextArea reportArea;
    private JTextField monthField;
    private JComboBox<String> reportTypeCombo, patientCombo;
    private JButton sendToPatientBtn;
    private String currentPatientId = "";
    private String currentReport = "";
    
    public ReportPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        add(createControlPanel(), BorderLayout.NORTH);
        add(createReportArea(), BorderLayout.CENTER);
    }
    
    private JPanel createControlPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(230, 126, 34), 2),
            "Report Generation",
            0, 0, new Font("Segoe UI", Font.BOLD, 16), new Color(230, 126, 34)
        ));
        
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(Color.WHITE);
        
        JLabel typeLabel = new JLabel("Report Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        reportTypeCombo = new JComboBox<>(new String[]{"Monthly Report", "Patient Report"});
        reportTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        reportTypeCombo.setPreferredSize(new Dimension(150, 30));
        
        JLabel monthLabel = new JLabel("Month (YYYY-MM):");
        monthLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        monthField = new JTextField(12);
        monthField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        monthField.setText(YearMonth.now().toString());
        
        JLabel patientLabel = new JLabel("Patient:");
        patientLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        patientCombo = new JComboBox<>();
        patientCombo.setEditable(true);
        patientCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        patientCombo.setPreferredSize(new Dimension(250, 30));
        patientCombo.setEnabled(false);
        updatePatientCombo();
        setupAutoComplete(patientCombo);
        
        sendToPatientBtn = createStyledButton("Send to Patient", new Color(46, 204, 113));
        sendToPatientBtn.setVisible(false);
        
        reportTypeCombo.addActionListener(e -> {
            boolean isPatientReport = reportTypeCombo.getSelectedIndex() == 1;
            patientCombo.setEnabled(isPatientReport);
            monthField.setEnabled(!isPatientReport);
            sendToPatientBtn.setVisible(false);
            currentPatientId = "";
            currentReport = "";
        });
        
        JButton generateBtn = createStyledButton("Generate", new Color(230, 126, 34));
        JButton currentBtn = createStyledButton("Current Month", new Color(52, 152, 219));
        
        generateBtn.addActionListener(e -> generateReport());
        currentBtn.addActionListener(e -> {
            monthField.setText(YearMonth.now().toString());
            reportTypeCombo.setSelectedIndex(0);
            generateReport();
        });
        sendToPatientBtn.addActionListener(e -> sendReportToPatient());
        
        panel.add(typeLabel);
        panel.add(reportTypeCombo);
        panel.add(monthLabel);
        panel.add(monthField);
        panel.add(patientLabel);
        panel.add(patientCombo);
        panel.add(generateBtn);
        panel.add(currentBtn);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(sendToPatientBtn);
        
        mainPanel.add(panel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private JScrollPane createReportArea() {
        reportArea = new JTextArea();
        reportArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        reportArea.setEditable(false);
        reportArea.setMargin(new Insets(15, 15, 15, 15));
        reportArea.setBackground(new Color(250, 250, 250));
        
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Report Details"));
        
        return scrollPane;
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(150, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    private void generateReport() {
        try {
            if (reportTypeCombo.getSelectedIndex() == 0) {
                YearMonth month = YearMonth.parse(monthField.getText().trim());
                String report = MonthlyReport.generateReport(month);
                reportArea.setText(report);
                sendToPatientBtn.setVisible(false);
                currentPatientId = "";
                currentReport = "";
            } else {
                String selected = (String) patientCombo.getSelectedItem();
                if (selected == null || selected.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please select a patient!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String patientId = extractId(selected);
                String report = generatePatientReport(patientId);
                reportArea.setText(report);
                currentPatientId = patientId;
                currentReport = report;
                sendToPatientBtn.setVisible(true);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input format!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String generatePatientReport(String patientId) {
        StringBuilder report = new StringBuilder();
        report.append("=== PATIENT REPORT ===\n\n");
        
        Patient patient = null;
        for (Patient p : Datastore.store.patients) {
            if (p.getId().equals(patientId)) {
                patient = p;
                break;
            }
        }
        
        if (patient == null) {
            return "Patient not found!";
        }
        
        report.append("Patient ID: ").append(patient.getId()).append("\n");
        report.append("Name: ").append(patient.getName()).append("\n");
        report.append("Phone: ").append(patient.getPhone()).append("\n");
        if (patient.getEmail() != null && !patient.getEmail().isEmpty()) {
            report.append("Email: ").append(patient.getEmail()).append("\n");
        }
        if (patient.getSituation() != null && !patient.getSituation().isEmpty()) {
            report.append("Situation: ").append(patient.getSituation()).append("\n");
        }
        report.append("\n");
        
        report.append("--- APPOINTMENT HISTORY ---\n");
        int totalAppointments = 0;
        int completed = 0;
        int scheduled = 0;
        int canceled = 0;
        
        for (Appointment apt : Datastore.store.appointments) {
            if (apt.patientId.equals(patientId)) {
                totalAppointments++;
                report.append("\n").append(apt.id).append(" - ");
                report.append("Doctor: ").append(apt.doctorId).append(", ");
                report.append("Date: ").append(apt.date).append(", ");
                report.append("Time: ").append(apt.time).append(", ");
                report.append("Status: ").append(apt.status).append("\n");
                
                if ("Completed".equalsIgnoreCase(apt.status)) completed++;
                else if ("Scheduled".equalsIgnoreCase(apt.status)) scheduled++;
                else if ("Canceled".equalsIgnoreCase(apt.status)) canceled++;
            }
        }
        
        report.append("\n--- SUMMARY ---\n");
        report.append("Total Appointments: ").append(totalAppointments).append("\n");
        report.append("Completed: ").append(completed).append("\n");
        report.append("Scheduled: ").append(scheduled).append("\n");
        report.append("Canceled: ").append(canceled).append("\n");
        
        return report.toString();
    }
    
    private void sendReportToPatient() {
        if (currentPatientId.isEmpty() || currentReport.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please generate a patient report first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Patient patient = null;
        for (Patient p : Datastore.store.patients) {
            if (p.getId().equals(currentPatientId)) {
                patient = p;
                break;
            }
        }
        
        if (patient == null) {
            JOptionPane.showMessageDialog(this, "Patient not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean sent = false;
        StringBuilder message = new StringBuilder("Report sent successfully to:\n");
        
        if (patient.getEmail() != null && !patient.getEmail().isEmpty()) {
            System.out.println("=== EMAIL SENT ===");
            System.out.println("To: " + patient.getEmail());
            System.out.println("Subject: Your Medical Report");
            System.out.println("Message:\n" + currentReport);
            System.out.println("==================\n");
            message.append("- Email: ").append(patient.getEmail()).append("\n");
            sent = true;
        }
        
        if (patient.getPhone() != null && !patient.getPhone().isEmpty()) {
            System.out.println("=== SMS SENT ===");
            System.out.println("To: " + patient.getPhone());
            System.out.println("Message: Your medical report has been generated. Please check your email or contact the clinic.");
            System.out.println("================\n");
            message.append("- SMS: ").append(patient.getPhone()).append("\n");
            sent = true;
        }
        
        if (sent) {
            NotificationManager.sendToPatient(currentPatientId, "Your medical report has been sent to your contact details.");
            Datastore.store.save();
            JOptionPane.showMessageDialog(this, message.toString(), "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Patient has no email or phone number!", "Error", JOptionPane.ERROR_MESSAGE);
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
    
    private void setupAutoComplete(JComboBox<String> combo) {
        final JTextField editor = (JTextField) combo.getEditor().getEditorComponent();
        final DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) combo.getModel();
        final java.util.List<String> allItems = new java.util.ArrayList<>();
        
        for (int i = 0; i < model.getSize(); i++) {
            allItems.add(model.getElementAt(i));
        }
        
        editor.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    return;
                }
                
                SwingUtilities.invokeLater(() -> {
                    String text = editor.getText().toLowerCase();
                    
                    if (text.isEmpty()) {
                        model.removeAllElements();
                        for (String item : allItems) {
                            model.addElement(item);
                        }
                        combo.hidePopup();
                        return;
                    }
                    
                    java.util.List<String> filtered = new java.util.ArrayList<>();
                    for (String item : allItems) {
                        if (item.toLowerCase().contains(text)) {
                            filtered.add(item);
                        }
                    }
                    
                    if (!filtered.isEmpty()) {
                        model.removeAllElements();
                        for (String item : filtered) {
                            model.addElement(item);
                        }
                        combo.setPopupVisible(true);
                    } else {
                        combo.hidePopup();
                    }
                });
            }
        });
        
        combo.addActionListener(e -> {
            if (combo.getSelectedItem() != null) {
                editor.setText(combo.getSelectedItem().toString());
            }
        });
    }
}
