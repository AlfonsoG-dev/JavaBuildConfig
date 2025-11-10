package application.builders;

import application.models.CommandModel;
import application.operation.FileOperation;

import java.io.File;

public record JarBuilder(String root, FileOperation fileOperation) implements CommandModel {
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
    private void appendAssets(StringBuilder lines, String targetURL, boolean includeLib) {
        lines.append(" -C ");
        lines.append(targetURL);
        lines.append(File.separator);
        lines.append(" .");
        if(includeLib) {
            File m = new File("extractionFiles");
            String[] libFiles = prepareLibFiles().toString().split(";");
            // TODO: test append assets having lib dependencies.
            for(String l: libFiles) {
                lines.append(" -C ");
                File f = new File(l);
                lines.append( m.toPath().resolve(f.getName().replace(".jar", "")));
                lines.append(File.separator);
                lines.append(" .");
            }

        }
    }
    private void appendJarFormat(StringBuilder lines, String format, String mainClass) {
        switch(format) {
            case "m " -> lines.append("Manifesto.txt");
            case "e " -> lines.append(mainClass);
        }
    }
    @Override
    public String getCommand(String targetURL, String flags, boolean includeLib) {
        StringBuilder lines = new StringBuilder("jar -c");
        if(!flags.isEmpty()) lines.append(flags);
        lines.append("f");
        String mainClass = fileOperation.getMainClass();
        // append jar format
        String format = appendJarType(lines, mainClass);

        // append files
        lines.append(fileOperation.getProjectName() + ".jar ");
        appendJarFormat(lines, format, mainClass);

        // append assets
        appendAssets(lines, targetURL, includeLib);
        return lines.toString();
    }
    public String getCommand(String targetURL, String mainClass, String flags, boolean includeLib) {
        StringBuilder lines = new StringBuilder("jar -c");
        if(!flags.isEmpty()) lines.append(flags);
        lines.append("f");
        // jar format
        String format = appendJarType(lines, mainClass);

        lines.append(fileOperation.getProjectName() + ".jar ");
        appendJarFormat(lines, format, mainClass);

        appendAssets(lines, targetURL, includeLib);
        return lines.toString();
    }

    public String getCommand(String fileName, String targetURL, String mainClass, String flags, boolean includeLib) {
        StringBuilder lines = new StringBuilder("jar -c");
        if(!flags.isEmpty()) lines.append(flags);
        lines.append("f");
        // jar format
        String format = appendJarType(lines, mainClass);

        lines.append(fileName + ".jar ");
        appendJarFormat(lines, format, mainClass);

        appendAssets(lines, targetURL, includeLib);
        return lines.toString();
    }
    /**
     * When updating only add the .class files from bin
     */
    public String getUpdateJarCommand(String fileName, String targetURI, String flags, boolean includeLib) {
        StringBuilder command = new StringBuilder("jar -u");
        if(targetURI.isBlank()) return null;
        if(!flags.isBlank()) command.append("v");
        if(fileName.isBlank()) return null;
        // add jar file
        command.append("f ");
        command.append(fileName.replace(".jar", ""));
        command.append(".jar");

        // bin or class file source
        appendAssets(command, targetURI, includeLib);
        return command.toString();
    }
}

