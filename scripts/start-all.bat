@echo off
:: =============================================================
::  HOW TO PIN TO TASKBAR (only needs to be done once)
::
::  A .bat file cannot be pinned directly to the taskbar.
::  You need to create a Shortcut OUTSIDE the project folder
::  (to avoid git tracking).
::
::  Steps:
::  1. Right-click Desktop -> New -> Shortcut
::  2. In "Type the location of the item", paste:
::        cmd /c "C:\actual\path\on\your\machine\Ticket4U\start-all.bat"
::     Example:
::        cmd /c "D:\study\Ticket4U\start-all.bat"
::  3. Name the shortcut: Ticket4U (or whatever you prefer)
::  4. (Optional) Right-click shortcut -> Properties
::               -> Change Icon -> pick from shell32.dll
::               -> Change Icon -> Browse to auto-run\logo-primary.ico 
::  5. Right-click shortcut -> Pin to taskbar
:: =============================================================

echo Starting the entire Ticket4U system...

:: Use %~dp0 to reference files relative to THIS script's location
call %~dp0start-backend.bat

start %~dp0start-frontend.bat

:: Automatically close this master window after all services have been launched
exit