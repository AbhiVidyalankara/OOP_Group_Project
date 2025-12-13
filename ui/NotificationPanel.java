import javax.swing.*;
import java.awt.*;

public class NotificationPanel extends JPanel {
    private JTextArea notificationArea;
    
    public NotificationPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel topPanel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Enter ID:");
        JTextField idField = new JTextField(15);
        JButton doctorBtn = new JButton("Doctor Notifications");
        JButton patientBtn = new JButton("Patient Notifications");
        
        doctorBtn.addActionListener(e -> showNotifications("Doctor", idField.getText()));
        patientBtn.addActionListener(e -> showNotifications("Patient", idField.getText()));
        
        topPanel.add(label);
        topPanel.add(idField);
        topPanel.add(doctorBtn);
        topPanel.add(patientBtn);
        
        add(topPanel, BorderLayout.NORTH);
        
        notificationArea = new JTextArea();
        notificationArea.setEditable(false);
        notificationArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(notificationArea), BorderLayout.CENTER);
    }
    
    private void showNotifications(String type, String id) {
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ID!");
            return;
        }
        
        StringBuilder notifications = new StringBuilder();
        notifications.append("=== ").append(type).append(" Notifications for ").append(id).append(" ===\n\n");
        notifications.append("• Appointment scheduled\n");
        notifications.append("• Status update received\n");
        notifications.append("• Reminder: Upcoming appointment\n");
        
        notificationArea.setText(notifications.toString());
    }
}
