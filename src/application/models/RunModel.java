package application.models;

import application.operation.FileOperation;

import java.io.File;

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
        if(!new File(targetURL).exists()) return null;
        StringBuilder command = new StringBuilder("java -cp '");
        String mainClassName = op.getMainClass();

        command.append(targetURL);
        if(includeLib && !prepareLibFiles().isEmpty()) {
            command.append(";");
            command.append(prepareLibFiles());
        }
        command.append("' ");
        if(mainClassName != "") command.append(mainClassName);

        return command.toString();
    }

}
