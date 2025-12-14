import org.junit.*;
import static org.junit.Assert.*;

public class DoctorPanelTest {
    
    @Before
    public void setUp() {
        Datastore.store = new Datastore();
    }
    
    @Test
    public void testPhoneValidation() {
        String validPhone = "123-456-7890";
        String invalidPhone = "abc123";
        
        assertTrue(validPhone.matches("[0-9\\-\\+\\(\\)\\s]+"));
        assertFalse(invalidPhone.matches("[0-9\\-\\+\\(\\)\\s]+"));
    }
    
    @Test
    public void testEmailValidation() {
        String validEmail = "doctor@hospital.com";
        String invalidEmail = "doctoremail";
        
        assertTrue(validEmail.contains("@"));
        assertFalse(invalidEmail.contains("@"));
    }
    
    @Test
    public void testDuplicatePhoneDetection() {
        Doctor d1 = new Doctor("D001", "Dr. Smith", "Cardiology", "smith@hospital.com", "123-456-7890");
        Datastore.store.doctors.add(d1);
        
        boolean isDuplicate = Datastore.store.doctors.stream()
            .anyMatch(d -> d.getPhone().equals("123-456-7890"));
        
        assertTrue(isDuplicate);
    }
    
    @Test
    public void testDuplicateEmailDetection() {
        Doctor d1 = new Doctor("D001", "Dr. Smith", "Cardiology", "smith@hospital.com", "123-456-7890");
        Datastore.store.doctors.add(d1);
        
        boolean isDuplicate = Datastore.store.doctors.stream()
            .anyMatch(d -> d.getEmail().equals("smith@hospital.com"));
        
        assertTrue(isDuplicate);
    }
}
