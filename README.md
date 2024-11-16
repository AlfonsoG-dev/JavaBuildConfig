# Java build tool
- Another java build tool for simple projects.
- In this occasion i want to use a configuration file.

----

# Installation
1. Clone the repository
```cmd
git clone https://github.com/AlfonsoG-dev/JavaBuildConfig
```
2. Create the configuration file
```txt
Created-By: Author-Name
Source-Path: ./src/
Class-Path: ./bin/
Libraries: ./lib/dependency/myJar.jar;./lib/dependency2/myJar2.jar
Main-Class: App
```
3. Execute the build script
>- for now only works on windows
```cmd
pwsh build.ps1
```

# Instructions
> Using the CLI commands you can:

1. compile
```shell
javabuild --compile
```
2. run
```shell
javabuild --run
```
> for now only executes the main class for future versions it have access to execution of another commands
3. create **JAR** file
```shell
javabuild --jar
```
4. build project
```shell
javabuild --build
```
> Which executes **compile** & create **JAR** commands
5. create build script
```shell
javabuild -cb
```
> for now only powershell **ps1** script are allow.
6. create the project folder structure
```shell
javabuild -cs
```
> Which will create the structure:
>- **./bin/**
>- **./docs/**
>- **./lib/**
>- **./src/**
7. Create the manifesto file
```shell
javabuild -cm
```
> The configuration file must be created before.

7. Help
```shell
javabuild --h
or 
javabuild --help
```
---

# Disclaimer
- This projects its for educational purposes.
- Security issues are not taken into account.
