// --- Patient.java ---
package Medicare;

import java.io.Serializable;

public class Patient implements Serializable {
    private static final long serialVersionUID = 1L;
    public String id;
    public String name;
    public String phone;

    public Patient(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }
}