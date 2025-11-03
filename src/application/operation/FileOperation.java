package application.operation;

import application.utils.*;

import java.io.File;

import java.nio.file.Path;

import java.util.List;

public class FileOperation {
    private String root;
    private FileUtils fu;
    private ExecutorUtils ex;
    private List<Path> listFiles;
    public FileOperation(String root) {
        this.root = root;
        fu = new FileUtils(root);
        ex = new ExecutorUtils();
    }
    public void populateList(String sourceURL) {
        listFiles = ex.getResult(fu.callableList(sourceURL, 0));
    }
    public List<Path> sourceFiles() {
        List<Path> files = listFiles
            .stream()
            .filter(p -> p.toFile().isFile())
            .toList();
        return files;
    }
    public String getMainClass() {
        String sourceRoot = root + File.separator;
        File f = new File(root);
        if(f.listFiles() != null) {
            outter:for(Path p: sourceFiles()) {
                File mf = p.toFile();
                if(mf.isFile() && mf.getName() != "TestLauncher.java") {
                    String[] lines = TextUtils.getFileLines(mf.getPath()).split("\n");
                    for(String l: lines) {
                        if(l.contains("public static void main")) {
                            return mf.getPath().replace(sourceRoot, "").replace(".java", "").replace(File.separator, ".");
                        }
                    }
                }
            }
        }
        return "";
    }
    public List<Path> sourceDirs() {
        List<Path> dirs = listFiles
            .stream()
            .filter(p -> p.toFile().isDirectory())
            .filter(p -> fu.countFiles(p) > 0)
            .toList();
        return dirs;

    }
    public List<Path> libFiles(String sourceURL) {
        return ex.getResult(fu.callableList(sourceURL, 2))
            .stream()
            .filter(p -> p.toFile().isFile() && p.toFile().getName().contains(".jar"))
            .toList();
    }
}
