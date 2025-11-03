package application.models;

import application.operation.FileOperation;

import java.util.List;
import java.util.stream.Collectors;

import java.nio.file.Path;
import java.nio.file.Paths;

public record CompileModel(String root, FileOperation op) {

    private String getLibURL() {
        return Paths.get(root).resolve("lib").toString();
    }

    private String prepareLibFiles() {
        StringBuilder libs = new StringBuilder();
        libs.append(op.libFiles(getLibURL())
            .stream()
            .map(Path::toString)
            .collect(Collectors.joining(";"))
        );
        String clean = libs.toString();
        if((clean.length()-1) < clean.length()) {
            clean = clean.substring(0, clean.length()-1);
        }
        return clean;
    }
    /**
     * join files path as src\directory\*.java
     */
    private String prepareSrcFiles() {
        StringBuilder src = new StringBuilder();
        src.append(op.sourceFiles()
                .stream()
                .map(Path::toString)
                .collect(Collectors.joining("*.java "))
        );
        String clean = src.toString();
        if((clean.length()-1) < clean.length()) {
            clean = clean.substring(0, clean.length()-1);
        }
        return clean;
    }

    private String getCommand(String targetURL, boolean includeLib) {
        StringBuilder command = new StringBuilder("javac -d ");
        // default bin
        command.append(targetURL);
        command.append(" ");
        if(includeLib) {
            command.append("-cp '");
            command.append(prepareLibFiles());
        }
        command.append("' ");
        command.append(prepareSrcFiles());
        return command.toString();
    }
}
