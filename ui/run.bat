@echo off
echo Compiling Medicare GUI App...
javac MedicareGUI.java DoctorPanel.java PatientPanel.java AppointmentPanel.java ReportPanel.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)
echo Compilation successful!
echo.
echo Starting Medicare GUI...
start javaw MedicareGUI
