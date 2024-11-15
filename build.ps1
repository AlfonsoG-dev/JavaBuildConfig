$classes=".\src\*.java .\src\Application\*.java .\src\Application\Operations\*.java .\src\Application\Utils\*.java "
$libs=""
$compile="javac -d .\bin\ $classes"
$run="java -cp '.\bin\;' 'JavaBuildConfig' --compile"
$invoke=$compile + " && " + $run
Invoke-Expression $invoke