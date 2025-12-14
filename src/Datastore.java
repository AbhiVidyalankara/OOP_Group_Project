import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class Datastore {
    public static Datastore store = new Datastore();
    
    public ArrayList<Doctor> doctors = new ArrayList<>();
    public ArrayList<Patient> patients = new ArrayList<>();
    public ArrayList<Appointment> appointments = new ArrayList<>();
    public java.util.Map<String, java.util.List<Notification>> doctorNotifications = new java.util.HashMap<>();
    public java.util.Map<String, java.util.List<Notification>> patientNotifications = new java.util.HashMap<>();
    
    private static final String FILE = "medicare_data.dat";
    private static boolean useDatabase = true;
    
    public void save() {
        if (useDatabase) {
            saveToDatabase();
        } else {
            saveToFile();
        }
    }
    
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
            oos.writeObject(doctors);
            oos.writeObject(patients);
            oos.writeObject(appointments);
            oos.writeObject(doctorNotifications);
            oos.writeObject(patientNotifications);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }
    
    private void saveToDatabase() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            saveDoctors(conn);
            saveDoctorSchedules(conn);
            savePatients(conn);
            saveAppointments(conn);
            saveNotifications(conn);
        } catch (SQLException e) {
            System.err.println("Database save error: " + e.getMessage());
        }
    }
    
    private void saveDoctors(Connection conn) throws SQLException {
        conn.createStatement().execute("DELETE FROM doctors");
        PreparedStatement ps = conn.prepareStatement("INSERT INTO doctors VALUES (?,?,?,?,?,?)");
        for (Doctor d : doctors) {
            ps.setString(1, d.getId());
            ps.setString(2, d.getName());
            ps.setString(3, d.getSpecialty());
            ps.setString(4, d.getEmail());
            ps.setString(5, d.getPhone());
            ps.setString(6, d.getStatus());
            ps.executeUpdate();
        }
    }
    
    private void saveDoctorSchedules(Connection conn) throws SQLException {
        conn.createStatement().execute("DELETE FROM doctor_schedules");
        PreparedStatement ps = conn.prepareStatement("INSERT INTO doctor_schedules VALUES (?,?,?,?)");
        for (Doctor d : doctors) {
            if (d.getSchedule() != null) {
                for (java.time.DayOfWeek day : d.getSchedule().getWeeklySchedule().keySet()) {
                    DoctorSchedule.TimeSlot slot = d.getSchedule().getTimeSlot(day);
                    ps.setString(1, d.getId());
                    ps.setString(2, day.toString());
                    ps.setTime(3, java.sql.Time.valueOf(slot.getStartTime()));
                    ps.setTime(4, java.sql.Time.valueOf(slot.getEndTime()));
                    ps.executeUpdate();
                }
            }
        }
    }
    
    private void savePatients(Connection conn) throws SQLException {
        conn.createStatement().execute("DELETE FROM patients");
        PreparedStatement ps = conn.prepareStatement("INSERT INTO patients VALUES (?,?,?,?,?)");
        for (Patient p : patients) {
            ps.setString(1, p.getId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getPhone());
            ps.setString(4, p.getEmail());
            ps.setString(5, p.getSituation());
            ps.executeUpdate();
        }
    }
    
    private void saveAppointments(Connection conn) throws SQLException {
        conn.createStatement().execute("DELETE FROM appointments");
        PreparedStatement ps = conn.prepareStatement("INSERT INTO appointments VALUES (?,?,?,?,?,?)");
        for (Appointment a : appointments) {
            ps.setString(1, a.id);
            ps.setString(2, a.doctorId);
            ps.setString(3, a.patientId);
            ps.setString(4, a.date != null ? a.date.toString() : "");
            ps.setString(5, a.time != null ? a.time.toString() : "");
            ps.setString(6, a.status);
            ps.executeUpdate();
        }
    }
    
    private void saveNotifications(Connection conn) throws SQLException {
        conn.createStatement().execute("DELETE FROM notifications");
        PreparedStatement ps = conn.prepareStatement("INSERT INTO notifications (recipientId, type, message, deliveryMethod) VALUES (?,?,?,?)");
        for (String id : doctorNotifications.keySet()) {
            for (Notification n : doctorNotifications.get(id)) {
                ps.setString(1, id);
                ps.setString(2, "Doctor");
                ps.setString(3, n.getMessage());
                ps.setString(4, n.getDeliveryMethod());
                ps.executeUpdate();
            }
        }
        for (String id : patientNotifications.keySet()) {
            for (Notification n : patientNotifications.get(id)) {
                ps.setString(1, id);
                ps.setString(2, "Patient");
                ps.setString(3, n.getMessage());
                ps.setString(4, n.getDeliveryMethod());
                ps.executeUpdate();
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public void load() {
        if (useDatabase) {
            loadFromDatabase();
        } else {
            loadFromFile();
        }
    }
    
    private void loadFromFile() {
        File f = new File(FILE);
        if (!f.exists()) return;
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE))) {
            doctors = (ArrayList<Doctor>) ois.readObject();
            patients = (ArrayList<Patient>) ois.readObject();
            appointments = (ArrayList<Appointment>) ois.readObject();
            try {
                doctorNotifications = (java.util.Map<String, java.util.List<Notification>>) ois.readObject();
                patientNotifications = (java.util.Map<String, java.util.List<Notification>>) ois.readObject();
            } catch (Exception e) {
                doctorNotifications = new java.util.HashMap<>();
                patientNotifications = new java.util.HashMap<>();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
    
    private void loadFromDatabase() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            loadDoctors(conn);
            loadDoctorSchedules(conn);
            loadPatients(conn);
            loadAppointments(conn);
            loadNotifications(conn);
        } catch (SQLException e) {
            System.err.println("Database load error: " + e.getMessage());
        }
    }
    
    private void loadDoctors(Connection conn) throws SQLException {
        doctors.clear();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM doctors");
        while (rs.next()) {
            Doctor d = new Doctor(rs.getString("id"), rs.getString("name"), rs.getString("specialty"), rs.getString("email"), rs.getString("phone"));
            d.setStatus(rs.getString("status"));
            doctors.add(d);
        }
    }
    
    private void loadDoctorSchedules(Connection conn) throws SQLException {
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM doctor_schedules");
        while (rs.next()) {
            String doctorId = rs.getString("doctorId");
            for (Doctor d : doctors) {
                if (d.getId().equals(doctorId)) {
                    if (d.getSchedule() == null) d.setSchedule(new DoctorSchedule());
                    java.time.DayOfWeek day = java.time.DayOfWeek.valueOf(rs.getString("dayOfWeek"));
                    java.time.LocalTime start = rs.getTime("startTime").toLocalTime();
                    java.time.LocalTime end = rs.getTime("endTime").toLocalTime();
                    d.getSchedule().setDaySchedule(day, start, end);
                    break;
                }
            }
        }
    }
    
    private void loadPatients(Connection conn) throws SQLException {
        patients.clear();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM patients");
        while (rs.next()) {
            patients.add(new Patient(rs.getString("id"), rs.getString("name"), rs.getString("phone"), rs.getString("email"), rs.getString("situation")));
        }
    }
    
    private void loadAppointments(Connection conn) throws SQLException {
        appointments.clear();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM appointments");
        while (rs.next()) {
            Appointment a = new Appointment();
            a.id = rs.getString("id");
            a.doctorId = rs.getString("doctorId");
            a.patientId = rs.getString("patientId");
            String dateStr = rs.getString("date");
            String timeStr = rs.getString("time");
            a.date = (dateStr != null && !dateStr.isEmpty()) ? java.time.LocalDate.parse(dateStr) : null;
            a.time = (timeStr != null && !timeStr.isEmpty()) ? java.time.LocalTime.parse(timeStr) : null;
            a.status = rs.getString("status");
            appointments.add(a);
        }
    }
    
    private void loadNotifications(Connection conn) throws SQLException {
        doctorNotifications.clear();
        patientNotifications.clear();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM notifications");
        while (rs.next()) {
            String id = rs.getString("recipientId");
            String type = rs.getString("type");
            Notification n = new Notification(id, rs.getString("message"), rs.getString("deliveryMethod"));
            if (type.equals("Doctor")) {
                doctorNotifications.computeIfAbsent(id, k -> new ArrayList<>()).add(n);
            } else {
                patientNotifications.computeIfAbsent(id, k -> new ArrayList<>()).add(n);
            }
        }
    }
}
