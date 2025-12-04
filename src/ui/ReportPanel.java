package ui;

import service.ReportService;

import javax.swing.*;
import java.awt.*;

public class ReportPanel extends JPanel {
    private JTextArea reportArea;

    public ReportPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(236, 240, 241));

        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Courier New", Font.PLAIN, 13));
        reportArea.setBackground(Color.WHITE);
        reportArea.setForeground(new Color(52, 73, 94));
        reportArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                "Monthly Report",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(52, 73, 94)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(new Color(236, 240, 241));

        JButton generateButton = createStyledButton("Generate Report", new Color(52, 152, 219));
        generateButton.addActionListener(e -> generateReport());
        buttonPanel.add(generateButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 40));
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void generateReport() {
        String report = ReportService.generateMonthlyReport();
        reportArea.setText(report);

        JOptionPane.showMessageDialog(this,
                "Monthly report generated successfully!",
                "Report Generated",
                JOptionPane.INFORMATION_MESSAGE);
    }
}