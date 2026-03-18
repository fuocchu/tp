@echo off
setlocal enableextensions

taskkill /F /IM java.exe /T 2>nul

cd ..
call gradlew clean shadowJar
if errorlevel 1 (
    echo Gradle build failed!
    exit /b 1
)


timeout /t 5 /nobreak >nul

cd build\libs
set "TARGET_JAR="
for /f "tokens=*" %%a in ('dir /b *.jar') do set "TARGET_JAR=%%a"


cd ..\..\text-ui-test
if exist ACTUAL.TXT del /f /q ACTUAL.TXT

echo Running Java...
type input.txt | java -jar "..\build\libs\%TARGET_JAR%" > ACTUAL.TXT 2>&1

timeout /t 3 /nobreak >nul

if not exist ACTUAL.TXT (
    echo [ERROR] ACTUAL.TXT was never created.
    exit /b 1
)

fc ACTUAL.TXT EXPECTED.TXT > nul
if errorlevel 1 (
    echo [FAIL] Output does not match expected!
    type ACTUAL.TXT
    exit /b 1
) else (
    echo [PASS] Test passed!
    exit /b 0
)