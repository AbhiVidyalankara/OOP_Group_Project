package Medicare;

import java.io.Serializable;

public class Doctor implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String specialty;
    private String workingSchedule;
    private boolean available;

    public Doctor(String id, String name, String specialty) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.workingSchedule = "Mon-Fri 9AM-5PM";
        this.available = true;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpecialty() { return specialty; }
    public String getWorkingSchedule() { return workingSchedule; }
    public boolean isAvailable() { return available; }

    public void setName(String name) { this.name = name; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public void setWorkingSchedule(String schedule) { this.workingSchedule = schedule; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return id + " - " + name + " (" + specialty + ") - " + (available ? "Available" : "Unavailable");
    }
}
