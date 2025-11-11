package application.builders;

import application.models.CommandModel;
import application.operation.FileOperation;

import java.io.File;

import java.nio.file.Path;
import java.nio.file.Paths;

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
    public String getCommand(String targetURL, String flags, boolean includeLib) {
        StringBuilder command = new StringBuilder();
        // don't include in build process
        if(!includeLib) return null;

        String[] libFiles = prepareLibFiles().toString().split(";");
        for(String l: libFiles) {
            File f = new File(l);
            Path targetPath = Paths.get(targetURL).resolve(f.getName().replace(".jar", ""));
            // create a directory with the same name as the jar file in extractionFiles 
            if(op.createDirectories(targetPath.toString())) {
                op.copyToPath(f.getPath(), targetPath.toString());
            }
        }
        File f = new File(targetURL);
        if(f.listFiles() != null) {
            for(File s: f.listFiles()) {
                command.append("cd ");
                command.append(s.getPath());
                command.append(" && ");
                appendExtractJarContent(command, s);
            }
        }
        return command.toString();
    }
    
}
