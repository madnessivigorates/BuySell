@echo off
chcp 65001

REM Включаем режим отладки
setlocal enabledelayedexpansion

REM Путь к текущей папке
set PROJECT_DIR=%~dp0
cd /d %PROJECT_DIR%

REM Проверка Docker
echo [INFO] Проверка Docker...
docker -v
if errorlevel 1 (
    echo [ERROR] Docker не найден. Убедитесь, что Docker установлен и работает.
    timeout /t 30
    exit /b 1
)

REM Проверка Docker Compose
echo [INFO] Проверка Docker Compose...
docker-compose -v
if errorlevel 1 (
    echo [ERROR] Docker Compose не найден. Убедитесь, что Docker Compose установлен.
    timeout /t 30
    exit /b 1
)

REM Сборка и запуск контейнеров
echo [INFO] Сборка и запуск контейнеров с помощью Docker Compose...
docker-compose down
docker-compose up --build -d
if errorlevel 1 (
    echo [ERROR] Ошибка при сборке и запуске контейнеров с Docker Compose.
    timeout /t 30
    exit /b 1
)

echo [SUCCESS] Приложение успешно запущено через Docker Compose!
timeout /t 30


