package Medicare;

import java.time.LocalDate;
import java.time.YearMonth;

public class MonthlyReport {
    
    public static String generateReport(YearMonth month) {
        StringBuilder report = new StringBuilder();
        report.append("=== MONTHLY REPORT FOR ").append(month).append(" ===\n\n");
        
        int totalAppointments = 0;
        int completedAppointments = 0;
        int cancelledAppointments = 0;
        
        for (Appointment apt : Datastore.store.appointments) {
            if (YearMonth.from(apt.date).equals(month)) {
                totalAppointments++;
                if ("Completed".equalsIgnoreCase(apt.status)) {
                    completedAppointments++;
                } else if ("Cancelled".equalsIgnoreCase(apt.status)) {
                    cancelledAppointments++;
                }
            }
        }
        
        report.append("Total Appointments: ").append(totalAppointments).append("\n");
        report.append("Completed: ").append(completedAppointments).append("\n");
        report.append("Cancelled: ").append(cancelledAppointments).append("\n");
        report.append("Pending: ").append(totalAppointments - completedAppointments - cancelledAppointments).append("\n\n");
        
        report.append("Total Doctors: ").append(Datastore.store.doctors.size()).append("\n");
        report.append("Total Patients: ").append(Datastore.store.patients.size()).append("\n");
        
        return report.toString();
    }
}
