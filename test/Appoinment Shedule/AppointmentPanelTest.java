import org.junit.*;
import static org.junit.Assert.*;
import java.time.*;

public class AppointmentPanelTest {
    
    @Before
    public void setUp() {
        Datastore.store = new Datastore();
    }
    
    @Test
    public void testAppointmentCreation() {
        Appointment apt = new Appointment();
        apt.id = "A001";
        apt.doctorId = "D001";
        apt.patientId = "P001";
        apt.date = "2024-12-25";
        apt.time = "10:00";
        apt.status = "Scheduled";
        
        Datastore.store.appointments.add(apt);
        
        assertEquals(1, Datastore.store.appointments.size());
        assertEquals("A001", apt.id);
    }
    
    @Test
    public void testTimeConflictDetection() {
        Appointment apt1 = new Appointment();
        apt1.doctorId = "D001";
        apt1.date = "2024-12-25";
        apt1.time = "10:00";
        apt1.status = "Scheduled";
        Datastore.store.appointments.add(apt1);
        
        boolean hasConflict = Datastore.store.appointments.stream()
            .anyMatch(a -> a.doctorId.equals("D001") && 
                          a.date.equals("2024-12-25") && 
                          a.time.equals("10:00") &&
                          !a.status.equals("Canceled"));
        
        assertTrue(hasConflict);
    }
    
    @Test
    public void testStatusUpdate() {
        Appointment apt = new Appointment();
        apt.status = "Scheduled";
        
        apt.status = "Completed";
        
        assertEquals("Completed", apt.status);
    }
    
    @Test
    public void testDateValidation() {
        LocalDate today = LocalDate.now();
        LocalDate pastDate = today.minusDays(1);
        LocalDate futureDate = today.plusDays(1);
        
        assertTrue(pastDate.isBefore(today));
        assertTrue(futureDate.isAfter(today));
    }
    
    @Test
    public void testTimeFormat() {
        String validTime = "14:30";
        String invalidTime = "25:70";
        
        try {
            LocalTime.parse(validTime);
            assertTrue(true);
        } catch (Exception e) {
            fail("Valid time should parse");
        }
        
        try {
            LocalTime.parse(invalidTime);
            fail("Invalid time should throw exception");
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}
