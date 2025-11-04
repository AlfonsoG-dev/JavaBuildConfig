package application.models;

import application.models.CommandModel;
import application.operation.FileOperation;

import java.io.File;

public record RunBuilder(String root, FileOperation op) implements CommandModel {

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
        if(flags.isEmpty()) flags = "-Xmx1g";

        command.append(flags);
        command.append(" ");
        if(mainClassName != "") command.append(mainClassName);

        return command.toString();
    }
    private String prepareClassName(String mainClass) {
        return mainClass.replace(root + File.separator, "").replace(File.separator, ".").replace(".java", "");
    }
    public String getCommand(String mainClass, String targetURL, String flags, boolean includeLib) {
        if(!new File(targetURL).exists()) return null;
        StringBuilder command = new StringBuilder("java -cp '");

        command.append(targetURL);
        if(includeLib && !prepareLibFiles().isEmpty()) {
            command.append(";");
            command.append(prepareLibFiles());
        }
        command.append("' ");
        if(flags.isEmpty()) flags = "-Xmx1g";

        command.append(flags);
        command.append(" ");
        command.append(prepareClassName(mainClass));

        return command.toString();
    }

}
