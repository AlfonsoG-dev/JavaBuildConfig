$srcClases = ".\src\*.java .\src\Application\Operations\*.java .\src\Application\Utils\*.java .\src\Application\*.java "
$libFiles = ""
$compile = "javac -Werror -Xlint:all -d .\bin\ $srcClases"
$createJar = "jar -cfm JavaBuildConfig.jar Manifesto.txt -C .\bin\ ."
$javaCommand = "java -jar JavaBuildConfig.jar"
$runCommand = "$compile" + " && " + "$createJar" + " && " +"$javaCommand"
Invoke-Expression $runCommand 
