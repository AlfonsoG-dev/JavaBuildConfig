package application.builders;

import application.models.CommandModel;

public record ScriptBuilder(CommandModel cm) {

    private final static String OS_NAME = System.getProperty("os.name").toLowerCase();

    public void appendSource(StringBuilder lines) {
        if(OS_NAME.contains("windows")) {
            lines.append("$Source=");
            lines.append("\"");
            lines.append(cm.prepareSrcFiles());
            lines.append("\"");
            lines.append("\n");
        } else if(OS_NAME.contains("linux")) {
            lines.append("source=");
            lines.append("\"");
            lines.append(cm.prepareSrcFiles());
            lines.append("\"");
            lines.append("\n");
        }
    }
    public void appendLib(StringBuilder lines) {
        if(OS_NAME.contains("windows")) {
            lines.append("$Libs=");
            lines.append("\"");
            lines.append(cm.prepareLibFiles());
            lines.append("\"");
            lines.append("\n");
        } else if(OS_NAME.contains("linux")) {
            lines.append("libs=");
            lines.append("\"");
            lines.append(cm.prepareLibFiles());
            lines.append("\"");
            lines.append("\n");
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
            lines.append("\"");
            lines.append("\n");
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
            lines.append("\"");
            lines.append("\n");
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
            lines.append("Invoke-Expression $Compile && $Run");
        }
    }

    public String getScript(String targetURL, boolean includeLib) {
        StringBuilder lines = new StringBuilder();
        appendSource(lines);
        if(includeLib) appendLib(lines);
        appendCompileCommand(lines, targetURL, includeLib);
        appendRunCommand(lines, targetURL, includeLib);
        appendExecuteCommands(lines);
        return lines.toString();
    }

}
