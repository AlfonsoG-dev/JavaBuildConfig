package Application.Operations;

import Application.Utils.CommandUtils;

import java.util.HashMap;

public class CommandOperations {
    private CommandUtils cUtils;
    private FileOperations fOperations;
    public CommandOperations(String rootPath) {
        cUtils = new CommandUtils();
        fOperations = new FileOperations(rootPath);
    }

    public String compile() {
        HashMap<String, String> configValues = fOperations.getConfigValues();
        String sourceFiles = fOperations.getProjectClassNames(configValues.get("Source-Path"));
        String c = cUtils.getCompileCommand(
                sourceFiles,
                configValues.get("Class-Path"),
                configValues.get("Libraries")
        );
        return c;
    }
}
