// --- Doctor.java ---
package Medicare;

import java.io.Serializable;

public class Doctor implements Serializable {
    private static final long serialVersionUID = 1L;
    public String id;
    public String name;
    public String specialty;

    public Doctor(String id, String name, String specialty) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
    }

    @Override
    public String toString() {
        return name + " (" + specialty + ")";
    }
}