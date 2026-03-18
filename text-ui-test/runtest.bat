@echo off
setlocal enableextensions

taskkill /F /IM java.exe /T 2>nul

cd ..
call gradlew clean shadowJar --no-daemon
if errorlevel 1 exit /b 1

cd build\libs
for /f "tokens=*" %%a in ('dir /b *.jar') do set "TARGET_JAR=%%a"

cd ..\..\text-ui-test

set "TEMP_OUT=RESULT_%RANDOM%.txt"

echo [RUNNING] Testing with %TARGET_JAR%...
powershell -Command "Get-Content input.txt | java -jar '..\build\libs\%TARGET_JAR%' | Out-File -FilePath '%TEMP_OUT%' -Encoding ascii"

timeout /t 2 /nobreak >nul

move /Y "%TEMP_OUT%" ACTUAL.TXT >nul 2>&1

fc /L /W ACTUAL.TXT EXPECTED.TXT > nul

if %errorlevel% EQU 0 (
    echo [PASS] Test passed!
    del ACTUAL.TXT 2>nul
    exit /b 0
) else (
    echo [FAIL] Output mismatch!
    echo --- ACTUAL ---
    type ACTUAL.TXT
    echo --- EXPECTED ---
    type EXPECTED.TXT
    exit /b 1
)