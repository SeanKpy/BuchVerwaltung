@echo off
set DIR="%~dp0"
set JAVA_EXEC="%DIR:"=%\java"
pushd %DIR% & %JAVA_EXEC%  -p "%~dp0/../app" -m BuchVerwalung.main/ch.kofmel.HauptFenster  %* & popd
