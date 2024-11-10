package Application.Operations;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Application.Utils.FileUtils;

public class FileOperations {

    private String rootFilePath;
    private FileUtils fUtils;
    public FileOperations(String rootFilePath) {
        this.rootFilePath = rootFilePath;
        fUtils = new FileUtils(rootFilePath);
    }

    /**
     * return the proyect class names for the compile command.
     */
    public String getProjectClassNames(String source) {
        String b = "";
        List<String> names = new ArrayList<>();
        try {
            File srcFile = new File(rootFilePath + File.separator + source);
            if(srcFile.listFiles() != null) {
                for(File f: srcFile.listFiles()) {
                    if(f.isFile() && f.getName().contains(".java")) {
                        names.add(".");
                        names.add(File.separator);
                        names.add(source);
                        names.add(File.separator);
                        names.add("*.java ");
                        break;
                    }
                }
                fUtils.getDirectoriesNames(source)
                    .parallelStream()
                    .filter(e -> !e.isEmpty())
                    .forEach(e -> {
                        int countFiles = fUtils.countFilesInDirectory(new File(e));
                        if(countFiles > 0) {
                            names.add(e + "*.java ");
                        }
                    });
            } else {
                System.out.println(
                        String.format("[Error] Directory '%s' is empty", source)
                );
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        b += names
            .parallelStream()
            .collect(Collectors.joining());
        return b;
    }
}
