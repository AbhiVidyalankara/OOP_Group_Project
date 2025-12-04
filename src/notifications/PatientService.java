package notifications;

import java.util.ArrayList;
import java.util.List;

public class PatientService {
    private List<Patient> patients = new ArrayList<>();
    private NotificationService notificationService = new NotificationService();

    public void addPatient(String patientId, String name, String contact, String doctorId, String situation) {
        Patient newPatient = new Patient(patientId, name, contact);
        patients.add(newPatient);

        String category = "New Patient";
        notificationService.sendDoctorNotification(doctorId, patientId, name, situation, category);
    }

    public List<Patient> getAllPatients() {
        return patients;
    }
}