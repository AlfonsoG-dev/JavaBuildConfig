$srcClases = "src\application\*.java src\application\utils\*.java "
$libFiles = ""
$compile = "javac --release 23 -Werror -Xlint:all -d .\bin\ $srcClases"
$createJar = "jar -cfm JavaBuildConfig.jar Manifesto.txt -C .\bin\ ."
$javaCommand = "java -jar JavaBuildConfig.jar"
$runCommand = "$compile" + " && " + "$createJar" + " && " +"$javaCommand"
Invoke-Expression $runCommand 
