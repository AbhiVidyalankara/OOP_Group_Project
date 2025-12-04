package notifications;

public class Patient {
    public String patientId;
    public String name;
    public String contact;

    public Patient(String patientId, String name, String contact) {
        this.patientId = patientId;
        this.name = name;
        this.contact = contact;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }
}