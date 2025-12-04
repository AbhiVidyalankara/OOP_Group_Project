package model;

import java.io.Serializable;

public class Doctor implements Serializable {
    private int id;
    private String name;
    private String specialty;
    private String availability;

    public Doctor(int id, String name, String specialty, String availability) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.availability = availability;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    @Override
    public String toString() {
        return name + " (" + specialty + ")";
    }
}