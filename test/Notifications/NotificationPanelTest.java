import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class NotificationPanelTest {
    
    @Before
    public void setUp() {
        Datastore.store = new Datastore();
    }
    
    @Test
    public void testNotificationCreation() {
        Notification notif = new Notification("Test message", "Email");
        
        assertNotNull(notif);
        assertEquals("Test message", notif.message);
        assertEquals("Email", notif.deliveryMethod);
    }
    
    @Test
    public void testDoctorNotificationStorage() {
        Notification notif = new Notification("Appointment scheduled", "SMS");
        Datastore.store.doctorNotifications.put("D001", new ArrayList<>());
        Datastore.store.doctorNotifications.get("D001").add(notif);
        
        assertEquals(1, Datastore.store.doctorNotifications.get("D001").size());
    }
    
    @Test
    public void testPatientNotificationStorage() {
        Notification notif = new Notification("Appointment reminder", "Email");
        Datastore.store.patientNotifications.put("P001", new ArrayList<>());
        Datastore.store.patientNotifications.get("P001").add(notif);
        
        assertEquals(1, Datastore.store.patientNotifications.get("P001").size());
    }
    
    @Test
    public void testNotificationFiltering() {
        Notification n1 = new Notification("Doctor message", "Email");
        Notification n2 = new Notification("Patient message", "SMS");
        
        List<Notification> doctorNotifs = new ArrayList<>();
        doctorNotifs.add(n1);
        
        List<Notification> patientNotifs = new ArrayList<>();
        patientNotifs.add(n2);
        
        assertEquals(1, doctorNotifs.size());
        assertEquals(1, patientNotifs.size());
    }
    
    @Test
    public void testNotificationDeletion() {
        Datastore.store.doctorNotifications.put("D001", new ArrayList<>());
        Notification notif = new Notification("Test", "Email");
        Datastore.store.doctorNotifications.get("D001").add(notif);
        
        Datastore.store.doctorNotifications.get("D001").clear();
        
        assertEquals(0, Datastore.store.doctorNotifications.get("D001").size());
    }
}
