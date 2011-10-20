@ECHO OFF

REM Make environment variable changes local to this batch file
SETLOCAL

REM SET CWRSYNCHOME=C:\cygwin\bin
SET CWRSYNCHOME=C:\PROGRAM FILES\CWRSYNC


REM Set CYGWIN variable to 'nontsec'. That makes sure that permissions
REM on your windows machine are not updated as a side effect of cygwin
REM operations.
SET CYGWIN=nontsec

REM Set HOME variable to your windows home directory. That makes sure 
REM that ssh command creates known_hosts in a directory you have access.
SET HOME=%HOMEDRIVE%%HOMEPATH%

REM Make cwRsync home as a part of system PATH to find required DLLs
SET CWOLDPATH=%PATH%
SET PATH=%CWRSYNCHOME%\BIN;%PATH%


REM if you want to avoid problems with file permissions on pwd.txt, do this
REM rsync  -vaz  --delete "/cygdrive/c/temp/test2" rsync://benoitx@server2.shade.ca/statsvn/ < pwd.txt

rsync --no-implied-dirs --password-file=pwd.txt -vaz "/cygdrive/c/project/statsvn/statsvn/target/docs/" rsync://benoitx@server2.shade.ca/statsvn/



