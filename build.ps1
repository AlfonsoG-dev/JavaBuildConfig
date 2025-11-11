$Source="src\application\*.java src\application\builders\*.java src\application\models\*.java src\application\operation\*.java src\application\utils\*.java"
$Compile="javac -d bin $Source"
$Jar="jar -cfm JavaBuildConfig.jar Manifesto.txt -C bin\ . "
$Run="java -cp 'bin' application.JavaBuildConfig"
Invoke-Expression ($Compile + " && " + $Jar + " && " + $Run)
