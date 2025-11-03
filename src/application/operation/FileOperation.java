package application.operation;

import application.utils.FileUtils;

public class FileOperation {
    private String root;
    private FileUtils fu;
    public FileOperation(String root) {
        this.root = root;
        fu = new FileUtils(root);
    }
}
