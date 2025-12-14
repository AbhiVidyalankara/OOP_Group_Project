import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class NotificationPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> typeCombo;
    
    public NotificationPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        add(createControlPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        loadAllNotifications();
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(241, 196, 15), 2),
            "Notification Viewer",
            0, 0, new Font("Segoe UI", Font.BOLD, 16), new Color(241, 196, 15)
        ));
        
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        typeCombo = new JComboBox<>(new String[]{"Doctor", "Patient"});
        typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        typeCombo.setPreferredSize(new Dimension(120, 30));
        
        JButton allBtn = createStyledButton("Show All", new Color(52, 152, 219));
        JButton filterBtn = createStyledButton("Filter by Type", new Color(241, 196, 15));
        JButton selectAllBtn = createStyledButton("Select All", new Color(155, 89, 182));
        JButton deleteBtn = createStyledButton("Delete Selected", new Color(231, 76, 60));
        
        allBtn.addActionListener(e -> loadAllNotifications());
        filterBtn.addActionListener(e -> filterNotifications());
        selectAllBtn.addActionListener(e -> table.selectAll());
        deleteBtn.addActionListener(e -> deleteSelectedNotifications());
        
        panel.add(typeLabel);
        panel.add(typeCombo);
        panel.add(filterBtn);
        panel.add(allBtn);
        panel.add(selectAllBtn);
        panel.add(deleteBtn);
        
        return panel;
    }
    
    private JScrollPane createTablePanel() {
        String[] columns = {"Time", "Type", "Recipient ID", "Recipient Name", "Message"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setForeground(Color.BLACK);
        table.setBackground(Color.WHITE);
        table.setGridColor(new Color(200, 200, 200));
        table.setShowGrid(true);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(400);
        
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setForeground(Color.BLACK);
        cellRenderer.setBackground(Color.WHITE);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(241, 196, 15));
        table.getTableHeader().setForeground(Color.BLACK);
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        showNotificationDetails(row);
                    }
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("All Notifications"));
        
        return scrollPane;
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
    
    private void loadAllNotifications() {
        try {
            tableModel.setRowCount(0);
            
            for (Doctor d : Datastore.store.doctors) {
                List<Notification> notifications = NotificationManager.getDoctorNotifications(d.getId());
                for (Notification n : notifications) {
                    String timestamp = n.toString().substring(1, n.toString().indexOf("]"));
                    tableModel.addRow(new Object[]{
                        timestamp,
                        "Doctor",
                        d.getId(),
                        d.getName(),
                        n.getMessage()
                    });
                }
            }
            
            for (Patient p : Datastore.store.patients) {
                List<Notification> notifications = NotificationManager.getPatientNotifications(p.getId());
                for (Notification n : notifications) {
                    String timestamp = n.toString().substring(1, n.toString().indexOf("]"));
                    tableModel.addRow(new Object[]{
                        timestamp,
                        "Patient",
                        p.getId(),
                        p.getName(),
                        n.getMessage()
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void filterNotifications() {
        try {
            tableModel.setRowCount(0);
            String type = typeCombo.getSelectedItem().toString();
            
            if (type.equals("Doctor")) {
                for (Doctor d : Datastore.store.doctors) {
                    List<Notification> notifications = NotificationManager.getDoctorNotifications(d.getId());
                    for (Notification n : notifications) {
                        String timestamp = n.toString().substring(1, n.toString().indexOf("]"));
                        tableModel.addRow(new Object[]{
                            timestamp,
                            "Doctor",
                            d.getId(),
                            d.getName(),
                            n.getMessage()
                        });
                    }
                }
            } else {
                for (Patient p : Datastore.store.patients) {
                    List<Notification> notifications = NotificationManager.getPatientNotifications(p.getId());
                    for (Notification n : notifications) {
                        String timestamp = n.toString().substring(1, n.toString().indexOf("]"));
                        tableModel.addRow(new Object[]{
                            timestamp,
                            "Patient",
                            p.getId(),
                            p.getName(),
                            n.getMessage()
                        });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showNotificationDetails(int row) {
        String time = table.getValueAt(row, 0).toString();
        String type = table.getValueAt(row, 1).toString();
        String recipientId = table.getValueAt(row, 2).toString();
        String recipientName = table.getValueAt(row, 3).toString();
        String message = table.getValueAt(row, 4).toString();
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Notification Details", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Notification Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(241, 196, 15));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setMargin(new Insets(10, 10, 10, 10));
        
        StringBuilder details = new StringBuilder();
        details.append("Time: ").append(time).append("\n\n");
        details.append("Type: ").append(type).append("\n\n");
        details.append("Recipient ID: ").append(recipientId).append("\n\n");
        details.append("Recipient Name: ").append(recipientName).append("\n\n");
        details.append("Message:\n").append(message);
        
        detailsArea.setText(details.toString());
        
        JScrollPane scrollPane = new JScrollPane(detailsArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(new Color(52, 152, 219));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setOpaque(true);
        closeBtn.setPreferredSize(new Dimension(100, 35));
        closeBtn.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(closeBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private void deleteSelectedNotifications() {
        int[] selectedRows = table.getSelectedRows();
        
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select notifications to delete!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Delete " + selectedRows.length + " selected notification(s)?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                int row = selectedRows[i];
                String type = table.getValueAt(row, 1).toString();
                String recipientId = table.getValueAt(row, 2).toString();
                String message = table.getValueAt(row, 4).toString();
                
                if (type.equals("Doctor")) {
                    List<Notification> notifications = Datastore.store.doctorNotifications.get(recipientId);
                    if (notifications != null) {
                        notifications.removeIf(n -> n.getMessage().equals(message));
                    }
                } else {
                    List<Notification> notifications = Datastore.store.patientNotifications.get(recipientId);
                    if (notifications != null) {
                        notifications.removeIf(n -> n.getMessage().equals(message));
                    }
                }
            }
            
            Datastore.store.save();
            loadAllNotifications();
            JOptionPane.showMessageDialog(this, selectedRows.length + " notification(s) deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
