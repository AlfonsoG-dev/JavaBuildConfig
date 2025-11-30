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
    /**
     * Create the manifest file for the .jar operation.
     * <p> The .jar operation uses the manifest fields to store meta-data in the .jar file.
     * @param author - the creator of the project.
     * @param includeLib - a series of options between (include, exclude, ignore) where:
     * <p> include means to include the lib dependencies in the build process.
     * <p> exclude means to exclude the lib dependencies of the build process by placing in the manifest the field. Class-Path: lib.jar
     * <p> ignore is the default value, and it means to ignore the lib dependencies of the build process.
     */
    public void createManifesto(String author, String includeLib) {
        StringBuilder lines = new StringBuilder("Created-By: ");
        if(!author.isBlank()) lines.append(author);
        lines.append("\n");
        String mainClass = fileOperation.getMainClass();
        if(!mainClass.isBlank()) {
            lines.append("Main-Class: ");
            lines.append(mainClass);
            lines.append("\n");
        }
        if(includeLib.equals("exclude")) {
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
        lines.append("\n");
        fileOperation.createFile("Manifesto.txt", lines.toString());
    }
    public void createConfig(String source, String target, String mainClass, String flags, String includeLib) {
        StringBuilder lines = new StringBuilder();
        String[][] headers = {
            {"Root-Path: ", source.split("\\" + File.separator)[0].trim()},
            {"Source-Path: ", source},
            {"Class-Path: ", target},
            {"Main-Class: ", mainClass},
            {"Test-Path: ", source.split("\\" + File.separator)[0].trim() + File.separator + "test"},
            {"Test-Class: ", "test.TestLauncher"},
            {"Libraries: ", includeLib},
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
