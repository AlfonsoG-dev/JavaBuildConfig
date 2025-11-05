package application.builders;

import application.operation.FileOperation;

import java.io.File;

import java.nio.file.Path;

import java.util.stream.Collectors;

public class FileBuilder {
    private FileOperation fileOperation;

    public FileBuilder(FileOperation fileOperation) {
        this.fileOperation = fileOperation;
    }
    public void createIgnore() {
        String[] files = {
            "**bin\n",
            "**lib\n",
            "**extractionFiles\n",
            "**Manifesto.txt\n",
            "**Session.vim\n",
            "**.jar\n",
            "**.exen"
        };
        StringBuilder lines = new StringBuilder();
        for(String f: files) {
            lines.append(f);
        }
        fileOperation.createFile(".gitignore", lines.toString());
    }

    public void createManifesto(String author, boolean includeLib) {
        StringBuilder lines = new StringBuilder("Created-By: ");
        if(!author.isEmpty()) lines.append(author);
        lines.append("\n");
        String mainClass = fileOperation.getMainClass();
        if(!mainClass.isEmpty()) {
            lines.append("Main-Class: ");
            lines.append(mainClass);
            lines.append("\n");
        }
        if(includeLib) {
            lines.append("Class-Path: ");
            lines.append(fileOperation.libFiles("." + File.separator + "lib")
                .stream()
                .map(Path::toString)
                .collect(Collectors.joining(";"))
            );
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
            {"Test-Path: ", "src" + File.separator + "test"},
            {"Test-Class: ", "test.TestLauncher"},
            {"Libraries: ", includeLib ? "include":"exclude"},
            {"Compile-Flags: ", flags}
        };
        for(int i=0; i<headers.length; ++i) {
            for(int j=0; j<headers[i].length; ++j) {
                lines.append(headers[i][0]);
                lines.append(headers[i][j]);
                lines.append("\n");
            }
        }
        fileOperation.createFile("config.txt", lines.toString());
    }
}
