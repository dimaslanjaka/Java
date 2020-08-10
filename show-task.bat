@echo off
cls
./gradlew tasks --warning-mode all --stacktrace > myLogs.txt 2> logErrors.txt
pause