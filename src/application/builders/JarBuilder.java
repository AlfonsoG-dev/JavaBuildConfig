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
        if(fileOperation.haveManifesto()) {
            executable = "m ";
        } else if(!mainClass.isEmpty()) {
            executable = "e ";
        }
        lines.append(executable);
        return executable;
    }
    private void appendAssets(StringBuilder lines, String targetURI, boolean includeLib) {
        lines.append(" -C .");
        lines.append(File.separator);
        lines.append(targetURI);
        lines.append(File.separator);
        lines.append(" .");
        if(includeLib) {
            File m = new File("extractionFiles");
            String[] libFiles = prepareLibFiles().toString().split(";");
            //append assets having lib dependencies.
            for(String l: libFiles) {
                lines.append(" -C .");
                lines.append(File.separator);
                File f = new File(l);
                lines.append( m.toPath().resolve(f.getName().replace(FILE_EXTENSION.trim(), "")));
                lines.append(File.separator);
                lines.append(" .");
            }

        }
    }
    private void appendJarFormat(StringBuilder lines, String format, String mainClass) {
        switch(format) {
            case "m " -> lines.append("Manifesto.txt");
            case "e " -> lines.append(mainClass);
            default -> lines.append("");
        }
    }
    @Override
    public String getCommand(String targetURI, String flags, boolean includeLib) {
        StringBuilder lines = new StringBuilder(JAR_COMMAND);
        if(!flags.isEmpty()) lines.append(flags);
        lines.append("f");
        String mainClass = fileOperation.getMainClass();
        // append jar format
        String format = appendJarType(lines, mainClass);

        // append files
        lines.append(fileOperation.getProjectName());
        lines.append(FILE_EXTENSION);
        appendJarFormat(lines, format, mainClass);

        // append assets
        appendAssets(lines, targetURI, includeLib);
        return lines.toString();
    }
    public String getCommand(String targetURI, String mainClass, String flags, boolean includeLib) {
        StringBuilder lines = new StringBuilder(JAR_COMMAND);
        if(!flags.isEmpty()) lines.append(flags);
        lines.append("f");
        // jar format
        String format = appendJarType(lines, mainClass);

        lines.append(fileOperation.getProjectName());
        lines.append(FILE_EXTENSION);
        appendJarFormat(lines, format, mainClass);

        appendAssets(lines, targetURI, includeLib);
        return lines.toString();
    }

    public String getCommand(String fileName, String targetURI, String mainClass, String flags, boolean includeLib) {
        StringBuilder lines = new StringBuilder(JAR_COMMAND);
        if(!flags.isEmpty()) lines.append(flags);
        lines.append("f");
        // jar format
        String format = appendJarType(lines, mainClass);

        lines.append(fileName);
        appendJarFormat(lines, format, mainClass);

        appendAssets(lines, targetURI, includeLib);
        return lines.toString();
    }
    /**
     * When updating only add the .class files from bin
     * @param fileName - the name of the jar file to update.
     */
    public String getUpdateJarCommand(String fileName, String targetURI, String flags) {
        StringBuilder command = new StringBuilder("jar -u");
        if(targetURI.isBlank()) return null;
        if(!flags.isBlank()) command.append("v");
        if(fileName.isBlank()) return null;
        if(!fileName.contains(FILE_EXTENSION)) fileName = fileName + FILE_EXTENSION;

        // add jar file
        command.append("f ");
        command.append(fileName);
        /**
         * FIXME: for now when updating lib files it generates a corrupt jar file.
         * a temporal fix is to disable the includeLib, making always false.
        */
        // bin or class file source
        appendAssets(command, targetURI, false);
        return command.toString();
    }
}

