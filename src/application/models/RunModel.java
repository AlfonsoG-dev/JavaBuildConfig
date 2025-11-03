package application.models;

import application.operation.FileOperation;

public record RunModel(String root, FileOperation op) implements CommandModel {

    @Override
    public String getRoot() {
        return root;
    }
    @Override
    public FileOperation getFileOperation() {
        return op;
    }
    @Override
    public String getCommand(String targetURL, String flags, boolean includeLib) {
        return null;
    }

}
