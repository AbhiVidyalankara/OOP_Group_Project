package Medicare;

import java.io.*;
import java.time.*;
import java.util.*;

public class Datastore implements Serializable {
    private static final long serialVersionUID = 1L;
    // Changed to public static to be accessible from main/UI
    public static Datastore store;

    public List<Doctor> doctors = new ArrayList<>();
    public List<Patient> patients = new ArrayList<>();
    public List<Appointment> appointments = new ArrayList<>();

    private static final String FILE = "data.ser";

    // Public constructor for creating a new instance
    public Datastore() {
        // Required for serialization
    }

    // Static initializer method to load or create data
    public static void initialize() {
        try {
            store = load();
            System.out.println("Datastore loaded from file.");
        } catch (Exception e) {
            // File not found or failed to load, create new data
            store = new Datastore();
            store.seedSampleData();
            System.out.println("Datastore file not found, seeding new data.");
        }
    }

    public static Datastore load() throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE))) {
            return (Datastore) ois.readObject();
        }
    }

    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
            oos.writeObject(this);
            // System.out.println("Data saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void seedSampleData() {
        // Add the requested doctors
        Doctor d1 = new Doctor("D001", "Dr. Ashan", "General Physician");
        Doctor d2 = new Doctor("D002", "Dr. Rohan", "Pediatrics");
        Doctor d3 = new Doctor("D003", "Dr. Mia Chen", "Cardiologist");
        Doctor d4 = new Doctor("D004", "Dr. Lena Khan", "Dermatologist");
        Doctor d5 = new Doctor("D005", "Dr. Elias Rodriguez", "Orthopedic Surgeon");
        doctors.add(d1);
        doctors.add(d2);
        doctors.add(d3);
        doctors.add(d4);
        doctors.add(d5);

        // Add a sample patient for the appointment
        patients.add(new Patient("P001", "Alice Smith", "555-1234"));

        // sample appointment tomorrow 10:00
        appointments.add(new Appointment(UUID.randomUUID().toString(), d1.id, "P001", LocalDate.now().plusDays(1), LocalTime.of(10,0), "Confirmed"));

        save();
    }
}