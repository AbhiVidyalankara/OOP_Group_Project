package Medicare;

import java.io.Serializable;

public class Patient implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String phone;
    private String medicalHistory;

    public Patient(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.medicalHistory = "";
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getMedicalHistory() { return medicalHistory; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }

    @Override
    public String toString() {
        return name + " [" + id + "]";
    }
}