package application.builders;

import application.models.CommandModel;
import application.operation.FileOperation;

import java.util.List;

import java.nio.file.Path;

public record CompileBuilder(String root, FileOperation op) implements CommandModel {
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
    public String reCompileCommand(String targetURL, String flags, boolean includeLib) {
        StringBuilder command = new StringBuilder("javac -d ");
        command.append(targetURL);
        command.append(" ");
        if(flags.isEmpty()) {
            flags = "-Werror";
        }
        command.append(flags);
        command.append(" -cp '");
        command.append(targetURL);
        if(includeLib) {
            command.append(";");
            command.append(prepareLibFiles());
        }
        command.append("' ");

        // change path to directory.App
        List<Path> paths = op.sourceFiles().stream()
            .filter(p -> op.diferDate(p))
            .toList();
        if(paths.size() == 0) {
            return null;
        }
        for(Path p: paths) {
            command.append(p.toString());
        }
        System.out.println("Hellow there");
        return command.toString();
    }
}
