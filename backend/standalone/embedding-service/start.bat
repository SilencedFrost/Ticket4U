@echo off
echo Starting embed API on http://localhost:8084 ...
uvicorn main:app --host 0.0.0.0 --port 8084
pause