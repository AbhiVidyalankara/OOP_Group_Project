@echo off
echo Compiling Medicare Console App...
javac -d . Datastore.java "Doctor Management\Doctor.java" "Patient Management\Patient.java" "Appoinment Shedule\Appointment.java" "Doctor Management\DoctorService.java" "Patient Management\PatientService.java" "Appoinment Shedule\Scheduler.java" "Assign Doctor to Patient\DoctorAssignment.java" "Assign Doctor to Patient\AssignmentService.java" "Monthly Report Generate\MonthlyReport.java" MedicareApp.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)
echo Compilation successful!
echo.
echo Starting Medicare Management System...
echo.
java Medicare.MedicareApp
pause
