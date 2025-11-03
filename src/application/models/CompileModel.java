package application.models;

import application.operation.FileOperation;

import java.util.List;
import java.util.stream.Collectors;

import java.io.File;

import java.nio.file.Path;
import java.nio.file.Paths;

public record CompileModel(String root, FileOperation op) {

    private String getLibURL() {
        return Paths.get(root).resolve("lib").toString();
    }

    private StringBuilder prepareLibFiles() {
        StringBuilder libs = new StringBuilder();
        libs.append(op.libFiles(getLibURL())
            .stream()
            .map(Path::toString)
            .collect(Collectors.joining(";"))
        );
        return libs;
    }
    /**
     * join files path as src\directory\*.java
     */
    private String prepareSrcFiles() {
        StringBuilder src = new StringBuilder();
        src.append(op.sourceDirs()
                .stream()
                .map(Path::toString)
                .collect(Collectors.joining(File.separator + "*.java "))
        );
        // with collectors joining the last line doesn't have the aggregation \*.java
        String clean = src.toString() + File.separator + "*.java";
        return clean;
    }

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
