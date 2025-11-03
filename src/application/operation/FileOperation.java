package application.operation;

import application.utils.*;

import java.nio.file.Path;

import java.util.List;

public class FileOperation {
    private String root;
    private FileUtils fu;
    private ExecutorUtils ex;
    public FileOperation(String root) {
        this.root = root;
        fu = new FileUtils(root);
        ex = new ExecutorUtils();
    }
    public List<Path> sourceFiles(String sourceURL) {
        List<Path> files = ex.getResult(fu.callableList(sourceURL, 0))
            .stream()
            .filter(p -> p.toFile().isFile())
            .toList();
        return files;
    }
    public List<Path> sourceDirs(String sourceURL) {
        List<Path> dirs = ex.getResult(fu.callableList(sourceURL, 0))
            .stream()
            .filter(p -> p.toFile().isDirectory())
            .toList();
        return dirs;

    }
}
