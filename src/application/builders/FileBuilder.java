package application.builders;

import application.operation.FileOperation;

import java.io.File;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileBuilder {
    private FileOperation fileOperation;

    public FileBuilder(FileOperation fileOperation) {
        this.fileOperation = fileOperation;
    }
    public void createManifesto(String author, boolean includeLib) {
        StringBuilder lines = new StringBuilder("Created-By: ");
        if(!author.isBlank()) lines.append(author);
        lines.append("\n");
        String mainClass = fileOperation.getMainClass();
        if(!mainClass.isBlank()) {
            lines.append("Main-Class: ");
            lines.append(mainClass);
            lines.append("\n");
        }
        if(!includeLib) {
            List<Path> libs = fileOperation.libFiles("." + File.separator + "lib");
            if(libs != null) {
                lines.append("Class-Path: ");
                lines.append(libs
                    .stream()
                    .map(Path::toString)
                    .collect(Collectors.joining(";"))
                );
            }
        }
        fileOperation.createFile("Manifesto.txt", lines.toString());
    }
    public void createConfig(String source, String target, String mainClass, String flags, boolean includeLib) {
        StringBuilder lines = new StringBuilder();
        String[][] headers = {
            {"Root-Path: ", source.split("\\" + File.separator)[0].trim()},
            {"Source-Path: ", source},
            {"Class-Path: ", target},
            {"Main-Class: ", mainClass},
            {"Test-Path: ", source.split("\\" + File.separator)[0].trim() + File.separator + "test"},
            {"Test-Class: ", "test.TestLauncher"},
            {"Libraries: ", includeLib ? "include":"exclude"},
            {"Compile-Flags: ", flags}
        };
        for(int i=0; i<headers.length; ++i) {
            for(int j=0; j<headers[i].length; ++j) {
                lines.append(headers[i][j]);
            }
            lines.append("\n");
        }
        fileOperation.createFile("config.txt", lines.toString());
    }
}
