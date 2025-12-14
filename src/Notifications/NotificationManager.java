import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationManager {
    
    public static void sendToDoctor(String doctorId, String message) {
        Datastore.store.doctorNotifications.putIfAbsent(doctorId, new ArrayList<>());
        
        Doctor doctor = findDoctor(doctorId);
        StringBuilder deliveryMethod = new StringBuilder("System");
        
        if (doctor != null) {
            if (doctor.getEmail() != null && !doctor.getEmail().isEmpty()) {
                System.out.println("[EMAIL to " + doctor.getEmail() + "] " + message);
                deliveryMethod.append(", Email");
            }
            if (doctor.getPhone() != null && !doctor.getPhone().isEmpty()) {
                System.out.println("[SMS to " + doctor.getPhone() + "] " + message);
                deliveryMethod.append(", SMS");
            }
        }
        
        Datastore.store.doctorNotifications.get(doctorId).add(new Notification(doctorId, message, deliveryMethod.toString()));
    }
    
    public static void sendToPatient(String patientId, String message) {
        Datastore.store.patientNotifications.putIfAbsent(patientId, new ArrayList<>());
        
        Patient patient = findPatient(patientId);
        StringBuilder deliveryMethod = new StringBuilder("System");
        
        if (patient != null) {
            if (patient.getEmail() != null && !patient.getEmail().isEmpty()) {
                System.out.println("[EMAIL to " + patient.getEmail() + "] " + message);
                deliveryMethod.append(", Email");
            }
            if (patient.getPhone() != null && !patient.getPhone().isEmpty()) {
                System.out.println("[SMS to " + patient.getPhone() + "] " + message);
                deliveryMethod.append(", SMS");
            }
        }
        
        Datastore.store.patientNotifications.get(patientId).add(new Notification(patientId, message, deliveryMethod.toString()));
    }
    
    public static List<Notification> getDoctorNotifications(String doctorId) {
        return Datastore.store.doctorNotifications.getOrDefault(doctorId, new ArrayList<>());
    }
    
    public static List<Notification> getPatientNotifications(String patientId) {
        return Datastore.store.patientNotifications.getOrDefault(patientId, new ArrayList<>());
    }
    
    private static Doctor findDoctor(String id) {
        for (Doctor d : Datastore.store.doctors) {
            if (d.getId().equals(id)) return d;
        }
        return null;
    }
    
    private static Patient findPatient(String id) {
        for (Patient p : Datastore.store.patients) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }
}
