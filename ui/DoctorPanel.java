import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DoctorPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField idField, nameField, specialtyField;
    
    public DoctorPanel() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        formPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        formPanel.add(idField);
        
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("Specialty:"));
        specialtyField = new JTextField();
        formPanel.add(specialtyField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        
        addBtn.addActionListener(e -> addDoctor());
        updateBtn.addActionListener(e -> updateDoctor());
        deleteBtn.addActionListener(e -> deleteDoctor());
        
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        formPanel.add(buttonPanel);
        
        add(formPanel, BorderLayout.NORTH);
        
        String[] columns = {"ID", "Name", "Specialty"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                idField.setText(tableModel.getValueAt(row, 0).toString());
                nameField.setText(tableModel.getValueAt(row, 1).toString());
                specialtyField.setText(tableModel.getValueAt(row, 2).toString());
            }
        });
        
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
    
    private void addDoctor() {
        tableModel.addRow(new Object[]{idField.getText(), nameField.getText(), specialtyField.getText()});
        clearFields();
    }
    
    private void updateDoctor() {
        int row = table.getSelectedRow();
        if (row != -1) {
            tableModel.setValueAt(idField.getText(), row, 0);
            tableModel.setValueAt(nameField.getText(), row, 1);
            tableModel.setValueAt(specialtyField.getText(), row, 2);
            clearFields();
        }
    }
    
    private void deleteDoctor() {
        int row = table.getSelectedRow();
        if (row != -1) {
            tableModel.removeRow(row);
            clearFields();
        }
    }
    
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        specialtyField.setText("");
    }
}
