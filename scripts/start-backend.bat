@echo off
set "ROOT_DIR=%~dp0.."
set "BACKEND_DIR=%ROOT_DIR%\backend\blocking"
start "User Service" powershell -NoExit -Command "cd '%BACKEND_DIR%'; .\gradlew runUser"
timeout /t 3 /nobreak
start "Ticket Service" powershell -NoExit -Command "cd '%BACKEND_DIR%'; .\gradlew runTicket"
timeout /t 3 /nobreak
start "Event Service" powershell -NoExit -Command "cd '%BACKEND_DIR%'; .\gradlew runEvent"

