package application.operation;

import application.utils.*;

public class FileOperation {
    private String root;
    private FileUtils fu;
    private ExecutorUtils ex;
    public FileOperation(String root) {
        this.root = root;
        fu = new FileUtils(root);
        ex = new ExecutorUtils();
    }

    public void printFiles(String pathURL) {
        ex.getResult(fu.callableList(pathURL, 0))
            .stream()
            .forEach(System.out::println);
    }
}
