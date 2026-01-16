@echo off
cls
echo ========================================
echo   Portfolio Builder - Starting...
echo ========================================
echo.

REM Set JAVA_HOME
set "JAVA_HOME=C:\Program Files\Java\jdk-23"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo [1/3] Checking port 8080...
powershell -Command "Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue | ForEach-Object { Stop-Process -Id $_.OwningProcess -Force }" >nul 2>&1
echo      Port 8080 is free!
echo.

echo [2/3] Compiling application...
call mvnw.cmd clean compile -DskipTests -q
if %ERRORLEVEL% NEQ 0 (
    echo      Compilation failed!
    pause
    exit /b 1
)
echo      Compilation successful!
echo.

echo [3/3] Starting application...
echo.
echo ========================================
echo   Application is starting...
echo   Wait for: "Started PortfolioApplication"
echo   Then open: http://localhost:8080
echo ========================================
echo.

call mvnw.cmd spring-boot:run

pause
