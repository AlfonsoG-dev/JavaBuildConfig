package application.builders;

import application.models.CommandModel;
import application.operation.FileOperation;
import application.utils.ExecutorUtils;

import java.io.File;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.ArrayList;

public record LibBuilder(String root, FileOperation op) implements CommandModel {

    @Override
    public FileOperation getFileOperation() {
        return op;
    }
    private void appendExtractJarContent(StringBuilder command, File extract) {
        command.append("jar -x");
        command.append("f ");
        command.append(extract.getName());
        command.append(".jar");
        command.append(" && rm -r ");
        command.append(extract.getName());
        command.append(".jar");
    }

    @Override
    public String getCommand(String targetURI, String flags, boolean includeLib) {
        // don't include in build process
        if(!includeLib) return null;

        StringBuilder command = new StringBuilder();
        File targetFile = new File(targetURI);

        String[] libFiles = prepareLibFiles().toString().split(";");
        for(String l: libFiles) {
            File f = new File(l);
            Path targetPath = targetFile.toPath().resolve(f.getName().replace(".jar", ""));
            // create a directory with the same name as the jar file in extractionFiles 
            if(op.createDirectories(targetPath.toString())) {
                op.copyToPath(f.getPath(), targetPath.toString());
            }
        }
        if(targetFile.listFiles() != null) {
            for(File s: targetFile.listFiles()) {
                command.append("cd ");
                command.append(s.getPath());
                command.append(" && ");
                appendExtractJarContent(command, s);
            }
        }
        return command.toString();
    }
    public void appendCommandToProcess(ExecutorUtils ex, String targetURI, boolean includeLib) {
        if(!includeLib) return;
        String[] libFiles = prepareLibFiles().toString().split(";");
        List<Path> extracFiles = new ArrayList<>();
        for(String l: libFiles) {
            File f = new File(l);
            Path targetPath = Paths.get(targetURI).resolve(f.getName().replace(".jar", ""));
            // if the extract file already exists don't extract its content.
            if(!targetPath.toFile().exists() && op.createDirectories(targetPath.toString())) {
                // create a directory with the same name as the jar file in extractionFiles 
                op.copyToPath(f.getPath(), targetPath.toString());
                extracFiles.add(targetPath);
            }
        }
        if(!extracFiles.isEmpty()) {
            // TODO: if is able to append more than 1 lib dependency.
            for(Path s: extracFiles) {
                StringBuilder command = new StringBuilder();
                // enter the directory
                command.append("cd ");
                command.append(s.getParent() != null ? s.getParent():"");
                command.append(" && ");
                // the .jar dependency has the same name as the parent folder.
                command.append("jar -x");
                command.append("f ");
                // should have .jar included in file name
                command.append(s.getFileName());
                // remove the .jar file.
                command.append(" && rm -r ");
                command.append(s.getFileName());
                // TODO: test if the string append is completed or you have to extract the command outside the for-loop to get the complete command sequence.
                ex.appendCommandToCallableProcess(command.toString());
            }
        }
    }
}
