import org.junit.*;
import static org.junit.Assert.*;

public class PatientPanelTest {
    
    @Before
    public void setUp() {
        Datastore.store = new Datastore();
    }
    
    @Test
    public void testPhoneValidation() {
        String validPhone = "987-654-3210";
        String invalidPhone = "xyz789";
        
        assertTrue(validPhone.matches("[0-9\\-\\+\\(\\)\\s]+"));
        assertFalse(invalidPhone.matches("[0-9\\-\\+\\(\\)\\s]+"));
    }
    
    @Test
    public void testEmailValidation() {
        String validEmail = "patient@email.com";
        String invalidEmail = "patientemail";
        
        assertTrue(validEmail.contains("@"));
        assertFalse(invalidEmail.contains("@"));
    }
    
    @Test
    public void testDuplicatePhoneDetection() {
        Patient p1 = new Patient("P001", "John Doe", "987-654-3210", "john@email.com");
        Datastore.store.patients.add(p1);
        
        boolean isDuplicate = Datastore.store.patients.stream()
            .anyMatch(p -> p.getPhone().equals("987-654-3210"));
        
        assertTrue(isDuplicate);
    }
    
    @Test
    public void testCrossCheckWithDoctors() {
        Doctor d1 = new Doctor("D001", "Dr. Smith", "Cardiology", "smith@hospital.com", "123-456-7890");
        Datastore.store.doctors.add(d1);
        
        boolean phoneExists = Datastore.store.doctors.stream()
            .anyMatch(d -> d.getPhone().equals("123-456-7890"));
        
        assertTrue(phoneExists);
    }
}
