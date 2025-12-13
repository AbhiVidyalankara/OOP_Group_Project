@echo off
echo Compiling Medicare Console App...
cd Medicare
javac *.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)
cd ..
echo Compilation successful!
echo.
echo Starting Medicare Management System...
echo.
java Medicare.MedicareApp
pause
