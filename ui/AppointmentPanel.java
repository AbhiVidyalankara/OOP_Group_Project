import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AppointmentPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField idField, doctorIdField, patientIdField, dateField, timeField;
    
    public AppointmentPanel() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        formPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        formPanel.add(idField);
        
        formPanel.add(new JLabel("Doctor ID:"));
        doctorIdField = new JTextField();
        formPanel.add(doctorIdField);
        
        formPanel.add(new JLabel("Patient ID:"));
        patientIdField = new JTextField();
        formPanel.add(patientIdField);
        
        formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField();
        formPanel.add(dateField);
        
        formPanel.add(new JLabel("Time (HH:MM):"));
        timeField = new JTextField();
        formPanel.add(timeField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton scheduleBtn = new JButton("Schedule");
        JButton deleteBtn = new JButton("Delete");
        
        scheduleBtn.addActionListener(e -> scheduleAppointment());
        deleteBtn.addActionListener(e -> deleteAppointment());
        
        buttonPanel.add(scheduleBtn);
        buttonPanel.add(deleteBtn);
        formPanel.add(buttonPanel);
        
        add(formPanel, BorderLayout.NORTH);
        
        String[] columns = {"ID", "Doctor ID", "Patient ID", "Date", "Time", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
    
    private void scheduleAppointment() {
        tableModel.addRow(new Object[]{
            idField.getText(), 
            doctorIdField.getText(), 
            patientIdField.getText(), 
            dateField.getText(), 
            timeField.getText(), 
            "Scheduled"
        });
        clearFields();
    }
    
    private void deleteAppointment() {
        int row = table.getSelectedRow();
        if (row != -1) {
            tableModel.removeRow(row);
        }
    }
    
    private void clearFields() {
        idField.setText("");
        doctorIdField.setText("");
        patientIdField.setText("");
        dateField.setText("");
        timeField.setText("");
    }
}
