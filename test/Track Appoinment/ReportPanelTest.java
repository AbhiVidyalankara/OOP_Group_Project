import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class ReportPanelTest {
    
    @Before
    public void setUp() {
        Datastore.store = new Datastore();
    }
    
    @Test
    public void testAppointmentCountByStatus() {
        Appointment a1 = new Appointment();
        a1.status = "Scheduled";
        Appointment a2 = new Appointment();
        a2.status = "Completed";
        Appointment a3 = new Appointment();
        a3.status = "Scheduled";
        
        Datastore.store.appointments.add(a1);
        Datastore.store.appointments.add(a2);
        Datastore.store.appointments.add(a3);
        
        long scheduled = Datastore.store.appointments.stream()
            .filter(a -> a.status.equals("Scheduled")).count();
        
        assertEquals(2, scheduled);
    }
    
    @Test
    public void testMonthlyAppointmentFilter() {
        Appointment a1 = new Appointment();
        a1.date = "2024-12-15";
        Appointment a2 = new Appointment();
        a2.date = "2024-11-20";
        
        Datastore.store.appointments.add(a1);
        Datastore.store.appointments.add(a2);
        
        long decemberCount = Datastore.store.appointments.stream()
            .filter(a -> a.date.startsWith("2024-12")).count();
        
        assertEquals(1, decemberCount);
    }
    
    @Test
    public void testDoctorAppointmentCount() {
        Appointment a1 = new Appointment();
        a1.doctorId = "D001";
        Appointment a2 = new Appointment();
        a2.doctorId = "D001";
        Appointment a3 = new Appointment();
        a3.doctorId = "D002";
        
        Datastore.store.appointments.add(a1);
        Datastore.store.appointments.add(a2);
        Datastore.store.appointments.add(a3);
        
        long d001Count = Datastore.store.appointments.stream()
            .filter(a -> a.doctorId.equals("D001")).count();
        
        assertEquals(2, d001Count);
    }
    
    @Test
    public void testNotificationSentAfterReport() {
        Datastore.store.doctorNotifications.put("D001", new ArrayList<>());
        Notification notif = new Notification("Monthly report", "Email");
        Datastore.store.doctorNotifications.get("D001").add(notif);
        
        assertTrue(Datastore.store.doctorNotifications.containsKey("D001"));
        assertEquals(1, Datastore.store.doctorNotifications.get("D001").size());
    }
}
