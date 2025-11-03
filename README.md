# Java build
- A java build tool that uses configuration files to build the project.

---

# Installation
This is a step by step guide on how to install this tool.
1. Download the source code of the project from []().
2. Build the project to get a `.jar` file to use.
> You can use the `.ps1` build script.
```sh
pwsh build.ps1
```
3. Execute the jar file with the *Java* `cli` tools.
```sh
java -jar App.jar
``` 

# Usage
In this build tool you have the following commands:
1. Compile command:
- Use `--compile` to compile the project.
```sh
java -jar javaBuild.jar --compile
```
> You can use 3 sub-commands:
>- `--s sourcePath` to change the source path.
>- `--t classPath` to change the target path.
>- `--i` to include or not the lib files.

2. Run Command
- Use `--run` to execute the project.
```sh
java -jar javaBuild.jar --run
```
> You can use 4 sub-commands:
>- `src\app.java` to change the main class to execute.
>- `--s` to change the source path.
>- `--t` to change the target path.
>- `--i` to include or not the lib files.

---

# Disclaimer
- This project is for educational purposes.
- Security issues are not taken into account.
- Use it at your own risks.
