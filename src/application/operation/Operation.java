package application.operation;

import application.utils.ExecutorUtils;
import application.builders.*;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.io.File;

public class Operation {
    private static final String DEFAULT_CONFIG_LIB = "ignore";


    private CompileBuilder compileBuilder;
    private RunBuilder runBuilder;
    private FileBuilder fileBuilder;
    private FileOperation fileOperation;
    private ExecutorUtils executorUtils;

    private String root;

    private String oSourceURI;
    private String oTargetURI;
    private String oMainClass;
    private String oAuthor;
    private String oCompileFlags;
    private String oTestClass;
    private String oIncludeLib;
    private ExecutorService executorThread;

    public Operation(String root) {
        this.root = Optional.ofNullable(root).orElse("src");
        executorUtils = new ExecutorUtils();
        fileOperation = new FileOperation(this.root, executorUtils);
        compileBuilder = new CompileBuilder(this.root, fileOperation);
        runBuilder = new RunBuilder(this.root, fileOperation);
        fileBuilder = new FileBuilder(fileOperation);
        executorThread = Executors.newCachedThreadPool();
    }
    public void loadConfig() {
        Map<String, String> configData = fileOperation.getConfigValues();
        oSourceURI = Optional.ofNullable(configData.get("Source-Path")).orElse("src");
        oTargetURI = Optional.ofNullable(configData.get("Class-Path")).orElse("bin");
        oMainClass = Optional.ofNullable(configData.get("Main-Class")).orElse(fileOperation.getProjectName());
        oAuthor = Optional.ofNullable(fileOperation.getAuthor()).orElse("System-Owner");
        oCompileFlags = Optional.ofNullable(configData.get("Compile-Flags")).orElse("-Xlint:all -Xdiags:verbose");
        oTestClass = Optional.ofNullable(configData.get("Test-Class")).orElse("test.TestLauncher");
        oIncludeLib = Optional.ofNullable(configData.get("Libraries")).orElse(DEFAULT_CONFIG_LIB);
    }
    public void initializeENV(String sourceURI, String targetURI, String includeLib) {
        if(targetURI != null) {
            oTargetURI = targetURI;
        }
        if(includeLib != null) {
            oIncludeLib = Optional.ofNullable(includeLib).orElse(DEFAULT_CONFIG_LIB);
        }
        fileOperation.appendSource(Optional.ofNullable(sourceURI).orElse(oSourceURI));
        if(!oIncludeLib.equals(DEFAULT_CONFIG_LIB)) {
            fileOperation.appendLib("lib");
        }
        fileOperation.appendLists();
        fileOperation.populateList(executorUtils.getListsResult(executorThread), oIncludeLib);
    }
    public void setConfig(String mainClass, String author) {
        mainClass = Optional.ofNullable(mainClass).orElse(oMainClass);
        author = Optional.ofNullable(author).orElse(oAuthor);
        fileBuilder.createConfig(oSourceURI, oTargetURI, mainClass, oCompileFlags, oIncludeLib);
        // include means include in the build process, otherwise append in the manifesto.
        fileBuilder.createManifesto(author, oIncludeLib);
    }
    public void appendCompileProcess(String compileFlags, String target) {
        String flags = Optional.ofNullable(compileFlags).orElse(oCompileFlags);
        target = Optional.ofNullable(target).orElse(oTargetURI);
        File f = new File(oTargetURI);
        String command = "";
        if(f.exists()) {
            command = compileBuilder.reCompileCommand(oTargetURI, target, flags,oIncludeLib);
        } else {
            command = compileBuilder.getCommand(oTargetURI, flags, oIncludeLib);
        }
        executorUtils.appendCommandToCallableProcess(command);
    }
    public void appendScratchCompileProcess(String flags) {
        flags = Optional.ofNullable(flags).orElse(oCompileFlags);
        // delete class path
        executorUtils.appendCommandToCallableProcess("rm -r " + oTargetURI);
        // compile project
        executorUtils.appendCommandToCallableProcess(
                compileBuilder.getCommand(oTargetURI, flags, oIncludeLib)
        );
    }
    public void appendRunProcess(String flags, String mainClass) {
        String command = "";
        flags = Optional.ofNullable(flags).orElse("");
        if(mainClass == null) {
            command = runBuilder.getCommand(oTargetURI, flags, oIncludeLib);
        } else {
            command = runBuilder.getCommand(mainClass, oTargetURI, flags, oIncludeLib);
        }
        executorUtils.appendCommandToCallableProcess(command);
    }
    public void appendTestProcess(String mainClass) {
        mainClass = Optional.ofNullable(mainClass).orElse(oTestClass);
        String command = "";
        if(mainClass == null) {
            command = runBuilder.getCommand(oTargetURI, "", oIncludeLib);
        } else {
            command = runBuilder.getCommand(mainClass, oTargetURI, "", oIncludeLib);
        }
        executorUtils.appendCommandToCallableProcess(command);
    }
    public void appendJarProcess(String fileName, String flags) {
        flags = Optional.ofNullable(flags).orElse("");
        fileName = Optional.ofNullable(fileName).orElse(fileOperation.getProjectName());
        JarBuilder jarBuilder = new JarBuilder(root, fileOperation);
        String command = "";
        if(!new File(fileName + ".jar").exists()) {
            command = jarBuilder.getCommand(fileName, oTargetURI, oMainClass, flags, oIncludeLib);
        } else {
            command = jarBuilder.getUpdateJarCommand(fileName, oTargetURI, fileOperation.getMainClass(), flags, oIncludeLib);
        }
        executorUtils.appendCommandToCallableProcess(command);
    }
    public void appendExtractDependenciesProcess(String targetURI) {
        targetURI = Optional.ofNullable(targetURI).orElse("extractionFiles");
        // the process is capable of executing all the concatenated processes with '&&'
        new LibBuilder(root, fileOperation).appendCommandToProcess(executorUtils, targetURI, oIncludeLib);
    }
    public void createBuildScript(String fileURI) {
        String osName = System.getProperty("os.name").toLowerCase();
        fileURI = Optional.ofNullable(fileURI).orElse("build");
        if(osName.contains("windows")) {
            fileURI = fileURI + ".ps1";
        } else if(osName.contains("linux")) {
            fileURI = fileURI + ".sh";
        }
        String lines = new ScriptBuilder(compileBuilder).getScript(oTargetURI, oIncludeLib);
        fileOperation.createFile(fileURI, lines);
    }
    public void copyToPath(String sourceURI, String destinationURI) {
        destinationURI = Optional.ofNullable(destinationURI).orElse("lib");
        // get the source .jar file name
        File sourceFile = new File(sourceURI);
        File destinationFile = new File(destinationURI).toPath().resolve(sourceFile.getName().replace(".jar", "")).toFile();
        if(!destinationFile.exists()) destinationFile.mkdir();
        fileOperation.copyToPath(sourceURI, destinationFile.toString());
    }
    public void executeCommand() {
        executorUtils.executeCallableProcess(executorThread);
    }
    public void terminateProgram() {
        executorThread.shutdown();
        try {
            if(!executorThread.awaitTermination(15, TimeUnit.SECONDS)) {
                executorThread.shutdownNow();
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            executorUtils.cleanPendingProcess();
        }
    }
}
