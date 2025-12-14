import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notification implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String message;
    private LocalDateTime timestamp;
    private String deliveryMethod;
    
    public Notification(String id, String message) {
        this.id = id;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.deliveryMethod = "System";
    }
    
    public Notification(String id, String message, String deliveryMethod) {
        this.id = id;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.deliveryMethod = deliveryMethod;
    }
    
    public String getId() { return id; }
    public String getMessage() { return message; }
    public String getDeliveryMethod() { return deliveryMethod; }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "[" + timestamp.format(formatter) + "] [" + deliveryMethod + "] " + message;
    }
}
