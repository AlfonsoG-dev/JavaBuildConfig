# Java build tool
- Another java build tool for simple projects.
- In this occasion i want to use a configuration file.

# Installation
1. Clone the repository
```shell
git clone https://github.com/AlfonsoG-dev/JavaBuildConfig
```
2. Create the configuration file
```shell
Created-By: Author-Name
Source-Path: ./src/
Class-Path: ./bin/
Libraries: ./lib/dependency/myJar.jar;./lib/dependency2/myJar2.jar
Main-Class: App
```
3. Execute the build script
>- for now only works on windows
```shell
pwsh build.ps1
```

# Disclaimer
- This projects its for educational purposes.
- Security issues are not taken into account.
