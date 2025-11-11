$Source="src\application\*.java src\application\builders\*.java src\application\models\*.java src\application\operation\*.java src\application\utils\*.java"
$Compile="javac -d bin $Source"
$Run="java -cp 'bin' application.JavaBuildConfig --h"
Invoke-Expression ($Compile + " && " + $Run)
