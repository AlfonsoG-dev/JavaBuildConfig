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
        return ex.getResult(fu.callableList(sourceURL, 0))
            .stream()
            .filter(p -> p.toFile().isFile())
            .toList();
    }
}
