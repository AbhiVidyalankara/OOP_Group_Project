package Medicare;

import java.util.ArrayList;

public class AssignmentService {
    private ArrayList<DoctorAssignment> assignments = new ArrayList<>();
    
    public void assignDoctor(String patientId, String doctorId) {
        for (DoctorAssignment a : assignments) {
            if (a.getPatientId().equals(patientId)) {
                a.setDoctorId(doctorId);
                System.out.println("Doctor reassigned successfully!");
                return;
            }
        }
        assignments.add(new DoctorAssignment(patientId, doctorId));
        System.out.println("Doctor assigned successfully!");
    }
    
    public String getAssignedDoctor(String patientId) {
        for (DoctorAssignment a : assignments) {
            if (a.getPatientId().equals(patientId)) {
                return a.getDoctorId();
            }
        }
        return null;
    }
    
    public ArrayList<String> getPatientsByDoctor(String doctorId) {
        ArrayList<String> patients = new ArrayList<>();
        for (DoctorAssignment a : assignments) {
            if (a.getDoctorId().equals(doctorId)) {
                patients.add(a.getPatientId());
            }
        }
        return patients;
    }
}
