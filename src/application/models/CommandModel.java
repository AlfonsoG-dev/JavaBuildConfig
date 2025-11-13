package application.models;

import application.operation.FileOperation;

import java.util.stream.Collectors;

import java.io.File;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface CommandModel {

    public FileOperation getFileOperation();
    public String getCommand(String targetURI, String flags, boolean includeLib);

    public default String getLibURI() {
        return Paths.get("." + File.separator).resolve("lib").toString();
    }

    public default StringBuilder prepareLibFiles() {
        StringBuilder libs = new StringBuilder();
        libs.append(getFileOperation().libFiles(getLibURI())
            .stream()
            .map(Path::toString)
            .collect(Collectors.joining(";"))
        );
        return libs;
    }
    /**
     * join files path as src\directory\*.java
     */
    public default String prepareSrcFiles() {
        StringBuilder src = new StringBuilder();
        src.append(getFileOperation().sourceDirs()
                .stream()
                .map(Path::toString)
                .collect(Collectors.joining(File.separator + "*.java "))
        );
        // with collectors joining the last line doesn't have the aggregation \*.java
        String clean = src.toString() + File.separator + "*.java";
        return clean;
    }
}
