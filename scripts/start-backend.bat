@echo off
set "ROOT_DIR=%~dp0.."
set "BLOCKING_BACKEND_DIR=%ROOT_DIR%\backend\blocking"
set "REACTIVE_BACKEND_DIR=%ROOT_DIR%\backend\reactive"
start "User Service" powershell -NoExit -Command "cd '%BLOCKING_BACKEND_DIR%'; .\gradlew runUser"
timeout /t 3 /nobreak
start "Ticket Service" powershell -NoExit -Command "cd '%REACTIVE_BACKEND_DIR%'; .\gradlew runTicket"
timeout /t 3 /nobreak
start "Event Service" powershell -NoExit -Command "cd '%BLOCKING_BACKEND_DIR%'; .\gradlew runEvent"

