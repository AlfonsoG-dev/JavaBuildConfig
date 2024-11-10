$build="javabuild --build"
$runJar="java -jar JavaBuildConfig.jar"
$runCommand=$build + " && " + $runJar

Invoke-Expression $runCommand
