package Medicare;

import java.util.ArrayList;

public class DoctorService {

    public void loadDoctors(ArrayList<Doctor> loadedList) {
        if (loadedList != null) {
            Datastore.store.doctors = loadedList;
        }
    }

    public ArrayList<Doctor> getDoctors() {
        return Datastore.store.doctors;
    }

    public void addDoctor(Doctor doctor) {
        Datastore.store.doctors.add(doctor);
        System.out.println("Doctor added successfully!");
    }

    public boolean updateDoctor(String id, String newName, String newSpecialty) {
        for (Doctor d : Datastore.store.doctors) {
            if (d.getId().equals(id)) {
                d.setName(newName);
                d.setSpecialty(newSpecialty);
                System.out.println("Doctor updated successfully!");
                return true;
            }
        }
        System.out.println("Doctor not found!");
        return false;
    }

    public boolean deleteDoctor(String id) {
        for (Doctor d : Datastore.store.doctors) {
            if (d.getId().equals(id)) {
                Datastore.store.doctors.remove(d);
                System.out.println("Doctor removed successfully!");
                return true;
            }
        }
        System.out.println("Doctor not found!");
        return false;
    }

    public void listDoctors() {
        if (Datastore.store.doctors.isEmpty()) {
            System.out.println("No doctors available.");
        } else {
            System.out.println("\n--- Doctor List ---");
            for (Doctor d : Datastore.store.doctors) {
                System.out.println(d);
            }
        }
    }
}
