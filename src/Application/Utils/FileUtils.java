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
    /**
     * normalize  given path
     */
    public String getCleanPath(String filePath) {
        return new File(filePath).toPath().normalize().toString();
    }

    /**
     * return the list of files from the given path
     */
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

    /**
     * return the list of files of the given directory
     */
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

    /**
     * return a list of directory names of the given directory
     */
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

    public String getFileLines(String file) {
        String b = "";
        BufferedReader br = null;
        File f = null;
        try {
            f = new File(file);
            if(f.isDirectory()) {
                System.err.println("[Error] only files here");
                return null;
            }
            if(!f.exists()) {
                System.err.println(
                        String.format("[Error] archive '%s' doesn't exists", file)
                );
                return null;
            }
            br = new BufferedReader(new FileReader(f));
            while(br.ready()) {
                b += br.readLine();
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return b;
    }
}
