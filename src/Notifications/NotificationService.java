public class NotificationService {
    
    public static void sendAppointmentNotifications(String doctorId, String patientId, String date, String time) {
        Doctor doctor = findDoctor(doctorId);
        Patient patient = findPatient(patientId);
        
        if (doctor != null) {
            String doctorMsg = "New Appointment Scheduled\n" +
                              "Patient: " + (patient != null ? patient.getName() : patientId) + "\n" +
                              "Patient ID: " + patientId + "\n" +
                              "Situation: " + (patient != null && patient.getSituation() != null ? patient.getSituation() : "Not specified") + "\n" +
                              "Date: " + date + "\n" +
                              "Time: " + time;
            
            if (doctor.getEmail() != null && !doctor.getEmail().isEmpty()) {
                sendEmail(doctor.getEmail(), "New Appointment", doctorMsg);
            }
            if (doctor.getPhone() != null && !doctor.getPhone().isEmpty()) {
                sendSMS(doctor.getPhone(), doctorMsg);
            }
            NotificationManager.sendToDoctor(doctorId, doctorMsg);
        }
        
        if (patient != null) {
            String patientMsg = "Appointment Confirmed\n" +
                               "Doctor: " + (doctor != null ? doctor.getName() : doctorId) + "\n" +
                               "Specialty: " + (doctor != null ? doctor.getSpecialty() : "N/A") + "\n" +
                               "Date: " + date + "\n" +
                               "Time: " + time;
            
            if (patient.getEmail() != null && !patient.getEmail().isEmpty()) {
                sendEmail(patient.getEmail(), "Appointment Confirmation", patientMsg);
            }
            if (patient.getPhone() != null && !patient.getPhone().isEmpty()) {
                sendSMS(patient.getPhone(), patientMsg);
            }
            NotificationManager.sendToPatient(patientId, patientMsg);
        }
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
    
    private static void sendEmail(String email, String subject, String message) {
        System.out.println("=== EMAIL SENT ===");
        System.out.println("To: " + email);
        System.out.println("Subject: " + subject);
        System.out.println("Message:\n" + message);
        System.out.println("==================\n");
    }
    
    private static void sendSMS(String phone, String message) {
        System.out.println("=== SMS SENT ===");
        System.out.println("To: " + phone);
        System.out.println("Message:\n" + message);
        System.out.println("================\n");
    }
}
