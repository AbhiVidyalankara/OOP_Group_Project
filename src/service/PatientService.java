package service;

import model.Patient;
import storage.FileStorage;

import java.util.ArrayList;
import java.util.List;

public class PatientService {
    private static List<Patient> patients = new ArrayList<>();
    private static int nextId = 1;

    static {
        patients = FileStorage.loadPatients();
        if (!patients.isEmpty()) {
            nextId = patients.stream().mapToInt(Patient::getId).max().orElse(0) + 1;
        }
    }

    public static void addPatient(Patient patient) {
        patient.setId(nextId++);
        patients.add(patient);
        FileStorage.savePatients(patients);
        NotificationService.addNotification("SYSTEM", "Admin", "New patient added: " + patient.getName());
    }

    public static List<Patient> getAllPatients() {
        return new ArrayList<>(patients);
    }

    public static Patient getPatientById(int id) {
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                return patient;
            }
        }
        return null;
    }

    public static void updatePatient(Patient updatedPatient) {
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getId() == updatedPatient.getId()) {
                patients.set(i, updatedPatient);
                FileStorage.savePatients(patients);
                return;
            }
        }
    }

    public static boolean deletePatient(int id) {
        // Check if patient has appointments
        if (AppointmentService.hasAppointmentsForPatient(id)) {
            return false;
        }

        patients.removeIf(p -> p.getId() == id);
        FileStorage.savePatients(patients);
        return true;
    }
}