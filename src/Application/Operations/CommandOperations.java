package Application.Operations;

import java.io.File;

import java.util.HashMap;

import Application.Utils.CommandUtils;

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
    public void createBuildScript() {
        fOperations.createBuildScript(
                configValues.get("Class-Path"),
                configValues.get("Source-Path"),
                configValues.get("Libraries"),
                configValues.get("Main-Class")
        );
    }
    public void createProjectStructure() {
        String[] files = {
            "." + File.separator + "bin" + File.separator,
            "." + File.separator + "docs" + File.separator,
            "." + File.separator + "lib" + File.separator,
            "." + File.separator + "src" + File.separator,
        };
        fOperations.createProjectStructure(files);
    }
    public void createManifesto(boolean includeLibs) {
        fOperations.createManifesto(
                configValues.get("Created-By"),
                configValues.get("Main-Class"),
                includeLibs
        );
    }
}
