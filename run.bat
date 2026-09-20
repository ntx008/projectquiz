@echo off
chcp 65001 > nul
echo ==================================================================
echo   Starting Android Phone OOP Simulator (100%% Pure Java)
echo ==================================================================

set "JDK_PATH=C:\Program Files\Android\Android Studio\jbr\bin"

if exist "%JDK_PATH%\java.exe" (
    set "JAVA_CMD=%JDK_PATH%\java.exe"
    set "JAVAC_CMD=%JDK_PATH%\javac.exe"
) else (
    set "JAVA_CMD=java"
    set "JAVAC_CMD=javac"
)

if not exist out mkdir out

echo Compiling Java classes...
dir /s /b src\*.java > sources.txt
"%JAVAC_CMD%" -encoding UTF-8 -d out @sources.txt
del sources.txt

if %ERRORLEVEL% equ 0 (
    echo Compilation successful!
    echo Starting server on http://localhost:3000 ...
    "%JAVA_CMD%" -cp out com.quiz.oop.Main
) else (
    echo Compilation failed!
    pause
)
