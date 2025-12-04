package model;

import java.io.Serializable;

public class Patient implements Serializable {
    private int id;
    private String name;
    private String contact;
    private String medicalHistory;

    public Patient(int id, String name, String contact, String medicalHistory) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.medicalHistory = medicalHistory;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }

    @Override
    public String toString() {
        return name;
    }
}