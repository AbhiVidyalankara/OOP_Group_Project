package Medicare;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PatientService {
    public void loadPatients(ArrayList<Patient> loadedList) {
        if (loadedList != null) {
            Datastore.store.patients = loadedList;
        }
    }

    public ArrayList<Patient> getPatients() {
        return Datastore.store.patients;
    }

    public void addPatient(Patient patient) {
        Datastore.store.patients.add(patient);
        System.out.println("Patient added successfully!");
    }

    public boolean updatePatient(String id, String newName, String newPhone) {
        for (Patient p : Datastore.store.patients) {
            if (p.getId().equals(id)) {
                p.setName(newName);
                p.setPhone(newPhone);
                System.out.println("Patient updated successfully!");
                return true;
            }
        }
        System.out.println("Patient not found!");
        return false;
    }

    public boolean deletePatient(String id) {
        for (Patient p : Datastore.store.patients) {
            if (p.getId().equals(id)) {
                Datastore.store.patients.remove(p);
                System.out.println("Patient removed successfully!");
                return true;
            }
        }
        System.out.println("Patient not found!");
        return false;
    }

    public void listPatients() {
        if (Datastore.store.patients.isEmpty()) {
            System.out.println("No patients available.");
        } else {
            System.out.println("\n--- Patient List ---");
            for (Patient p : Datastore.store.patients) {
                System.out.println(p);
            }
        }
    }
}

