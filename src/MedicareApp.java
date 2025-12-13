package Medicare;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Scanner;

public class MedicareApp {
    private static DoctorService doctorService = new DoctorService();
    private static PatientService patientService = new PatientService();
    private static AssignmentService assignmentService = new AssignmentService();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        Datastore.store.load();
        doctorService.loadDoctors(Datastore.store.doctors);
        patientService.loadPatients(Datastore.store.patients);
        
        while (true) {
            System.out.println("\n=== MEDICARE SYSTEM ===");
            System.out.println("1. Doctor Management");
            System.out.println("2. Patient Management");
            System.out.println("3. Appointment Scheduling");
            System.out.println("4. Assign Doctor to Patient");
            System.out.println("5. Generate Monthly Report");
            System.out.println("6. View Notifications");
            System.out.println("7. Exit");
            System.out.print("Choose: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1: doctorMenu(); break;
                case 2: patientMenu(); break;
                case 3: appointmentMenu(); break;
                case 4: assignDoctorMenu(); break;
                case 5: generateReport(); break;
                case 6: viewNotifications(); break;
                case 7:
                    Datastore.store.save();
                    System.out.println("Goodbye!");
                    return;
            }
        }
    }
    
    private static void doctorMenu() {
        System.out.println("\n1. Add Doctor\n2. Update Doctor\n3. Delete Doctor\n4. List Doctors");
        System.out.print("Choose: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice == 1) {
            System.out.print("ID: ");
            String id = scanner.nextLine();
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Specialty: ");
            String specialty = scanner.nextLine();
            doctorService.addDoctor(new Doctor(id, name, specialty));
        } else if (choice == 2) {
            System.out.print("Doctor ID: ");
            String id = scanner.nextLine();
            System.out.print("New Name: ");
            String name = scanner.nextLine();
            System.out.print("New Specialty: ");
            String specialty = scanner.nextLine();
            doctorService.updateDoctor(id, name, specialty);
        } else if (choice == 3) {
            System.out.print("Doctor ID: ");
            String id = scanner.nextLine();
            doctorService.deleteDoctor(id);
        } else if (choice == 4) {
            doctorService.listDoctors();
        }
    }
    
    private static void patientMenu() {
        System.out.println("\n1. Add Patient\n2. Update Patient\n3. Delete Patient\n4. List Patients");
        System.out.print("Choose: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice == 1) {
            System.out.print("ID: ");
            String id = scanner.nextLine();
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Phone: ");
            String phone = scanner.nextLine();
            patientService.addPatient(new Patient(id, name, phone));
        } else if (choice == 2) {
            System.out.print("Patient ID: ");
            String id = scanner.nextLine();
            System.out.print("New Name: ");
            String name = scanner.nextLine();
            System.out.print("New Phone: ");
            String phone = scanner.nextLine();
            patientService.updatePatient(id, name, phone);
        } else if (choice == 3) {
            System.out.print("Patient ID: ");
            String id = scanner.nextLine();
            patientService.deletePatient(id);
        } else if (choice == 4) {
            patientService.listPatients();
        }
    }
    
    private static void appointmentMenu() {
        System.out.println("\n1. Schedule Appointment\n2. Update Status\n3. List Appointments");
        System.out.print("Choose: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice == 1) {
            System.out.print("Appointment ID: ");
            String id = scanner.nextLine();
            System.out.print("Doctor ID: ");
            String doctorId = scanner.nextLine();
            System.out.print("Patient ID: ");
            String patientId = scanner.nextLine();
            System.out.print("Date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());
            System.out.print("Time (HH:MM): ");
            LocalTime time = LocalTime.parse(scanner.nextLine());
            
            if (Scheduler.isAvailable(doctorId, date, time)) {
                Datastore.store.appointments.add(new Appointment(id, doctorId, patientId, date, time, "Scheduled"));
                NotificationManager.sendToDoctor(doctorId, "New appointment with Patient " + patientId + " on " + date);
                NotificationManager.sendToPatient(patientId, "Appointment confirmed with Doctor " + doctorId + " on " + date);
                System.out.println("Appointment scheduled! Notifications sent.");
            } else {
                System.out.println("Time slot not available!");
            }
        } else if (choice == 2) {
            System.out.print("Appointment ID: ");
            String id = scanner.nextLine();
            System.out.print("New Status (Scheduled/Completed/Canceled/Delayed): ");
            String status = scanner.nextLine();
            for (Appointment a : Datastore.store.appointments) {
                if (a.id.equals(id)) {
                    a.setStatus(status);
                    NotificationManager.sendToDoctor(a.doctorId, "Appointment " + id + " status: " + status);
                    NotificationManager.sendToPatient(a.patientId, "Your appointment status: " + status);
                    System.out.println("Status updated! Notifications sent.");
                    return;
                }
            }
            System.out.println("Appointment not found!");
        } else if (choice == 3) {
            for (Appointment a : Datastore.store.appointments) {
                System.out.println(a.id + " - Dr:" + a.doctorId + " Pt:" + a.patientId + " " + a.date + " " + a.time + " [" + a.status + "]");
            }
        }
    }
    
    private static void assignDoctorMenu() {
        System.out.println("\n1. Manual Assignment\n2. Auto Assignment");
        System.out.print("Choose: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        if (choice == 1) {
            System.out.print("Patient ID: ");
            String patientId = scanner.nextLine();
            System.out.print("Doctor ID: ");
            String doctorId = scanner.nextLine();
            assignmentService.assignDoctor(patientId, doctorId);
            NotificationManager.sendToDoctor(doctorId, "New patient assigned: " + patientId);
        } else if (choice == 2) {
            System.out.print("Patient ID: ");
            String patientId = scanner.nextLine();
            System.out.print("Required Specialty: ");
            String specialty = scanner.nextLine();
            System.out.print("Urgency (High/Normal/Low): ");
            String urgency = scanner.nextLine();
            String doctorId = assignmentService.autoAssignDoctor(specialty, urgency);
            if (doctorId != null) {
                assignmentService.assignDoctor(patientId, doctorId);
                NotificationManager.sendToDoctor(doctorId, "Auto-assigned patient: " + patientId);
                System.out.println("Doctor " + doctorId + " auto-assigned!");
            } else {
                System.out.println("No available doctor for specialty: " + specialty);
            }
        }
    }
    
    private static void generateReport() {
        System.out.print("Year-Month (YYYY-MM): ");
        YearMonth month = YearMonth.parse(scanner.nextLine());
        System.out.println(MonthlyReport.generateReport(month));
    }
    
    private static void viewNotifications() {
        System.out.println("\n1. Doctor Notifications\n2. Patient Notifications");
        System.out.print("Choose: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        
        if (choice == 1) {
            System.out.println("\n--- Doctor Notifications for " + id + " ---");
            for (Notification n : NotificationManager.getDoctorNotifications(id)) {
                System.out.println(n);
            }
        } else if (choice == 2) {
            System.out.println("\n--- Patient Notifications for " + id + " ---");
            for (Notification n : NotificationManager.getPatientNotifications(id)) {
                System.out.println(n);
            }
        }
    }
}
