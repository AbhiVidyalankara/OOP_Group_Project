import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PatientPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField idField, nameField, phoneField;
    
    public PatientPanel() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        formPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        formPanel.add(idField);
        
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        
        addBtn.addActionListener(e -> addPatient());
        updateBtn.addActionListener(e -> updatePatient());
        deleteBtn.addActionListener(e -> deletePatient());
        
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        formPanel.add(buttonPanel);
        
        add(formPanel, BorderLayout.NORTH);
        
        String[] columns = {"ID", "Name", "Phone"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                idField.setText(tableModel.getValueAt(row, 0).toString());
                nameField.setText(tableModel.getValueAt(row, 1).toString());
                phoneField.setText(tableModel.getValueAt(row, 2).toString());
            }
        });
        
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
    
    private void addPatient() {
        tableModel.addRow(new Object[]{idField.getText(), nameField.getText(), phoneField.getText()});
        clearFields();
    }
    
    private void updatePatient() {
        int row = table.getSelectedRow();
        if (row != -1) {
            tableModel.setValueAt(idField.getText(), row, 0);
            tableModel.setValueAt(nameField.getText(), row, 1);
            tableModel.setValueAt(phoneField.getText(), row, 2);
            clearFields();
        }
    }
    
    private void deletePatient() {
        int row = table.getSelectedRow();
        if (row != -1) {
            tableModel.removeRow(row);
            clearFields();
        }
    }
    
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        phoneField.setText("");
    }
}
