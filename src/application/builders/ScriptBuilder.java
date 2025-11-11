package application.builders;

import java.io.File;

import application.models.CommandModel;

public record ScriptBuilder(CommandModel cm) {

    private final static String OS_NAME = System.getProperty("os.name").toLowerCase();

    public void appendSource(StringBuilder lines) {
        if(OS_NAME.contains("windows")) {
            lines.append("$Source=");
            lines.append("\"");
            lines.append(cm.prepareSrcFiles());
            lines.append("\"\n");
        } else if(OS_NAME.contains("linux")) {
            lines.append("source=");
            lines.append("\"");
            lines.append(cm.prepareSrcFiles());
            lines.append("\"\n");
        }
    }
    public void appendLib(StringBuilder lines) {
        if(OS_NAME.contains("windows")) {
            lines.append("$Libs=");
            lines.append("\"");
            lines.append(cm.prepareLibFiles());
            lines.append("\"\n");
        } else if(OS_NAME.contains("linux")) {
            lines.append("libs=");
            lines.append("\"");
            lines.append(cm.prepareLibFiles());
            lines.append("\"\n");
        }
    }
    public void appendCompileCommand(StringBuilder lines, String targetURL, boolean includeLib) {
        if(OS_NAME.contains("windows")) {
            lines.append("$Compile=");
            lines.append("\"");
            lines.append("javac -d ");
            lines.append(targetURL);
            if(includeLib) {
                lines.append(" -cp '");
                lines.append("$Libs'");
            }
            lines.append(" $Source");
            lines.append("\"\n");
        } else if(OS_NAME.contains("linux")) {
            lines.append("javac -d ");
            lines.append(targetURL);
            if(includeLib) {
                lines.append(" -cp '");
                lines.append("$libs'");
            }
            lines.append(" $source");
            lines.append("\n");
        }
    }
    public void appendCreateJarCommand(StringBuilder lines, String targetURL, boolean includeLib) {
        // TODO: test this create jar command
        if(OS_NAME.contains("windows")) {
            lines.append("$Jar=\"jar -cfm ");
            lines.append(cm.getFileOperation().getProjectName());
            lines.append(".jar ");
            lines.append("Manifesto.txt -C ");
            lines.append(targetURL);
            lines.append(File.separator);
            lines.append(" .");
            if(includeLib) {
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
            // TODO: add linux support
            lines.append("jar -cfm ");
            lines.append(cm.getFileOperation().getProjectName());
            lines.append(".jar ");
            lines.append("Manifesto.txt -C ");
            lines.append(targetURL);
            lines.append(File.separator);
            lines.append(" .");
            if(includeLib) {
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
    public void appendRunCommand(StringBuilder lines, String targetURL, boolean includeLib) {
        if(OS_NAME.contains("windows")) {
            lines.append("$Run=");
            lines.append("\"");
            lines.append("java -cp '");
            lines.append(targetURL);
            if(includeLib) {
                lines.append(";");
                lines.append(cm.prepareLibFiles());
            }
            lines.append("' ");
            lines.append(cm.getFileOperation().getMainClass());
            lines.append("\"\n");
        } else if(OS_NAME.contains("linux")) {
            lines.append("java -cp '");
            lines.append(targetURL);
            if(includeLib) {
                lines.append(";");
                lines.append(cm.prepareLibFiles());
            }
            lines.append("' ");
            lines.append(cm.getFileOperation().getMainClass());
            lines.append("\n");
        }
    }
    // only on windows
    public void appendExecuteCommands(StringBuilder lines) {
        if(OS_NAME.contains("windows")) {
            lines.append("Invoke-Expression ($Compile + \" && \" + $Jar + \" && \" + $Run)\n");
        }
    }

    public String getScript(String targetURL, boolean includeLib) {
        StringBuilder lines = new StringBuilder();
        appendSource(lines);
        if(includeLib) appendLib(lines);
        appendCompileCommand(lines, targetURL, includeLib);
        appendCreateJarCommand(lines, targetURL, includeLib);
        appendRunCommand(lines, targetURL, includeLib);
        appendExecuteCommands(lines);
        return lines.toString();
    }

}
