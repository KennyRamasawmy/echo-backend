@echo off
setlocal

set MAVEN_HOME=%USERPROFILE%\.m2\wrapper\dists\apache-maven-3.9.6
if not exist "%MAVEN_HOME%" (
    echo Downloading Maven...
    mkdir "%MAVEN_HOME%"
    powershell -Command "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip' -OutFile '%TEMP%\maven.zip'; Expand-Archive -Path '%TEMP%\maven.zip' -DestinationPath '%MAVEN_HOME%\..' -Force"
)

"%MAVEN_HOME%\bin\mvn.cmd" %*
