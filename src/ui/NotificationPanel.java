package ui;

import model.Notification;
import service.NotificationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NotificationPanel extends JPanel {
    private JTable notificationTable;
    private DefaultTableModel tableModel;

    public NotificationPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(236, 240, 241));

        String[] columns = {"ID", "Type", "Recipient", "Message", "Time", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        notificationTable = new JTable(tableModel);
        notificationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notificationTable.setRowHeight(30);
        notificationTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        notificationTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        notificationTable.getTableHeader().setBackground(new Color(52, 73, 94));
        notificationTable.getTableHeader().setForeground(Color.WHITE);

        notificationTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = notificationTable.getSelectedRow();
                    if (selectedRow != -1) {
                        showNotificationDetails(selectedRow);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(notificationTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
                "System Notifications - Double click to view details",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(52, 73, 94)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(new Color(236, 240, 241));

        JButton refreshButton = createStyledButton("Refresh", new Color(52, 152, 219));
        JButton markReadButton = createStyledButton("Mark as Read", new Color(46, 204, 113));
        JButton markAllReadButton = createStyledButton("Mark All Read", new Color(230, 126, 34));

        refreshButton.addActionListener(e -> loadNotifications());
        markReadButton.addActionListener(e -> markSelectedAsRead());
        markAllReadButton.addActionListener(e -> markAllAsRead());

        buttonPanel.add(refreshButton);
        buttonPanel.add(markReadButton);
        buttonPanel.add(markAllReadButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadNotifications();
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(130, 35));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void showNotificationDetails(int selectedRow) {
        int notificationId = (int) tableModel.getValueAt(selectedRow, 0);
        String type = (String) tableModel.getValueAt(selectedRow, 1);
        String recipient = (String) tableModel.getValueAt(selectedRow, 2);
        String message = (String) tableModel.getValueAt(selectedRow, 3);
        String time = (String) tableModel.getValueAt(selectedRow, 4);
        String status = (String) tableModel.getValueAt(selectedRow, 5);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Notification Details", true);
        dialog.setLayout(new BorderLayout(15, 15));
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new GridLayout(6, 1, 5, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        contentPanel.add(createInfoLabel("Type: " + type));
        contentPanel.add(createInfoLabel("Recipient: " + recipient));
        contentPanel.add(createInfoLabel("Time: " + time));
        contentPanel.add(createInfoLabel("Status: " + status));

        JTextArea messageArea = new JTextArea(message);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        messageArea.setEditable(false);
        messageArea.setBackground(new Color(236, 240, 241));
        messageArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane messageScroll = new JScrollPane(messageArea);
        messageScroll.setBorder(BorderFactory.createTitledBorder("Message"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton markReadBtn = createStyledButton("Mark as Read", new Color(46, 204, 113));
        JButton closeBtn = createStyledButton("Close", new Color(149, 165, 166));

        markReadBtn.addActionListener(e -> {
            NotificationService.markAsRead(notificationId);
            loadNotifications();
            JOptionPane.showMessageDialog(dialog, "Notification marked as read!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        closeBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(markReadBtn);
        buttonPanel.add(closeBtn);

        dialog.add(contentPanel, BorderLayout.NORTH);
        dialog.add(messageScroll, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(52, 73, 94));
        return label;
    }

    public void loadNotifications() {
        tableModel.setRowCount(0);
        List<Notification> notifications = NotificationService.getNotifications();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Notification n : notifications) {
            tableModel.addRow(new Object[]{
                    n.getId(),
                    n.getType(),
                    n.getRecipient(),
                    n.getMessage(),
                    n.getTimestamp().format(formatter),
                    n.isRead() ? "Read" : "Unread"
            });
        }
    }

    private void markSelectedAsRead() {
        int selectedRow = notificationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a notification to mark as read!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int notificationId = (int) tableModel.getValueAt(selectedRow, 0);
        NotificationService.markAsRead(notificationId);
        loadNotifications();
        JOptionPane.showMessageDialog(this, "Notification marked as read!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void markAllAsRead() {
        NotificationService.markAllAsRead();
        loadNotifications();
        JOptionPane.showMessageDialog(this, "All notifications marked as read!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}