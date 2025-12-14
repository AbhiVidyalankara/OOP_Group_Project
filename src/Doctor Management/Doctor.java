import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class Doctor implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String specialty;
    private String email;
    private String phone;
    private String status;
    private DoctorSchedule schedule;
    private boolean manualStatusOverride = false;
    private java.time.LocalDate lastStatusUpdate;

    public Doctor(String id, String name, String specialty) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.status = "Available";
        this.schedule = new DoctorSchedule();
    }
    
    public Doctor(String id, String name, String specialty, String email, String phone) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.email = email;
        this.phone = phone;
        this.status = "Available";
        this.schedule = new DoctorSchedule();
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpecialty() { return specialty; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getStatus() { return status; }
    public DoctorSchedule getSchedule() { return schedule; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setStatus(String status) { 
        this.status = status;
        this.manualStatusOverride = true;
        this.lastStatusUpdate = java.time.LocalDate.now();
    }
    
    public void setStatusAutomatic(String status) {
        this.status = status;
    }
    public void setSchedule(DoctorSchedule schedule) { this.schedule = schedule; }
    
    public boolean isAvailableToday() {
        updateStatusBasedOnSchedule();
        if (schedule == null) {
            return "Available".equals(status);
        }
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        return "Available".equals(status) && schedule.isAvailableOn(today);
    }
    
    private void updateStatusBasedOnSchedule() {
        if (schedule == null) return;
        
        java.time.LocalDate today = java.time.LocalDate.now();
        
        // Reset manual override at midnight (new day)
        if (manualStatusOverride && lastStatusUpdate != null && !lastStatusUpdate.equals(today)) {
            manualStatusOverride = false;
        }
        
        // Skip automatic updates if manual override is active
        if (manualStatusOverride) return;
        
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        java.time.LocalTime now = java.time.LocalTime.now();
        
        if (schedule.isAvailableOn(dayOfWeek)) {
            DoctorSchedule.TimeSlot slot = schedule.getTimeSlot(dayOfWeek);
            if (now.isBefore(slot.getStartTime()) || now.isAfter(slot.getEndTime())) {
                setStatusAutomatic("Unavailable");
            } else {
                setStatusAutomatic("Available");
            }
        } else {
            setStatusAutomatic("Unavailable");
        }
    }

    @Override
    public String toString() {
        return id + " - " + name + " (" + specialty + ")";
    }
}
