@echo off
echo Starting Medicare App with MySQL driver...

cd src
set CP=.;..\lib\mysql-connector-j-8.0.33.jar;"Doctor Management";"Patient Management";"Appoinment Shedule";"Notifications";"Track Appoinment";"Assign Doctor to Patient";"Monthly Report Generate"

echo Compiling...
javac -cp "%CP%" *.java "Doctor Management\*.java" "Patient Management\*.java" "Appoinment Shedule\*.java" "Notifications\*.java" "Track Appoinment\*.java" "Assign Doctor to Patient\*.java" "Monthly Report Generate\*.java"

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Running Medicare App...
java -cp "%CP%" MedicareApp

pause