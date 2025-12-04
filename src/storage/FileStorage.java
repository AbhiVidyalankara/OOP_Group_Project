package storage;

import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {
    private static final String PATIENTS_FILE = "patients.dat";
    private static final String DOCTORS_FILE = "doctors.dat";
    private static final String APPOINTMENTS_FILE = "appointments.dat";
    private static final String NOTIFICATIONS_FILE = "notifications.dat";

    public static void savePatients(List<Patient> patients) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PATIENTS_FILE))) {
            oos.writeObject(patients);
        } catch (IOException e) {
            System.err.println("Error saving patients: " + e.getMessage());
        }
    }

    public static void saveDoctors(List<Doctor> doctors) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DOCTORS_FILE))) {
            oos.writeObject(doctors);
        } catch (IOException e) {
            System.err.println("Error saving doctors: " + e.getMessage());
        }
    }

    public static void saveAppointments(List<Appointment> appointments) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(APPOINTMENTS_FILE))) {
            oos.writeObject(appointments);
        } catch (IOException e) {
            System.err.println("Error saving appointments: " + e.getMessage());
        }
    }

    public static void saveNotifications(List<Notification> notifications) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOTIFICATIONS_FILE))) {
            oos.writeObject(notifications);
        } catch (IOException e) {
            System.err.println("Error saving notifications: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Patient> loadPatients() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PATIENTS_FILE))) {
            return (List<Patient>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Doctor> loadDoctors() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DOCTORS_FILE))) {
            return (List<Doctor>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Appointment> loadAppointments() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(APPOINTMENTS_FILE))) {
            return (List<Appointment>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Notification> loadNotifications() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NOTIFICATIONS_FILE))) {
            return (List<Notification>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}