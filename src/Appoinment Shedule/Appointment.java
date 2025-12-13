// --- Appointment.java ---
package Medicare;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment implements Serializable {
    private static final long serialVersionUID = 1L;
    public String id;
    public String doctorId;
    public String patientId;
    public LocalDate date;
    public LocalTime time;
    public String status;

    public Appointment(String id, String doctorId, String patientId, LocalDate date, LocalTime time, String status) {
        this.id = id;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.date = date;
        this.time = time;
        this.status = status;
    }
}