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
        if(!f.exists()){
            if(f.toPath().getNameCount() > 2) {
                System.out.println("[Info] Creating " + f.getPath());
                return f.mkdirs();
            } else {
                System.out.println("[Info] Creating " + f.getPath());
                return f.mkdir();
            }
        }
        return true;
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
                Path destination = Paths.get(destinationURI).resolve(sourcePath.getFileName());
                String o = Files.copy(sourcePath, destination, StandardCopyOption.REPLACE_EXISTING).toString();
                System.out.printf(
                    "\tCopy { %s%s%s } \n\t\tinto => [ %s%s%s ]%n",
                    TextUtils.Colors.YELLOW_UNDERLINED, sourcePath, TextUtils.Colors.ANSI_RESET,
                    TextUtils.Colors.GREEN_UNDERLINED, destination, TextUtils.Colors.ANSI_RESET
                );
                return o;
            }
            return "";
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public void copyDirectory(Path sourceDir, String destinationURI) {
        try {
            Path destinationRoot = Paths.get(destinationURI);
            Files.walk(sourceDir)
                .forEach(source -> {
                    try {
                        Path relative = sourceDir.relativize(source);
                        Path destination = destinationRoot.resolve(relative);
    
                        if (Files.isDirectory(source)) {
                            Files.createDirectories(destination);
                        } else {
                            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                            System.out.printf(
                                "\tCopy { %s%s%s } \n\t\tinto => [ %s%s%s ]%n",
                                TextUtils.Colors.YELLOW_UNDERLINED, source, TextUtils.Colors.ANSI_RESET,
                                TextUtils.Colors.GREEN_UNDERLINED, destination, TextUtils.Colors.ANSI_RESET
                            );
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Count the items that are type file inside a directory.
     * @param directory the directory to count its files.
     * @return the number of files inside the given dierectory, if its empty 0.
     */
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
    /**
     * A list of paths on the given nested level, if level is 0 the content will be recursively appended to the list.
     * @param pathURI the path to list its content.
     * @param level the nested level to search for content.
     * @return the list content inside the given path
     */
    public List<Path> listPaths(String pathURI, int level) {
        List<Path> files = new ArrayList<>();
        try {
            populateList(files, pathURI, level);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return files;
    }
    /**
     * A callable list of paths on the given nested level, if level is 0 the content will be recursively appended to the list.
     * @param pathURI the paths to list its content.
     * @param level the nested level to search for content.
     * @return a callable tasks to complete its future.
     */
    public Callable<List<Path>> callableList(String pathURI, int level) {
        return new Callable<List<Path>>() {
            @Override
            public List<Path> call() {
                List<Path> files = new ArrayList<>();
                try {
                    populateList(files, pathURI, level);
                } catch(IOException e) {
                    e.printStackTrace();
                }
                return files;
            }
        };
    }
}
