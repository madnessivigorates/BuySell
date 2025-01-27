@echo off
chcp 65001
REM Параметры подключения MySQL
SET MYSQL_USER=root
SET MYSQL_PASS=5093
SET MYSQL_HOST=localhost
SET MYSQL_PORT=3306
SET MYSQL_DB=buysell

REM Путь к вашему MySQL клиенту
SET MYSQL_CMD="C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"

REM Путь к вашему SQL файлу
SET SQL_FILE=create_schema.sql

REM Выполнение SQL файла
%MYSQL_CMD% -h %MYSQL_HOST% -P %MYSQL_PORT% -u %MYSQL_USER% -p%MYSQL_PASS% < %SQL_FILE%

IF %ERRORLEVEL% NEQ 0 (
    echo Ошибка при создании схемы базы данных.
    pause
    exit /b %ERRORLEVEL%
)

echo Схема 'buysell' успешно создана!
pause
