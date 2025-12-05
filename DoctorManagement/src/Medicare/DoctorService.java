package Medicare;

import java.util.ArrayList;

public class DoctorService {

    private ArrayList<Doctor> doctors = new ArrayList<>();

    public DoctorService() {}

    public void loadDoctors(ArrayList<Doctor> loadedList) {
        if (loadedList != null) {
            this.doctors = loadedList;
        }
    }

    public ArrayList<Doctor> getDoctors() {
        return doctors;
    }

    // ADD doctor
    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
        System.out.println("Doctor added successfully!");
    }

    // UPDATE doctor
    public boolean updateDoctor(String id, String newName, String newSpecialty) {
        for (Doctor d : doctors) {
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

    // DELETE doctor
    public boolean deleteDoctor(String id) {
        for (Doctor d : doctors) {
            if (d.getId().equals(id)) {
                doctors.remove(d);
                System.out.println("Doctor removed successfully!");
                return true;
            }
        }
        System.out.println("Doctor not found!");
        return false;
    }

    // LIST doctors
    public void listDoctors() {
        if (doctors.isEmpty()) {
            System.out.println("No doctors available.");
        } else {
            System.out.println("\n--- Doctor List ---");
            for (Doctor d : doctors) {
                System.out.println(d);
            }
        }
    }
}
