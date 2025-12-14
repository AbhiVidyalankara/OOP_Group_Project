import java.sql.*;

public class TestConnection {
    public static void main(String[] args) {
        System.out.println("Testing MySQL connection to WAMP server...");
        
        try {
            DatabaseConnection.initDatabase();
            
            try (Connection conn = DatabaseConnection.getConnection()) {
                System.out.println("✓ Database connection successful!");
                
                // Test if tables exist
                DatabaseMetaData meta = conn.getMetaData();
                ResultSet rs = meta.getTables(null, null, "doctors", null);
                if (rs.next()) {
                    System.out.println("✓ Tables created successfully!");
                } else {
                    System.out.println("⚠ Tables not found, but connection works");
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Connection failed: " + e.getMessage());
            System.err.println("\nTroubleshooting steps:");
            System.err.println("1. Make sure WAMP server is running (green icon)");
            System.err.println("2. Check if MySQL service is started in WAMP");
            System.err.println("3. Verify MySQL is running on port 3306");
        }
    }
}