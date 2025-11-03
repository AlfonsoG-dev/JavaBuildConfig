package application.models;

import application.operation.FileOperation;

import java.util.List;
import java.util.stream.Collectors;

import java.io.File;

import java.nio.file.Path;
import java.nio.file.Paths;

public record CompileModel(String root, FileOperation op) implements CommandModel {
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
        StringBuilder command = new StringBuilder("javac -d ");
        // default bin
        command.append(targetURL);
        command.append(" ");
        if(flags.isEmpty()) {
            flags = "-Werror";
        }
        command.append(flags);
        command.append(" ");
        if(includeLib && !prepareLibFiles().isEmpty()) {
            command.append("-cp '");
            command.append(prepareLibFiles());
            command.append("' ");
        }
        command.append(prepareSrcFiles());
        return command.toString();
    }
}
