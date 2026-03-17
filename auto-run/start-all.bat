@echo off
echo Dang khoi dong toan bo he thong Ticket4U...

:: Goi file khoi dong backend (se tu mo 2 cua so PowerShell moi)
call "%~dp0start-backend.bat"

:: Mo mot cua so CMD moi de chay frontend
start "Nuxt Frontend" cmd /k ""%~dp0start-frontend.bat""

:: Tu dong tat cua so tong quan nay sau khi da goi xong cac service
exit