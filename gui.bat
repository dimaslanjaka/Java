@echo off
setlocal enabledelayedexpansion
set "LOG_MODE=n"

:MENU
cls
echo --- MAIN MENU ---
echo 1 - assemble
echo 2 - build
echo 3 - debug
echo 4 - install debug
echo 0 - enable log to file
echo 00 - disable log to file
if defined LOG_MODE if "%LOG_MODE%"=="y" (
  echo Log mode: true
) else (
  echo Log mode: false
)
echo Q - quit


set "SELECT="
set /P "SELECT=Type the options above <Enter>: "
if "%SELECT%"=="1" GOTO :assemble
if "%SELECT%"=="2" GOTO :build
if "%SELECT%"=="3" GOTO :debug
if "%SELECT%"=="4" GOTO :install-debug
if "%SELECT%"=="0" GOTO :enable-log
if "%SELECT%"=="00" GOTO :disable-log
if /I "%SELECT%"=="Q" GOTO :EOF
goto :MENU

:enable-log
set "LOG_MODE=y"
goto :MENU

:disable-log
set "LOG_MODE=n"
goto :MENU

:assemble
if defined LOG_MODE if "%LOG_MODE%"=="y" (
  gradlew assemble --warning-mode all --stacktrace > myLogs.txt 2> logErrors.txt
) else (
  gradlew assemble --warning-mode all --stacktrace
)
pause
goto :MENU

:build
if defined LOG_MODE if "%LOG_MODE%"=="y" (
  gradlew build --warning-mode all --stacktrace > myLogs.txt 2> logErrors.txt
) else (
  gradlew build --warning-mode all --stacktrace
)
pause
goto :MENU

:debug
if defined LOG_MODE if "%LOG_MODE%"=="y" (
  gradlew assembleDebug --warning-mode all --stacktrace > myLogs.txt 2> logErrors.txt
) else (
  gradlew assembleDebug --warning-mode all --stacktrace
)
pause
goto :MENU

:install-debug
if defined LOG_MODE if "%LOG_MODE%"=="y" (
  gradlew installDebug --warning-mode all --stacktrace > myLogs.txt 2> logErrors.txt
) else (
  gradlew installDebug --warning-mode all --stacktrace
)
pause
goto :MENU