package application.builders;

import application.models.CommandModel;
import application.operation.FileOperation;

import java.util.List;
import java.util.Set;
import java.io.File;
import java.nio.file.Path;

public record CompileBuilder(String root, FileOperation op) implements CommandModel {
    @Override
    public FileOperation getFileOperation() {
        return op;
    }
    @Override
    public String getCommand(String targetURI, String flags, boolean includeLib) {
        StringBuilder command = new StringBuilder("javac -d ");
        // default bin
        command.append(targetURI);
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
    public String reCompileCommand(String targetURI, String sourceURI, String flags, boolean includeLib) {
        StringBuilder command = new StringBuilder("javac -d ");
        command.append(targetURI);
        command.append(" ");
        if(flags.isEmpty()) {
            flags = "-Werror";
        }
        command.append(flags);
        command.append(" -cp '");
        command.append(sourceURI);
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
        Set<String> imports = null;
        for(Path p: paths) {
            command.append(p.toString());
            // files that depend on this one
            String packageName = p.toString().replace(root + File.separator, "").replace(".java", "").replace(File.separator, ".");
            imports = op.getDependencies(packageName, p.getFileName().toString());
            for(String n: imports) {
                command.append(" ");
                command.append(n);
            }

        }
        return command.toString();
    }
}
