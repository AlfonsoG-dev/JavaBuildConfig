package application.operation;

import application.utils.ExecutorUtils;
import application.models.*;
import application.builders.*;

import java.util.Optional;

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

    public Operation(String root) {
        this.root = Optional.ofNullable(root).orElse("src");
        fileOperation = new FileOperation(this.root);
        ex = new ExecutorUtils();
        compileBuilder = new CompileBuilder(this.root, fileOperation);
        runBuilder = new RunBuilder(this.root, fileOperation);
        jarBuilder = new JarBuilder(this.root, fileOperation);
        scriptBuilder = new ScriptBuilder(compileBuilder);
    }
    public void initializeENV(String sourceURl, String targetURL, boolean includeLib) {
        this.sourceURl = Optional.ofNullable(sourceURl).orElse("src");
        this.targetURL = Optional.ofNullable(targetURL).orElse("bin");
        this.includeLib = Optional.ofNullable(includeLib).orElse(false);
        fileOperation.populateList(this.sourceURl);
    }
    public void executeCompileCommand(String compileFlags) {
        String flags = Optional.ofNullable(compileFlags).orElse("-Werror -Xlint:all -Xdiags:verbose");
        String command = compileBuilder.getCommand(targetURL, flags, includeLib);
        ex.executeCommand(command);
    }
    public void executeRunCommand(String flags, String mainClass) {
        String command = "";
        if(mainClass == null) {
            command = runBuilder.getCommand(targetURL, flags, includeLib);
        } else {
            command = runBuilder.getCommand(mainClass, targetURL, flags, includeLib);
        }
        ex.executeCommand(command);
    }
    public void executeJarCommand(String fileName, String flags, String mainClass) {
        mainClass = Optional.ofNullable(mainClass).orElse(fileOperation.getMainClass());
        flags = Optional.ofNullable(flags).orElse("v");
        fileName = Optional.ofNullable(fileName).orElse(fileOperation.getProjectName());
        String command = jarBuilder.getCommand(fileName, targetURL, mainClass, flags, includeLib);
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
        String lines = scriptBuilder.getScript(targetURL, includeLib);
        fileOperation.createFile(fileURL, lines);
    }
}
