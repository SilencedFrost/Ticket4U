@echo off
:: =============================================================
::  HUONG DAN GHIN LEN TASKBAR (chi can lam 1 lan)
::
::  File .bat khong the ghin truc tiep len taskbar.
::  Can tao Shortcut ben NGOAI thu muc du an (tranh git track).
::
::  Cac buoc:
::  1. Chuot phai Desktop -> New -> Shortcut
::  2. "Type the location of the item", dan vao:
::        cmd /c "C:\path\thuc\te\cua\ban\Ticket4U\start-all.bat"
::     Vi du:
::        cmd /c "D:\study\Ticket4U\start-all.bat"
::  3. Dat ten shortcut: Ticket4U (hoac tuy y)
::  4. (Tuy chon) Chuot phai shortcut -> Properties
::               -> Change Icon -> chon tu shell32.dll
::               -> Change Icon -> Browse toi auto-run\logo-primary.ico
::  5. Chuot phai shortcut -> Pin to taskbar
:: =============================================================

echo Dang khoi dong toan bo he thong Ticket4U...

:: Goi file khoi dong backend (se tu mo 2 cua so PowerShell moi)
call "%~dp0auto-run\start-backend.bat"

:: Mo mot cua so CMD moi de chay frontend
start "Nuxt Frontend" cmd /k ""%~dp0auto-run\start-frontend.bat""

:: Tu dong tat cua so tong quan nay sau khi da goi xong cac service
exit
