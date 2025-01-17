classes="./src/*.java ./src/Application/*.java ./src/Application/Operations/*.java ./src/Application/Utils/*.java "
libs=""
javac -d ./bin/ $classes
jar -cmf Manifesto.txt App.jar -C ./bin/ .
java -jar App.jar --h

