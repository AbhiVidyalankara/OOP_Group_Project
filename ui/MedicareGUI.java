import javax.swing.*;
import java.awt.*;

public class MedicareGUI extends JFrame {
    private JTabbedPane tabbedPane;
    
    public MedicareGUI() {
        setTitle("Medicare Management System");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Doctors", new DoctorPanel());
        tabbedPane.addTab("Patients", new PatientPanel());
        tabbedPane.addTab("Appointments", new AppointmentPanel());
        tabbedPane.addTab("Reports", new ReportPanel());
        
        add(tabbedPane);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MedicareGUI().setVisible(true));
    }
}
