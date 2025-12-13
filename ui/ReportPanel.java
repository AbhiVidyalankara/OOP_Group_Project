import javax.swing.*;
import java.awt.*;

public class ReportPanel extends JPanel {
    private JTextField monthField;
    private JTextArea reportArea;
    
    public ReportPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Year-Month (YYYY-MM):"));
        monthField = new JTextField(10);
        topPanel.add(monthField);
        
        JButton generateBtn = new JButton("Generate Report");
        generateBtn.addActionListener(e -> generateReport());
        topPanel.add(generateBtn);
        
        add(topPanel, BorderLayout.NORTH);
        
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(reportArea), BorderLayout.CENTER);
    }
    
    private void generateReport() {
        String month = monthField.getText();
        StringBuilder report = new StringBuilder();
        report.append("=== MONTHLY REPORT FOR ").append(month).append(" ===\n\n");
        report.append("Total Appointments: 0\n");
        report.append("Completed: 0\n");
        report.append("Cancelled: 0\n");
        report.append("Pending: 0\n\n");
        report.append("Total Doctors: 0\n");
        report.append("Total Patients: 0\n");
        reportArea.setText(report.toString());
    }
}
