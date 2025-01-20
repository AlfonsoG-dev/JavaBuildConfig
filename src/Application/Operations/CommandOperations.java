package Application.Operations;

import java.io.File;

import java.util.HashMap;

import Application.Utils.CommandUtils;

public class CommandOperations {
    private String rootPath;
    private CommandUtils cUtils;
    private FileOperations fOperations;
    private HashMap<String, String> configValues;
    public CommandOperations(String rootPath) {
        this.rootPath = rootPath;
        cUtils = new CommandUtils();
        fOperations = new FileOperations(rootPath);
        configValues = fOperations.getConfigValues();
    }

    public String compile() {
        String sourceFiles = fOperations.getProjectClassNames(configValues.get("Source-Path"));
        String c = cUtils.getCompileCommand(
                configValues.get("Compile-Flags"),
                sourceFiles,
                configValues.get("Class-Path"),
                configValues.get("Libraries")
        );
        return c;
    }
    public String run(String runClassPath, String commands) {
        String r = cUtils.getRunCommand(
                configValues.get("Class-Path"),
                configValues.get("Libraries"),
                configValues.get("Main-Class"),
                runClassPath,
                commands
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
    public void createConfigFile() {
        String m = fOperations.getMainClass();
        if(m == null) {
            try {
                File f = new File(rootPath);
                m = new File(f.getCanonicalPath()).getName();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        fOperations.createConfigFile("", "./src/", "/bin/", m);
    }
}
