package service;

import model.Appointment;
import storage.FileStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentService {
    private static List<Appointment> appointments = new ArrayList<>();
    private static int nextId = 1;

    static {
        appointments = FileStorage.loadAppointments();
        if (!appointments.isEmpty()) {
            nextId = appointments.stream().mapToInt(Appointment::getId).max().orElse(0) + 1;
        }
    }

    public static void scheduleAppointment(int patientId, int doctorId, LocalDateTime dateTime, String status) {
        Appointment appointment = new Appointment(nextId++, patientId, doctorId, dateTime, status);
        appointments.add(appointment);
        FileStorage.saveAppointments(appointments);

        // Send notifications
        model.Patient patient = PatientService.getPatientById(patientId);
        model.Doctor doctor = DoctorService.getDoctorById(doctorId);

        if (patient != null) {
            NotificationService.addNotification("PATIENT", patient.getName(),
                    "Your appointment with Dr. " + doctor.getName() + " is scheduled for " +
                            dateTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }

        if (doctor != null) {
            NotificationService.addNotification("DOCTOR", doctor.getName(),
                    "New appointment scheduled with " + patient.getName() + " on " +
                            dateTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
    }

    public static List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    public static void updateAppointmentStatus(int appointmentId, String newStatus) {
        for (Appointment appointment : appointments) {
            if (appointment.getId() == appointmentId) {
                String oldStatus = appointment.getStatus();
                appointment.setStatus(newStatus);
                FileStorage.saveAppointments(appointments);

                // Send notification about status change
                model.Patient patient = PatientService.getPatientById(appointment.getPatientId());
                model.Doctor doctor = DoctorService.getDoctorById(appointment.getDoctorId());

                String message = "Appointment #" + appointmentId + " status changed from " + oldStatus + " to " + newStatus;

                if (patient != null) {
                    NotificationService.addNotification("PATIENT", patient.getName(), message);
                }

                if (doctor != null) {
                    NotificationService.addNotification("DOCTOR", doctor.getName(), message);
                }
                break;
            }
        }
    }

    public static boolean hasAppointmentsForPatient(int patientId) {
        for (Appointment appointment : appointments) {
            if (appointment.getPatientId() == patientId) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasAppointmentsForDoctor(int doctorId) {
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorId() == doctorId) {
                return true;
            }
        }
        return false;
    }
}