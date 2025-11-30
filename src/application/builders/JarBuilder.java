package application.builders;

import application.models.CommandModel;
import application.operation.FileOperation;

import java.io.File;

public record JarBuilder(String root, FileOperation fileOperation) implements CommandModel {
    private static final String JAR_COMMAND = "jar -c";
    private static final String FILE_EXTENSION = ".jar ";
    @Override
    public FileOperation getFileOperation() {
        return fileOperation;
    }
    private String appendJarType(StringBuilder lines, String mainClass) {
        String executable = "";
        if(!mainClass.isBlank()) {
            executable = "e ";
        } else if(fileOperation.haveManifesto()) {
            executable = "m ";
        }
        lines.append(executable);
        return executable;
    }
    private void appendAssets(StringBuilder lines, String targetURI, String includeLib) {
        lines.append(" -C .");
        lines.append(File.separator);
        lines.append(targetURI);
        lines.append(File.separator);
        lines.append(" .");
        File m = new File("extractionFiles");
        if(includeLib.equals("include") && m.listFiles() != null) {
            //append assets having lib dependencies.
            for(File l: m.listFiles()) {
                lines.append(" -C .");
                lines.append(File.separator);
                lines.append(l.toPath().normalize().toString());
                lines.append(File.separator);
                lines.append(" .");
            }

        }
    }
    private void appendJarFormatAsset(StringBuilder lines, String format, String mainClass) {
        switch(format) {
            case "m " -> lines.append("Manifesto.txt");
            case "e " -> lines.append(mainClass);
            default -> lines.append("");
        }
    }
    @Override
    public String getCommand(String targetURI, String flags, String includeLib) {
        StringBuilder lines = new StringBuilder(JAR_COMMAND);
        if(!flags.isBlank()) lines.append(flags);
        lines.append("f");
        String mainClass = fileOperation.getMainClass();
        // append jar format
        String format = appendJarType(lines, mainClass);

        // append files
        lines.append(fileOperation.getProjectName());
        lines.append(FILE_EXTENSION);
        appendJarFormatAsset(lines, format, mainClass);

        // append assets
        appendAssets(lines, targetURI, includeLib);
        return lines.toString();
    }
    public String getCommand(String targetURI, String mainClass, String flags, String includeLib) {
        StringBuilder lines = new StringBuilder(JAR_COMMAND);
        if(!flags.isBlank()) lines.append(flags);
        lines.append("f");
        // jar format
        String format = appendJarType(lines, mainClass);

        lines.append(fileOperation.getProjectName());
        lines.append(FILE_EXTENSION);
        appendJarFormatAsset(lines, format, mainClass);

        appendAssets(lines, targetURI, includeLib);
        return lines.toString();
    }

    public String getCommand(String fileName, String targetURI, String mainClass, String flags, String includeLib) {
        StringBuilder lines = new StringBuilder(JAR_COMMAND);
        if(!flags.isBlank()) lines.append(flags);
        lines.append("f");
        // jar format
        String format = appendJarType(lines, mainClass);

        lines.append(fileName);
        lines.append(FILE_EXTENSION);
        appendJarFormatAsset(lines, format, mainClass);

        appendAssets(lines, targetURI, includeLib);
        return lines.toString();
    }
    /**
     * When updating only add the .class files from bin
     * @param fileName - the name of the jar file to update.
     */
    public String getUpdateJarCommand(String fileName, String targetURI, String mainClass, String flags, String includeLib) {
        StringBuilder command = new StringBuilder("jar -u");
        if(targetURI.isBlank()) return null;
        if(!flags.isBlank()) command.append("v");
        if(fileName.isBlank()) return null;

        // add jar file
        command.append("f");
        String format = appendJarType(command, mainClass);
        command.append(fileName);
        command.append(FILE_EXTENSION);
        // append manifesto to the .jar update process
        appendJarFormatAsset(command, format, mainClass);
        // bin or class file source
        appendAssets(command, targetURI, includeLib);
        return command.toString();
    }
}

