@echo off
set "ROOT_DIR=%~dp0.."
set "BACKEND_DIR=%ROOT_DIR%\backend\blocking"

start "Event Service" powershell -NoExit -Command "cd '%BACKEND_DIR%'; .\gradlew runEvent"
timeout /t 3 /nobreak
start "User Service" powershell -NoExit -Command "cd '%BACKEND_DIR%'; .\gradlew runUser"