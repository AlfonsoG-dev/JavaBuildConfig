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
            File f = new File(targetURL);
            if(f.listFiles() != null) {
                for(File s: f.listFiles()) {
                    command.append("cd ");
                    command.append(s.getPath());
                    command.append(" && ");
                    if(s.listFiles() != null) {
                        for(File d: s.listFiles()) {
                            if(d.getName().contains(".jar")) {
                                command.append("jar -x");
                                command.append(flags);
                                command.append("f ");
                                command.append(d.getName());
                                command.append(" && rm -r ");
                                command.append(d.getName());
                                command.append(" cd.. && ");
                            }
                        }
                    }
                }
            }
        }
        String clean = command.toString();
        if((clean.length()-9) < clean.length()) {
            clean = clean.substring(0, clean.length()-9);
        }
        return clean;
    }
    
}
