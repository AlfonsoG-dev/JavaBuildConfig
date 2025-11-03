package application.operation;

import application.utils.ExecutorUtils;
import application.models.*;
import application.builders.*;

public class Operation {
    private CompileModel compileModel;
    private RunModel runModel;
    private FileOperation fileOperation;
    private ExecutorUtils ex;
    private ScriptBuilder scriptBuilder;

    private String root;
    private String sourceURl;
    private String targetURL;
    private boolean includeLib;

    public Operation(String root) {
        fileOperation = new FileOperation(root);
        ex = new ExecutorUtils();
        compileModel = new CompileModel(root, fileOperation);
        runModel = new RunModel(root, fileOperation);
        scriptBuilder = new ScriptBuilder(compileModel);
    }
    public void initializeENV(String sourceURl, String targetURL, boolean includeLib) {
        this.sourceURl = sourceURl;
        this.targetURL = targetURL;
        this.includeLib = includeLib;
        fileOperation.populateList(sourceURl);
    }
    public void executeCompileCommand(String flags) {
        String command = compileModel.getCommand(targetURL, flags, includeLib);
        ex.executeCommand(command);
    }
    public void executeRunCommand(String flags) {
        String command = runModel.getCommand(targetURL, flags, includeLib);
        ex.executeCommand(command);
    }
    public void createBuildScript(String fileURL) {
        String osName = System.getProperty("os.name").toLowerCase()
        if(osName.contains("windows")) {
            fileURL = fileURL + ".ps1";
        } else if(osName.contains("linux")) {
            fileURL = fileURL + ".sh";
        }
        String lines = scriptBuilder.getScript(targetURL, includeLib);
        fileOperation.createFile(fileURL, lines);
    }
}
