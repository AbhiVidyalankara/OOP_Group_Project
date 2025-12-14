import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/medicare_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&createDatabaseIfNotExist=true";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("MySQL JDBC Driver (legacy) loaded successfully");
            } catch (ClassNotFoundException e2) {
                System.err.println("MySQL JDBC Driver not found. Add mysql-connector-j-8.0.33.jar to project classpath");
            }
        }
    }
    
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL database successfully");
            return conn;
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            System.err.println("Make sure WAMP server is running and MySQL service is started");
            throw e;
        }
    }
    
    public static void initDatabase() {
        try {
            createDatabaseIfNotExists();
            try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS doctors (id VARCHAR(10) PRIMARY KEY, name VARCHAR(100), specialty VARCHAR(100), email VARCHAR(100), phone VARCHAR(20), status VARCHAR(20))");
                stmt.execute("CREATE TABLE IF NOT EXISTS patients (id VARCHAR(10) PRIMARY KEY, name VARCHAR(100), phone VARCHAR(20), email VARCHAR(100), situation TEXT)");
                stmt.execute("CREATE TABLE IF NOT EXISTS appointments (id VARCHAR(10) PRIMARY KEY, doctorId VARCHAR(10), patientId VARCHAR(10), date VARCHAR(20), time VARCHAR(10), status VARCHAR(20))");
                stmt.execute("CREATE TABLE IF NOT EXISTS notifications (id INT AUTO_INCREMENT PRIMARY KEY, recipientId VARCHAR(10), type VARCHAR(20), message TEXT, deliveryMethod VARCHAR(20), timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
                stmt.execute("CREATE TABLE IF NOT EXISTS doctor_schedules (doctorId VARCHAR(10), dayOfWeek VARCHAR(10), startTime TIME, endTime TIME, PRIMARY KEY(doctorId, dayOfWeek), FOREIGN KEY(doctorId) REFERENCES doctors(id) ON DELETE CASCADE)");
                System.out.println("Database initialized successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Database init error: " + e.getMessage());
            System.err.println("Make sure WAMP server is running and MySQL service is started.");
        }
    }
    
    private static void createDatabaseIfNotExists() throws SQLException {
        String baseUrl = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        try (Connection conn = DriverManager.getConnection(baseUrl, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE DATABASE IF NOT EXISTS medicare_db");
        }
    }
}
