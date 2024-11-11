package Application.Operations;

import Application.Utils.CommandUtils;

import java.util.HashMap;

public class CommandOperations {
    private CommandUtils cUtils;
    private FileOperations fOperations;
    private HashMap<String, String> configValues;
    public CommandOperations(String rootPath) {
        cUtils = new CommandUtils(rootPath);
        fOperations = new FileOperations(rootPath);
        configValues = fOperations.getConfigValues();
    }

    public String compile() {
        String sourceFiles = fOperations.getProjectClassNames(configValues.get("Source-Path"));
        String c = cUtils.getCompileCommand(
                sourceFiles,
                configValues.get("Class-Path"),
                configValues.get("Libraries")
        );
        return c;
    }
    public String run() {
        String r = cUtils.getRunCommand(
                configValues.get("Class-Path"),
                configValues.get("Libraries"),
                configValues.get("Main-Class")
        );
        return r;
    }
    public String createJar() {
        String cj = cUtils.getCreateJarCommand(
                configValues.get("Class-Path"),
                configValues.get("Libraries"),
                configValues.get("Main-Class")

        );
        return cj;
    }
}
