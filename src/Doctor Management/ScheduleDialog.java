import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ScheduleDialog extends JDialog {
    private Doctor doctor;
    private DoctorSchedule schedule;
    private JCheckBox[] dayCheckBoxes;
    private JTextField[] startTimeFields;
    private JTextField[] endTimeFields;
    private boolean saved = false;
    
    public ScheduleDialog(JFrame parent, Doctor doctor) {
        super(parent, "Manage Schedule - Dr. " + doctor.getName(), true);
        this.doctor = doctor;
        this.schedule = new DoctorSchedule();
        
        // Copy existing schedule
        if (doctor.getSchedule() != null) {
            for (DayOfWeek day : DayOfWeek.values()) {
                if (doctor.getSchedule().isAvailableOn(day)) {
                    DoctorSchedule.TimeSlot slot = doctor.getSchedule().getTimeSlot(day);
                    this.schedule.setDaySchedule(day, slot.getStartTime(), slot.getEndTime());
                }
            }
        }
        
        initComponents();
        setSize(500, 400);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Header
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        JLabel headerLabel = new JLabel("Set Weekly Schedule", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        mainPanel.add(headerLabel, gbc);
        
        // Column headers
        gbc.gridwidth = 1; gbc.gridy = 1;
        gbc.gridx = 0; mainPanel.add(new JLabel("Day"), gbc);
        gbc.gridx = 1; mainPanel.add(new JLabel("Available"), gbc);
        gbc.gridx = 2; mainPanel.add(new JLabel("Start Time (HH:MM)"), gbc);
        gbc.gridx = 3; mainPanel.add(new JLabel("End Time (HH:MM)"), gbc);
        
        // Initialize arrays
        DayOfWeek[] days = DayOfWeek.values();
        dayCheckBoxes = new JCheckBox[7];
        startTimeFields = new JTextField[7];
        endTimeFields = new JTextField[7];
        
        // Create rows for each day
        for (int i = 0; i < days.length; i++) {
            DayOfWeek day = days[i];
            gbc.gridy = i + 2;
            
            // Day name
            gbc.gridx = 0;
            mainPanel.add(new JLabel(day.toString()), gbc);
            
            // Checkbox
            gbc.gridx = 1;
            dayCheckBoxes[i] = new JCheckBox();
            dayCheckBoxes[i].setSelected(schedule.isAvailableOn(day));
            mainPanel.add(dayCheckBoxes[i], gbc);
            
            // Start time
            gbc.gridx = 2;
            startTimeFields[i] = new JTextField(8);
            if (schedule.isAvailableOn(day)) {
                startTimeFields[i].setText(schedule.getTimeSlot(day).getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            } else {
                startTimeFields[i].setText("09:00");
            }
            mainPanel.add(startTimeFields[i], gbc);
            
            // End time
            gbc.gridx = 3;
            endTimeFields[i] = new JTextField(8);
            if (schedule.isAvailableOn(day)) {
                endTimeFields[i].setText(schedule.getTimeSlot(day).getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            } else {
                endTimeFields[i].setText("17:00");
            }
            mainPanel.add(endTimeFields[i], gbc);
            
            // Add listener to checkbox
            final int index = i;
            dayCheckBoxes[i].addActionListener(e -> {
                startTimeFields[index].setEnabled(dayCheckBoxes[index].isSelected());
                endTimeFields[index].setEnabled(dayCheckBoxes[index].isSelected());
            });
            
            // Set initial enabled state
            startTimeFields[i].setEnabled(dayCheckBoxes[i].isSelected());
            endTimeFields[i].setEnabled(dayCheckBoxes[i].isSelected());
        }
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Save Schedule");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> saveSchedule());
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void saveSchedule() {
        try {
            DoctorSchedule newSchedule = new DoctorSchedule();
            DayOfWeek[] days = DayOfWeek.values();
            
            for (int i = 0; i < days.length; i++) {
                if (dayCheckBoxes[i].isSelected()) {
                    String startTimeStr = startTimeFields[i].getText().trim();
                    String endTimeStr = endTimeFields[i].getText().trim();
                    
                    if (startTimeStr.isEmpty() || endTimeStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, 
                            "Please enter both start and end times for " + days[i], 
                            "Missing Time", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    LocalTime startTime = LocalTime.parse(startTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
                    LocalTime endTime = LocalTime.parse(endTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
                    
                    if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
                        JOptionPane.showMessageDialog(this, 
                            "Start time must be before end time for " + days[i], 
                            "Invalid Time Range", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    newSchedule.setDaySchedule(days[i], startTime, endTime);
                }
            }
            
            doctor.setSchedule(newSchedule);
            saved = true;
            dispose();
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid time format. Please use HH:MM format (e.g., 09:00)", 
                "Invalid Time Format", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isSaved() {
        return saved;
    }
}