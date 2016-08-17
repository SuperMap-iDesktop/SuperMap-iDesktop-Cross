@echo off
set "Path=.\bin;.\jre\bin;%Path%;"
java -Dfelix.config.properties=file:Configuration/config.properties -splash:./Resources/Frame/iDesktop_Cross_Startup.gif -jar iDesktop.jar