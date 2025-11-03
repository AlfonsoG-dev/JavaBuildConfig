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

public class FileUtils {
    private String root;
    public FileUtils(String root) {
        this.root = Optional.ofNullable(root).orElse("." + File.separator);
    }
    public File getRoot() {
        return new File(root);
    }
    private void populateList(List<Path> list, String pathURL, int level) throws IOException {
        if(level > 0) {
            list.addAll(Files.walk(Paths.get(pathURL), level, FileVisitOption.FOLLOW_LINKS).toList());
        } else {
            list.addAll(Files.walk(Paths.get(pathURL), FileVisitOption.FOLLOW_LINKS).toList());
        }
    }
    public List<Path> listPaths(String pathURL, int level) {
        List<Path> files = new ArrayList<>();
        try {
            populateList(files, pathURL, level);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return files;
    }
    public Callable<List<Path>> callableList(String pathURL, int level) {
        return new Callable<List<Path>>() {
            @Override
            public List<Path> call() {
                return listPaths(pathURL, level);
            }
        };
    }
}
