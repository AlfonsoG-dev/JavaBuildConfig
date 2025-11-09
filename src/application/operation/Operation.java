package application.operation;

import application.utils.ExecutorUtils;
import application.builders.*;

import java.util.Optional;
import java.util.HashMap;
import java.io.File;

public class Operation {
    private CompileBuilder compileBuilder;
    private RunBuilder runBuilder;
    private JarBuilder jarBuilder;
    private ScriptBuilder scriptBuilder;
    private LibBuilder libBuilder;
    private FileBuilder fileBuilder;
    private FileOperation fileOperation;
    private ExecutorUtils ex;

    private String root;
    private String sourceURl;
    private HashMap<String, String> configData;

    private String oSourceURl;
    private String oTargetURL;
    private String oMainClass;
    private String oAuthor;
    private String oCompileFlags;
    private boolean oIncludeLib;

    public Operation(String root) {
        this.root = Optional.ofNullable(root).orElse("src");
        fileOperation = new FileOperation(this.root);
        ex = new ExecutorUtils();
        compileBuilder = new CompileBuilder(this.root, fileOperation);
        runBuilder = new RunBuilder(this.root, fileOperation);
        jarBuilder = new JarBuilder(this.root, fileOperation);
        scriptBuilder = new ScriptBuilder(compileBuilder);
        libBuilder = new LibBuilder(this.root, fileOperation);
        fileBuilder = new FileBuilder(fileOperation);
    }
    public void loadConfig() {
        configData = fileOperation.getConfigValues();
        oSourceURl = Optional.ofNullable(configData.get("Source-Path")).orElse("src");
        oTargetURL = Optional.ofNullable(configData.get("Class-Path")).orElse("bin");
        oMainClass = Optional.ofNullable(configData.get("Main-Class")).orElse(fileOperation.getProjectName());
        oAuthor = Optional.ofNullable(fileOperation.getAuthor()).orElse("System-Owner");
        oCompileFlags = Optional.ofNullable(configData.get("Compile-Flags")).orElse("-Xlint:all -Xdiags:verbose");
        String dataLib = Optional.ofNullable(configData.get("Libraries")).orElse("exclude");
        oIncludeLib = dataLib.equals("include");
    }
    public void initializeENV(String sourceURl, String targetURL, String includeLib) {
        this.sourceURl = Optional.ofNullable(sourceURl).orElse(oSourceURl);
        fileOperation.populateList(this.sourceURl);
        if(targetURL != null) {
            this. oTargetURL = targetURL;
        }
        if(includeLib != null) {
            this.oIncludeLib = includeLib.equals("include");
        }
    }
    public void setConfig(String mainClass, String author) {
        mainClass = Optional.ofNullable(mainClass).orElse(oMainClass);
        author = Optional.ofNullable(author).orElse(oAuthor);
        fileBuilder.createConfig(oSourceURl, oTargetURL, mainClass, oCompileFlags, oIncludeLib);
        fileBuilder.createManifesto(author, oIncludeLib);
    }
    public void executeCompileCommand(String compileFlags, String target) {
        String flags = Optional.ofNullable(compileFlags).orElse(oCompileFlags);
        target = Optional.ofNullable(target).orElse(oTargetURL);
        File f = new File(oTargetURL);
        String command = "";
        if(f.exists()) {
            command = compileBuilder.reCompileCommand(oTargetURL, target, flags,oIncludeLib);
        } else {
            command = compileBuilder.getCommand(oTargetURL, flags, oIncludeLib);
        }
        ex.executeCommand(command);
    }
    public void executeScratchCompile(String flags) {
        flags = Optional.ofNullable(flags).orElse(oCompileFlags);
        String command = "rm -r " + oTargetURL + " && " + compileBuilder.getCommand(oTargetURL, flags, oIncludeLib);
        ex.executeCommand(command);
    }
    public void executeRunCommand(String flags, String mainClass) {
        String command = "";
        if(mainClass == null) {
            command = runBuilder.getCommand(oTargetURL, flags, oIncludeLib);
        } else {
            command = runBuilder.getCommand(mainClass, oTargetURL, flags, oIncludeLib);
        }
        ex.executeCommand(command);
    }
    public void executeJarCommand(String fileName, String flags, String mainClass) {
        flags = Optional.ofNullable(flags).orElse("v");
        fileName = Optional.ofNullable(fileName).orElse(fileOperation.getProjectName());
        String command = "";
        if(!new File(fileName + ".jar").exists()) {
            command = jarBuilder.getCommand(fileName, oTargetURL, oMainClass, flags, oIncludeLib);
        } else {
            command = jarBuilder.getUpdateJarCommand(fileName, oTargetURL, flags);
        }
        ex.executeCommand(command);
    }
    public void extractDependencies(String targetURI, String flags) {
        targetURI = Optional.ofNullable(targetURI).orElse("extractionFiles");
        flags = Optional.ofNullable(flags).orElse("v");
        String c = libBuilder.getCommand(targetURI, flags, oIncludeLib);
        ex.executeCommand(c);
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
    public void copyToPath(String sourceURI, String destinationURI) {
        destinationURI = Optional.ofNullable(destinationURI).orElse("lib");
        fileOperation.copyToPath(sourceURI, destinationURI);
    }
}
