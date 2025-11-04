package application.builders;

import application.models.CommandModel;
import application.operation.FileOperation;

import java.io.File;

public record JarBuilder(String root, FileOperation fileOperation) implements CommandModel {
    @Override
    public FileOperation getFileOperation() {
        return fileOperation;
    }
    @Override
    public String getRoot() {
        return root;
    }
    private String jarType(StringBuilder lines, String mainClass) {
        String executable = "";
        if(fileOperation.haveManifesto()) {
            lines.append("m ");
            executable = "m ";
        } else if(!mainClass.isEmpty()) {
            lines.append("e ");
            executable = "e ";
        }
        return executable;
    }
    @Override
    public String getCommand(String targetURL, String flags, boolean includeLib) {
        StringBuilder lines = new StringBuilder("jar -c");
        if(!flags.isEmpty()) lines.append(flags);
        lines.append("f");
        String mainClass = fileOperation.getMainClass();
        // jar format
        String format = jarType(lines, mainClass);

        lines.append(fileOperation.getProjectName() + ".jar ");
        switch(format) {
            case "m " -> lines.append("Manifesto.txt");
            case "e " -> lines.append(mainClass);
        }
        lines.append(" -C ");
        lines.append(targetURL);
        lines.append(File.separator);
        lines.append(" .");
        if(includeLib) {
            lines.append(" -C ");
            lines.append("extractionFiles");
            lines.append(File.separator);
            lines.append(" .");
        }
        return lines.toString();
    }
    public String getCommand(String targetURL, String mainClass, String flags, boolean includeLib) {
        StringBuilder lines = new StringBuilder("jar -c");
        if(!flags.isEmpty()) lines.append(flags);
        lines.append("f");
        // jar format
        String format = jarType(lines, mainClass);

        lines.append(fileOperation.getProjectName() + ".jar ");
        switch(format) {
            case "m " -> lines.append("Manifesto.txt");
            case "e " -> lines.append(mainClass);
        }
        lines.append(" -C ");
        lines.append(targetURL);
        lines.append(File.separator);
        lines.append(" .");
        if(includeLib) {
            lines.append(" -C ");
            lines.append("extractionFiles");
            lines.append(File.separator);
            lines.append(" .");
        }
        return lines.toString();
    }
}
