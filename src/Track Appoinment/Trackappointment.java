import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Trackappointment {
    
    public static List<Appointment> getAppointmentsByDoctor(String doctorId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : Datastore.store.appointments) {
            if (a.doctorId.equals(doctorId)) {
                result.add(a);
            }
        }
        return result;
    }
    
    public static List<Appointment> getAppointmentsByPatient(String patientId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : Datastore.store.appointments) {
            if (a.patientId.equals(patientId)) {
                result.add(a);
            }
        }
        return result;
    }
    
    public static List<Appointment> getAppointmentsByDate(LocalDate date) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : Datastore.store.appointments) {
            if (a.date.equals(date)) {
                result.add(a);
            }
        }
        return result;
    }
}
