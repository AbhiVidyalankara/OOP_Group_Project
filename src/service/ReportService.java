package service;

import model.Appointment;
import model.Doctor;
import model.Patient;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class ReportService {

    public static String generateMonthlyReport() {
        StringBuilder report = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDayOfMonth = now.with(TemporalAdjusters.firstDayOfMonth());

        report.append("MediCare Plus - Monthly Report\n");
        report.append("Period: ").append(firstDayOfMonth.format(java.time.format.DateTimeFormatter.ofPattern("MMMM yyyy"))).append("\n");
        report.append("Generated: ").append(LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
        report.append("=========================================\n\n");

        // Total counts
        List<Patient> patients = PatientService.getAllPatients();
        List<Doctor> doctors = DoctorService.getAllDoctors();
        List<Appointment> appointments = AppointmentService.getAllAppointments();

        report.append("OVERALL STATISTICS:\n");
        report.append("Total Patients: ").append(patients.size()).append("\n");
        report.append("Total Doctors: ").append(doctors.size()).append("\n");
        report.append("Total Appointments: ").append(appointments.size()).append("\n\n");

        // Appointment status breakdown
        report.append("APPOINTMENT STATUS BREAKDOWN:\n");
        int scheduled = 0, completed = 0, canceled = 0, delayed = 0;

        for (Appointment a : appointments) {
            switch (a.getStatus()) {
                case "Scheduled": scheduled++; break;
                case "Completed": completed++; break;
                case "Canceled": canceled++; break;
                case "Delayed": delayed++; break;
            }
        }

        report.append("  Scheduled: ").append(scheduled).append("\n");
        report.append("  Completed: ").append(completed).append("\n");
        report.append("  Canceled: ").append(canceled).append("\n");
        report.append("  Delayed: ").append(delayed).append("\n\n");

        // Doctor workload
        report.append("DOCTOR WORKLOAD:\n");
        for (Doctor doctor : doctors) {
            int count = 0;
            for (Appointment a : appointments) {
                if (a.getDoctorId() == doctor.getId()) {
                    count++;
                }
            }
            report.append("  ").append(doctor.getName()).append(" (").append(doctor.getSpecialty()).append("): ")
                    .append(count).append(" appointments\n");
        }

        return report.toString();
    }
}