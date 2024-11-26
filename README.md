# Java build tool
- Another java build tool for simple projects.
- In this occasion i want to use a configuration file.

# References
- [java tools and commands](https://docs.oracle.com/javase/10/tools/tools-and-command-reference.htm)

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
>- For now only works on windows
```cmd
pwsh build.ps1
```

# Instructions
> Using the CLI commands you can:

1. Compile
```shell
javabuild --compile
```
2. Run
```shell
javabuild --run
```
> It can execute other **.java** files
```shell
javabuild --run ./src/Testing/Test.java
```
3. Create **JAR** file
```shell
javabuild --jar
```
> Which creates the project **JAR** file
4. Build project
```shell
javabuild --build
```
> Which executes **compile** the project & creates the **JAR** file.
5. Create build script
```shell
javabuild -cb
```
> For now only powershell **ps1** scripts are allowed.
6. Create the project folder structure
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

# TODO
- [x] add more options to the compile: 1) -Xlint:all, 2) -g, 3) -Werror, 4) --target java-version(17)

---

# Disclaimer
- This projects its for educational purposes.
- Security issues are not taken into account.
