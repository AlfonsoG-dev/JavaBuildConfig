package Application.Utils;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {
    private String rootFilePath;
    public FileUtils(String rootFilePath) {
        this.rootFilePath = rootFilePath;
    }

    public int countFilesInDirectory(File myDirectory) {
        int count = 0;
        if(myDirectory.listFiles() != null) {
            for(File f: myDirectory.listFiles()) {
                if(f.isFile()) {
                    ++count;
                }
            }
        }
        return count;
    }
    public String getCleanPath(String filePath) {
        return new File(filePath).toPath().normalize().toString();
    }
    public List<File> getFilesFromPath(String filePath) {
        List<File> names = new ArrayList<>();
        try {
            File miFile = new File(filePath);
            if(miFile.exists() && miFile.isFile()) {
                names.add(miFile);
            } else if(miFile.listFiles() != null) {
                names.addAll(
                        getDirectoryFiles(
                            Files.newDirectoryStream(miFile.toPath())
                        )
                );
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return names;
    }

    public List<File> getFilesFromDirectory(DirectoryStream<Path> files) {
        List<File> names = new ArrayList<>();
        for(Path p: files) {
            File f = p.toFile();
            if(f.isFile()) {
                names.add(f);
            } else if (f.isDirectory()){
                names.addAll(
                        getFilesFromPath(f.getPath())
                );
            }
        }
        return names;
    }
    public List<File> getDirectoryFiles(DirectoryStream<Path> misFiles) {
        List<File> names = new ArrayList<>();
        for(Path p: misFiles) {
            File f = p.toFile();
            try {
                if(f.exists() && f.isFile()) {
                    names.add(f);
                } else if(f.isDirectory()) {
                    names.addAll(
                            getDirectoryFiles(
                                Files.newDirectoryStream(f.toPath())
                            )
                    );
                }
            } catch(IOException err) {
                err.printStackTrace();
            }
        }
        return names;
    }

    public List<String> getDirectoriesNames(String path) {
        List<String> names = new ArrayList<>();
        try {
            String c = getCleanPath(path);
            File lf = new File(rootFilePath + File.separator + c);
            if(lf.listFiles() != null) {
                Files.newDirectoryStream(lf.toPath())
                    .forEach(e -> {
                        File f = e.toFile();
                        if(f.isDirectory()) {
                            names.add(f.getPath() + File.separator);
                            if(f.listFiles() != null) {
                                names.addAll(
                                        getDirectoriesNames(f.getPath())
                                );
                            }
                        }
                    });
            }
        } catch(IOException err) {
            err.printStackTrace();
        }
        return names;
    }

    /**
     * TODO: receive a params
     * - as the source of the .java files
     * - as the target of the .class files
     **/
    public String getProjectClassNames() {
        String b = "";
        List<String> names = new ArrayList<>();
        try {
            File srcFile = new File(rootFilePath + File.separator + "src");
            if(srcFile.listFiles() != null) {
                for(File f: srcFile.listFiles()) {
                    if(f.isFile() && f.getName().contains(".java")) {
                        names.add(".");
                        names.add(File.separator);
                        names.add("src");
                        names.add(File.separator);
                        names.add("*.java ");
                        break;
                    }
                }
                getDirectoriesNames("src")
                    .parallelStream()
                    .filter(e -> !e.isEmpty())
                    .forEach(e -> {
                        int countFiles = countFilesInDirectory(new File(e));
                        if(countFiles > 0) {
                            names.add(e + "*.java ");
                        }
                    });
            } else {
                System.out.println("[ INFO ]: " + rootFilePath + "\\src\\ folder not found");
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
