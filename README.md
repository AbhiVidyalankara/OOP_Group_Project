# Medicare Management System

A comprehensive healthcare management system built with Java, featuring both console and GUI interfaces.

## Features

- **Doctor Management** - Add, update, delete, and view doctors
- **Patient Management** - Manage patient records
- **Appointment Scheduling** - Schedule and track appointments
- **Doctor Assignment** - Assign doctors to patients
- **Monthly Reports** - Generate statistical reports
- **Notifications** - Doctor and patient notification systems

## Project Structure

```
├── src/                          # Console application
│   ├── MedicareApp.java         # Main console entry point
│   ├── Datastore.java           # Data persistence
│   ├── Doctor Management/
│   ├── Patient Management/
│   ├── Appoinment Shedule/
│   ├── Assign Doctor to Patient/
│   ├── Monthly Report Generate/
│   ├── Doctor Notification/
│   ├── Patient Notification/
│   └── Track Appoinment/
│
└── ui/                           # GUI application
    ├── MedicareGUI.java         # Main GUI window
    ├── DoctorPanel.java
    ├── PatientPanel.java
    ├── AppointmentPanel.java
    └── ReportPanel.java
```

## How to Run

### Console Version
```bash
cd src/Medicare
javac *.java
cd ..
java Medicare.MedicareApp
```

Or simply run: `src/run.bat`

### GUI Version
```bash
cd ui
javac MedicareGUI.java DoctorPanel.java PatientPanel.java AppointmentPanel.java ReportPanel.java
java MedicareGUI
```

## Requirements

- Java 8 or higher
- No external dependencies required

## License

Educational Project
