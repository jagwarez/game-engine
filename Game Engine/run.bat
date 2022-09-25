@echo off
cd /D "%~dp0"

if /i "%1"=="trace" goto trace
if /i "%1"=="retrace" goto retrace
    
java -cp ".\build\classes;.\libs\lwjgl\*" TestGame

goto exit

:trace

apitrace trace --output=./trace/game.trace --api=gl java -cp ".\build\classes;.\libs\lwjgl\*" TestGame

goto exit

:retrace

qapitrace ./trace/game.trace

:exit