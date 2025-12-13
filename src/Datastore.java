package Medicare;

import java.io.*;
import java.util.ArrayList;

public class Datastore {
    public static Datastore store = new Datastore();
    
    public ArrayList<Doctor> doctors = new ArrayList<>();
    public ArrayList<Patient> patients = new ArrayList<>();
    public ArrayList<Appointment> appointments = new ArrayList<>();
    
    private static final String FILE = "medicare_data.dat";
    
    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
            oos.writeObject(doctors);
            oos.writeObject(patients);
            oos.writeObject(appointments);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public void load() {
        File f = new File(FILE);
        if (!f.exists()) return;
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE))) {
            doctors = (ArrayList<Doctor>) ois.readObject();
            patients = (ArrayList<Patient>) ois.readObject();
            appointments = (ArrayList<Appointment>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
}
