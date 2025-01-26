@echo off

chcp 65001

REM Укажите путь к вашему jar файлу
set JAR_PATH=C:\Users\magic\IdeaProjects\BuySell\target\BuySell-0.0.1.jar

REM Проверяем, существует ли файл
if not exist "%JAR_PATH%" (
    echo Файл %JAR_PATH% не найден!
    exit /b 1
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
