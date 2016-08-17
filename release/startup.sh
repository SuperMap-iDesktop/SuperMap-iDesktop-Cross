@echo off
export LD_LIBRARY_PATH=./bin:$LD_LIBRARY_PATH
export JRE_PATH=./jre/bin
export PATH=$LD_LIBRARY_PATH:$JRE_PATH:$PATH
java -Dfelix.config.properties=file:Configuration/config.properties -splash:./Resources/Frame/iDesktop_Cross_Startup.gif -jar iDesktop.jar