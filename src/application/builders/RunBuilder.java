package application.builders;

import application.models.CommandModel;
import application.operation.FileOperation;

import java.io.File;

public record RunBuilder(String root, FileOperation op) implements CommandModel {

    @Override
    public FileOperation getFileOperation() {
        return op;
    }
    /**
     * Get run command.
     * @param targetURI the path to the source of the .class files.
     * @param flags additional command to execute alongside with the run commnad.
     * @param includeLib true if you want to include the jar dependencies in the build process, false otherwise.
     */
    @Override
    public String getCommand(String targetURI, String flags, String includeLib) {
        if(!new File(targetURI).exists()) return null;
        StringBuilder command = new StringBuilder("java -cp '");
        String mainClassName = op.getMainClass();

        command.append(targetURI);
        if(!includeLib.equals("ignore") && !prepareLibFiles().isEmpty()) {
            command.append(";");
            command.append(prepareLibFiles());
        }
        command.append("' ");

        if(!mainClassName.isBlank()) command.append(mainClassName);
        command.append(" ");
        command.append(flags);

        return command.toString();
    }
    /**
     * Prepare the class name to the form `com.application.manual.App`
     * @param mainClass the main class name with file prefix included
     * @return the main class name formated to match package definition.
     */
    private String prepareClassName(String mainClass) {
        return mainClass.replace(root + File.separator, "").replace(File.separator, ".").replace(".java", "");
    }
    /**
     * Get run command when the user provides the class name to execute
     * @param mainClass class to execute
     * @param targetURI the path to the source of .class files.
     * @param flags the commands to execute alongside with run command.
     * @param includeLib if you want to include jar dependencies in the build process
     * @return the command.
     */
    public String getCommand(String mainClass, String targetURI, String flags, String includeLib) {
        if(!new File(targetURI).exists()) return null;
        StringBuilder command = new StringBuilder("java -cp '");

        command.append(targetURI);
        if(!includeLib.equals("ignore") && !prepareLibFiles().isEmpty()) {
            command.append(";");
            command.append(prepareLibFiles());
        }
        command.append("' ");
        command.append(prepareClassName(mainClass));

        command.append(" ");
        command.append(flags);

        return command.toString();
    }

}
