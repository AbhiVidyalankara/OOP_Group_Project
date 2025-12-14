import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MedicareApp extends JFrame {
    private static DoctorService doctorService = new DoctorService();
    private static PatientService patientService = new PatientService();
    private static AssignmentService assignmentService = new AssignmentService();
    private static DoctorPanel doctorPanel;
    private static PatientPanel patientPanel;
    private static AppointmentPanel appointmentPanel;
    private JTabbedPane tabbedPane;
    private static MedicareApp instance;
    
    public MedicareApp() {
        instance = this;
        setTitle("MedicarePlus");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
        startStatusUpdateTimer();
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Datastore.store.save();
                System.exit(0);
            }
        });
    }
    
    private void initComponents() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        doctorPanel = new DoctorPanel(doctorService);
        patientPanel = new PatientPanel(patientService);
        appointmentPanel = new AppointmentPanel();
        
        tabbedPane.addTab("Dashboard", createDashboardPanel());
        tabbedPane.addTab("Doctors", doctorPanel);
        tabbedPane.addTab("Patients", patientPanel);
        tabbedPane.addTab("Appointments", appointmentPanel);
        tabbedPane.addTab("Reports", new ReportPanel());
        tabbedPane.addTab("Notifications", new NotificationPanel());
        
        add(tabbedPane);
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("MedicarePlus", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(new Color(0, 102, 204));
        panel.add(title, BorderLayout.NORTH);
        
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        statsPanel.add(createStatCard("Total Doctors", String.valueOf(Datastore.store.doctors.size()), new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Total Patients", String.valueOf(Datastore.store.patients.size()), new Color(46, 204, 113)));
        statsPanel.add(createStatCard("Total Appointments", String.valueOf(Datastore.store.appointments.size()), new Color(155, 89, 182)));
        statsPanel.add(createStatCard("Doctors Active Today", String.valueOf(getTodayAppointments()), new Color(230, 126, 34)));
        
        panel.add(statsPanel, BorderLayout.CENTER);
        
        JLabel footer = new JLabel("Advanced Healthcare Management Platform", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        footer.setForeground(Color.GRAY);
        panel.add(footer, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createStatCard(String label, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        valueLabel.setForeground(color);
        
        JLabel textLabel = new JLabel(label, SwingConstants.CENTER);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        textLabel.setForeground(Color.DARK_GRAY);
        
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(textLabel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private int getTodayAppointments() {
        int count = 0;
        for (Doctor doctor : Datastore.store.doctors) {
            if (doctor.isAvailableToday()) {
                count++;
            }
        }
        return count;
    }
    
    public static DoctorService getDoctorService() { return doctorService; }
    public static PatientService getPatientService() { return patientService; }
    public static AssignmentService getAssignmentService() { return assignmentService; }
    
    public static void refreshAllPanels() {
        updateDoctorStatuses();
        if (doctorPanel != null) doctorPanel.refreshTable();
        if (patientPanel != null) patientPanel.refreshTable();
        if (appointmentPanel != null) appointmentPanel.refreshTable();
        if (instance != null) {
            instance.tabbedPane.setComponentAt(0, instance.createDashboardPanel());
        }
        Datastore.store.save();
    }
    
    private static void updateDoctorStatuses() {
        for (Doctor doctor : Datastore.store.doctors) {
            doctor.isAvailableToday(); // This triggers status update
        }
    }
    
    private void startStatusUpdateTimer() {
        Timer timer = new Timer(60000, e -> {
            updateDoctorStatuses();
            if (doctorPanel != null) doctorPanel.refreshTable();
            tabbedPane.setComponentAt(0, createDashboardPanel());
        });
        timer.start();
    }
    
    public static void main(String[] args) {
        DatabaseConnection.initDatabase();
        Datastore.store.load();
        doctorService.loadDoctors(Datastore.store.doctors);
        patientService.loadPatients(Datastore.store.patients);
        
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MedicareApp().setVisible(true);
        });
    }
}
