@echo off

chcp 65001

REM Укажите путь к вашему jar файлу
set JAR_PATH=%SCRIPT_DIR%target\BuySell-0.0.1.jar

REM Проверяем, существует ли JAR файл
if exist "%JAR_PATH%" (
    echo JAR файл найден: %JAR_PATH%
    REM Запускаем JAR файл
    java -jar "%JAR_PATH%"
) else (
    echo Ошибка: JAR файл не найден по пути %JAR_PATH%
)

REM Запускаем приложение
echo Запуск приложения...
java -jar "%JAR_PATH%"

REM Проверяем, что приложение запустилось без ошибок
if %ERRORLEVEL% EQU 0 (
    echo Приложение успешно запущено!
) else (
    echo Произошла ошибка при запуске приложения.
)

pause
