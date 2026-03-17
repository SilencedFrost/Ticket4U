@echo off
echo Dang khoi dong Nuxt frontend...
set "ROOT_DIR=%~dp0.."
cd /d "%ROOT_DIR%\frontend"
pnpm run dev
pause