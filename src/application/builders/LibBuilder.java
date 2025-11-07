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

    @Override
    public String getCommand(String targetURL, String flags, boolean includeLib) {
        StringBuilder command = new StringBuilder();
        if(includeLib) {
            String[] libFiles = prepareLibFiles().toString().split(";");
            for(String l: libFiles) {
                File f = new File(l);
                Path targetPath = Paths.get(targetURL).resolve(f.getName().replace(".jar", ""));
                if(op.createDirectories(targetPath.toString())) {
                    op.copyToPath(f.getPath(), targetPath.toString());
                }
            }
        }
        return command.toString();
    }
    
}
