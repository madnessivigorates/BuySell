@echo off
chcp 65001
echo Запуск сборки проекта с помощью Maven...

:: Выполним clean и package
mvn clean package

:: Проверим, успешна ли сборка
IF %ERRORLEVEL% EQU 0 (
    echo Сборка прошла успешно!
    echo Готовый JAR файл находится в директории target\
) ELSE (
    echo Произошла ошибка при сборке проекта.
)
pause
