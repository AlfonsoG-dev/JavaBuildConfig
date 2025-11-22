$srcClasses = "src\application\*.java src\application\builders\*.java src\application\models\*.java src\application\operation\*.java src\application\utils\*.java "
$libFiles = ""
$compile = "javac --release 23 -Xlint:all -Xdiags:verbose -d .\bin\ $srcClasses"
$createJar = "jar -cfm JavaBuildConfig.jar Manifesto.txt -C .\bin\ ."
$javaCommand = "java -jar JavaBuildConfig.jar"
$runCommand = "$compile" + " && " + "$createJar" + " && " +"$javaCommand"
Invoke-Expression $runCommand 
