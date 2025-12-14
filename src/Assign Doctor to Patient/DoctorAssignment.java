import java.io.Serializable;

public class DoctorAssignment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String patientId;
    private String doctorId;
    
    public DoctorAssignment(String patientId, String doctorId) {
        this.patientId = patientId;
        this.doctorId = doctorId;
    }
    
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
}
