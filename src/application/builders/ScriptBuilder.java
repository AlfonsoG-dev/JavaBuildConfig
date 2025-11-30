package application.builders;

import java.io.File;

import application.models.CommandModel;

public record ScriptBuilder(CommandModel cm) {

    private static final String DEFAULT_LIB_CONFIG = "ignore";
    private static final boolean OS_NAME_WINDOWS = System.getProperty("os.name").contains("windows");

    public void appendSource(StringBuilder lines) {
        if(OS_NAME_WINDOWS) {
            lines.append("$Source=");
            lines.append("\"");
            lines.append(cm.prepareSrcFiles());
            lines.append("\"\n");
        } else {
            lines.append("source=");
            lines.append("\"");
            lines.append(cm.prepareSrcFiles());
            lines.append("\"\n");
        }
    }
    public void appendLib(StringBuilder lines) {
        if(OS_NAME_WINDOWS){
            lines.append("$Libs=");
            lines.append("\"");
            lines.append(cm.prepareLibFiles());
            lines.append("\"\n");
        } else {
            lines.append("libs=");
            lines.append("\"");
            lines.append(cm.prepareLibFiles());
            lines.append("\"\n");
        }
    }
    public void appendCompileCommand(StringBuilder lines, String targetURI, String includeLib) {
        if(OS_NAME_WINDOWS) {
            lines.append("$Compile=");
            lines.append("\"");
            lines.append("javac -d ");
            lines.append(targetURI);
            if(!includeLib.equals("ingore")) {
                lines.append(" -cp '");
                lines.append("$Libs'");
            }
            lines.append(" $Source");
            lines.append("\"\n");
        } else {
            lines.append("javac -d ");
            lines.append(targetURI);
            if(!includeLib.equals(DEFAULT_LIB_CONFIG)) {
                lines.append(" -cp '");
                lines.append("$libs'");
            }
            lines.append(" $source");
            lines.append("\n");
        }
    }
    public void appendCreateJarCommand(StringBuilder lines, String targetURI, String includeLib) {
        // TEST: create jar command
        if(OS_NAME_WINDOWS) {
            lines.append("$Jar=\"jar -cfm ");
            lines.append(cm.getFileOperation().getProjectName());
            lines.append(".jar ");
            lines.append("Manifesto.txt -C ");
            lines.append(targetURI);
            lines.append(File.separator);
            lines.append(" .");
            if(includeLib.equals("include")) {
                File f = new File("extractionFiles");
                for(File l: f.listFiles()) {
                    lines.append(" -C ");
                    lines.append(l.getPath());
                    lines.append(File.separator);
                    lines.append(" .");
                }
            }
            lines.append(" \"\n");
        } else {
            // TEST: linux support
            lines.append("jar -cfm ");
            lines.append(cm.getFileOperation().getProjectName());
            lines.append(".jar ");
            lines.append("Manifesto.txt -C ");
            lines.append(targetURI);
            lines.append(File.separator);
            lines.append(" .");
            if(includeLib.equals("include")) {
                File f = new File("extractionFiles");
                for(File l: f.listFiles()) {
                    lines.append(" -C ");
                    lines.append(l.getPath());
                    lines.append(File.separator);
                    lines.append(" .");
                }
            }
            lines.append("\n");
        }
    }
    public void appendRunCommand(StringBuilder lines, String targetURI, String includeLib) {
        if(OS_NAME_WINDOWS) {
            lines.append("$Run=");
            lines.append("\"");
            lines.append("java -cp '");
            lines.append(targetURI);
            if(!includeLib.equals(DEFAULT_LIB_CONFIG)) {
                lines.append(";");
                lines.append("$Libs");
            }
            lines.append("' ");
            lines.append(cm.getFileOperation().getMainClass());
            lines.append("\"\n");
        } else {
            lines.append("java -cp '");
            lines.append(targetURI);
            if(!includeLib.equals(DEFAULT_LIB_CONFIG)) {
                lines.append(";");
                lines.append("$libs");
            }
            lines.append("' ");
            lines.append(cm.getFileOperation().getMainClass());
            lines.append("\n");
        }
    }
    // only on windows
    public void appendExecuteCommands(StringBuilder lines) {
        if(OS_NAME_WINDOWS) {
            lines.append("Invoke-Expression ($Compile + \" && \" + $Jar + \" && \" + $Run)\n");
        }
    }

    public String getScript(String targetURI, String includeLib) {
        StringBuilder lines = new StringBuilder();
        appendSource(lines);
        if(!includeLib.equals(DEFAULT_LIB_CONFIG)) appendLib(lines);
        appendCompileCommand(lines, targetURI, includeLib);
        appendCreateJarCommand(lines, targetURI, includeLib);
        appendRunCommand(lines, targetURI, includeLib);
        appendExecuteCommands(lines);
        return lines.toString();
    }

}
