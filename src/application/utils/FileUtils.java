package application.utils;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Callable;

import java.io.File;
import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.FileVisitOption;
import java.nio.file.StandardCopyOption;

public class FileUtils {
    private String root;
    public FileUtils(String root) {
        this.root = Optional.ofNullable(root).orElse("src");
    }
    public File getRoot() {
        return new File(root);
    }
    private void populateList(List<Path> list, String pathURI, int level) throws IOException {
        if(level > 0) {
            list.addAll(Files.walk(Paths.get(pathURI), level, FileVisitOption.FOLLOW_LINKS).toList());
        } else {
            list.addAll(Files.walk(Paths.get(pathURI), FileVisitOption.FOLLOW_LINKS).toList());
        }
    }
    public boolean createDirectory(String pathURI) {
        File f = new File(pathURI);
        if(f.exists()) return false;
        if(f.isDirectory()) {
            if(f.toPath().getNameCount() > 2) {
                return f.mkdirs();
            } else {
                return f.mkdir();
            }
        }
        return false;
    }
    public void createFile(String fileURI, String lines) {
        TextUtils.writeLines(fileURI, lines);
    }
    /**
     * copy a file to a destination directory
     * @param sourceURI the file path
     * @param destinationURI the directory destination path
     * @return the destination with the copied file.
     */
    public String copyFile(Path sourcePath, String destinationURI) {
        try {
            if(sourcePath.toFile().isFile()) {
                sourcePath = sourcePath.normalize();
                Path newPath = Paths.get(destinationURI).resolve(sourcePath);
                String o =  Files.copy(sourcePath, newPath, StandardCopyOption.REPLACE_EXISTING).toString().toString();
                System.out.println(
                        "[Info] Copying { " + TextUtils.Colors.YELLOW_UNDERLINED + sourcePath.toString() + TextUtils.Colors.ANSI_RESET + " } as => [ " + TextUtils.Colors.GREEN_UNDERLINED + o + TextUtils.Colors.ANSI_RESET + " ]"
                );
                return o;
            }
            return "";
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public String copyDirectory(Path sourcePath, String destinationURI) {
        // sourcePath already check if its a directory
        sourcePath = sourcePath.normalize();
        List<Path> files = listPaths(sourcePath.toString(), 0);
        files
            .stream()
            .filter(p -> p.toFile().isDirectory())
            .forEach(p -> {
                Path destinationPath = Paths.get(destinationURI).resolve(p);
                try {
                    Files.createDirectories(destinationPath);
                } catch(IOException e) {
                    e.printStackTrace();
                }
            });
        files
            .stream()
            .forEach(p -> {
                copyFile(p, destinationURI);
            });
        return "";
    }
    public int countFiles(Path directory) {
        int n = 0;
        File f = directory.toFile();
        if(f.isDirectory() && f.listFiles() != null) {
            for(File mf: f.listFiles()) {
                if(mf.isFile()) {
                    ++n;
                }
            }
        }
        return n;
    }
    public List<Path> listPaths(String pathURI, int level) {
        List<Path> files = new ArrayList<>();
        try {
            populateList(files, pathURI, level);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return files;
    }
    public Callable<List<Path>> callableList(String pathURI, int level) {
        return new Callable<List<Path>>() {
            @Override
            public List<Path> call() {
                return listPaths(pathURI, level);
            }
        };
    }
}
