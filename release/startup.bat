@echo off
set "Path=.\bin;.\jre_64\bin;%Path%;"
java -Dfelix.config.properties=file:Configuration/config.properties -jar iDesktop.jar