package service;

import model.Doctor;
import storage.FileStorage;

import java.util.ArrayList;
import java.util.List;

public class DoctorService {
    private static List<Doctor> doctors = new ArrayList<>();
    private static int nextId = 1;

    static {
        doctors = FileStorage.loadDoctors();
        if (!doctors.isEmpty()) {
            nextId = doctors.stream().mapToInt(Doctor::getId).max().orElse(0) + 1;
        }
    }

    public static void addDoctor(Doctor doctor) {
        doctor.setId(nextId++);
        doctors.add(doctor);
        FileStorage.saveDoctors(doctors);
        NotificationService.addNotification("SYSTEM", "Admin", "New doctor added: " + doctor.getName());
    }

    public static List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctors);
    }

    public static Doctor getDoctorById(int id) {
        for (Doctor doctor : doctors) {
            if (doctor.getId() == id) {
                return doctor;
            }
        }
        return null;
    }

    public static void updateDoctor(Doctor updatedDoctor) {
        for (int i = 0; i < doctors.size(); i++) {
            if (doctors.get(i).getId() == updatedDoctor.getId()) {
                doctors.set(i, updatedDoctor);
                FileStorage.saveDoctors(doctors);
                return;
            }
        }
    }

    public static boolean deleteDoctor(int id) {
        // Check if doctor has appointments
        if (AppointmentService.hasAppointmentsForDoctor(id)) {
            return false;
        }

        doctors.removeIf(d -> d.getId() == id);
        FileStorage.saveDoctors(doctors);
        return true;
    }

    public static Doctor assignDoctor(String specialty) {
        for (Doctor doctor : doctors) {
            if (doctor.getSpecialty().equalsIgnoreCase(specialty)) {
                return doctor;
            }
        }
        return null;
    }
}