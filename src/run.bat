@echo off
echo ========================================
echo Medicare Management System - Compiler
echo ========================================
echo.

echo Cleaning old class files...
del /S /Q *.class 2>nul

echo.
echo Compiling all Java files...
echo.

REM Set classpath to include all subdirectories and MySQL connector
set CP=.;"Doctor Management";"Patient Management";"Appoinment Shedule";"Notifications";"Track Appoinment";"Assign Doctor to Patient";"Monthly Report Generate";..\lib\*

REM Compile all Java files recursively
javac -encoding UTF-8 -cp "%CP%" "Doctor Management\Doctor.java" "Doctor Management\DoctorService.java" "Doctor Management\DoctorPanel.java"
javac -encoding UTF-8 -cp "%CP%" "Patient Management\Patient.java" "Patient Management\PatientService.java" "Patient Management\PatientPanel.java"
javac -encoding UTF-8 -cp "%CP%" "Appoinment Shedule\Appointment.java" "Appoinment Shedule\Scheduler.java"
javac -encoding UTF-8 -cp "%CP%" "Assign Doctor to Patient\DoctorAssignment.java" "Assign Doctor to Patient\AssignmentService.java"
javac -encoding UTF-8 -cp "%CP%" "Monthly Report Generate\MonthlyReport.java"
javac -encoding UTF-8 -cp "%CP%" "Notifications\Notification.java" "Notifications\NotificationManager.java" "Notifications\NotificationService.java"
javac -encoding UTF-8 -cp "%CP%" "Track Appoinment\Trackappointment.java" "Track Appoinment\ReportPanel.java"
javac -encoding UTF-8 -cp "%CP%" Datastore.java
javac -encoding UTF-8 -cp "%CP%" "Appoinment Shedule\AppointmentPanel.java"
javac -encoding UTF-8 -cp "%CP%" "Notifications\NotificationPanel.java"
javac -encoding UTF-8 -cp "%CP%" MedicareApp.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ========================================
    echo ERROR: Compilation failed!
    echo ========================================
    pause
    exit /b 1
)

echo.
echo ========================================
echo Compilation successful!
echo Starting Medicare Management System...
echo ========================================
echo.

java -cp "%CP%" MedicareApp

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Data saved to MySQL database successfully!
)

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ========================================
    echo ERROR: Application failed to start!
    echo ========================================
    pause
    exit /b 1
)

pause
