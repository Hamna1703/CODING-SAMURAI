@echo off
echo ================================
echo   Library Management System
echo ================================
echo.

REM Check if JDBC driver exists
if not exist "lib\mysql-connector-java*.jar" (
    echo ERROR: MySQL JDBC driver not found in lib folder!
    echo.
    echo Please download MySQL Connector/J from:
    echo https://dev.mysql.com/downloads/connector/j/
    echo.
    echo Extract and copy mysql-connector-java-X.X.XX.jar to the lib\ folder
    echo.
    pause
    exit /b 1
)

REM Check if MySQL is running
echo Checking MySQL connection...
mysql -u root -p -e "SELECT 1;" > nul 2>&1
if errorlevel 1 (
    echo WARNING: Cannot connect to MySQL. Make sure MySQL server is running.
    echo.
)

REM Compile Java files
echo Compiling Java files...
if not exist "bin" mkdir bin
javac -cp "lib\*" -d bin src\*.java
if errorlevel 1 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!
echo.
echo Starting Library Management System...
echo.

REM Run the application
java -cp "bin;lib\*" App

pause