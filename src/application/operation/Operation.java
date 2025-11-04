package application.operation;

import application.utils.ExecutorUtils;
import application.models.*;
import application.builders.*;

import java.util.Optional;
import java.util.HashMap;

public class Operation {
    private CompileBuilder compileBuilder;
    private RunBuilder runBuilder;
    private JarBuilder jarBuilder;
    private FileOperation fileOperation;
    private ExecutorUtils ex;
    private ScriptBuilder scriptBuilder;

    private String root;
    private String sourceURl;
    private String targetURL;
    private boolean includeLib;
    private HashMap<String, String> configData;

    private String oRoot;
    private String oSourceURl;
    private String oTargetURL;
    private String oMainClass;
    private boolean oIncludeLib;

    public Operation(String root) {
        this.root = Optional.ofNullable(root).orElse("src");
        fileOperation = new FileOperation(this.root);
        ex = new ExecutorUtils();
        compileBuilder = new CompileBuilder(this.root, fileOperation);
        runBuilder = new RunBuilder(this.root, fileOperation);
        jarBuilder = new JarBuilder(this.root, fileOperation);
        scriptBuilder = new ScriptBuilder(compileBuilder);
    }
    public void loadConfig() {
        configData = fileOperation.getConfigValues();
        oRoot = Optional.ofNullable(configData.get("Root-Path")).orElse("src");
        oSourceURl = Optional.ofNullable(configData.get("Source-Path")).orElse("src");
        oTargetURL = Optional.ofNullable(configData.get("Class-Path")).orElse("bin");
        oMainClass = Optional.ofNullable(configData.get("Main-Class")).orElse(fileOperation.getProjectName());
        String dataLib = Optional.ofNullable(configData.get("Libraries")).orElse("exclude");
        oIncludeLib = dataLib.equals("include");
    }
    public void initializeENV(String sourceURl, String targetURL, String includeLib) {
        this.sourceURl = Optional.ofNullable(sourceURl).orElse(oSourceURl);
        this.targetURL = Optional.ofNullable(targetURL).orElse(oTargetURL);
        String dataLib = Optional.ofNullable(includeLib).orElse("exclude");
        this.includeLib = dataLib.equals("include");
        fileOperation.populateList(this.sourceURl);
    }
    public void executeCompileCommand(String compileFlags) {
        String flags = Optional.ofNullable(compileFlags).orElse("-Werror -Xlint:all -Xdiags:verbose");
        String command = compileBuilder.getCommand(oTargetURL, flags, oIncludeLib);
        ex.executeCommand(command);
    }
    public void executeRunCommand(String flags, String mainClass) {
        String command = "";
        if(mainClass == null) {
            command = runBuilder.getCommand(oTargetURL, flags, oIncludeLib);
        } else {
            command = runBuilder.getCommand(oMainClass, oTargetURL, flags, oIncludeLib);
        }
        ex.executeCommand(command);
    }
    public void executeJarCommand(String fileName, String flags, String mainClass) {
        flags = Optional.ofNullable(flags).orElse("v");
        fileName = Optional.ofNullable(fileName).orElse(fileOperation.getProjectName());
        String command = jarBuilder.getCommand(fileName, oTargetURL, oMainClass, flags, oIncludeLib);
        ex.executeCommand(command);
    }
    public void createBuildScript(String fileURL) {
        String osName = System.getProperty("os.name").toLowerCase();
        fileURL = Optional.ofNullable(fileURL).orElse("build");
        if(osName.contains("windows")) {
            fileURL = fileURL + ".ps1";
        } else if(osName.contains("linux")) {
            fileURL = fileURL + ".sh";
        }
        String lines = scriptBuilder.getScript(oTargetURL, oIncludeLib);
        fileOperation.createFile(fileURL, lines);
    }
}
