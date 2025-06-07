package Application.Utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.DirectoryStream;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class FileMethods {

    private String rootFilePath;

    public FileMethods(String rootFilePath) {
        this.rootFilePath = rootFilePath;
    }

    public File resolvePath(String parent, String children) {
        return new File(parent).toPath().resolve(children).toFile();
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

    public boolean searchForFileInRoot(String filePath) {
        boolean exists = false;
        // root
        File r = null;
        try {
            r = new File(rootFilePath);
            List<File> files = Arrays.asList(r.listFiles());
            if(files.contains(new File(filePath))) {
                exists = true;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return exists;
    }

    /**
     * return the list of files of the given directory
     */
    public List<File> getDirectoryFiles(DirectoryStream<Path> misFiles) {
        List<File> names = new ArrayList<>();
        for(Path p: misFiles) {
            File f = p.toFile();
            try {
                if(f.isFile()) {
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
    public String getCleanTextFromFile(String filePath) {
        StringBuffer build = new StringBuffer();
        FileReader miReader = null;
        BufferedReader miBufferReader = null;
        try {
            miReader = new FileReader(filePath);
            miBufferReader = new BufferedReader(miReader);
            while(miBufferReader.ready()) {
                build.append(miBufferReader.readLine());
                build.append("\n");
            }
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            if(miReader != null) {
                try {
                    miReader.close();
                } catch(Exception e) {
                    System.err.println(e);
                }
            }
            if(miBufferReader != null) {
                try {
                    miBufferReader.close();
                } catch(Exception e) {
                    System.err.println(e);
                } finally {
                    miBufferReader = null;
                }
            }
        }
        return build.toString();
    }
    public void writeToFile(String file, String sentences) {
        try (FileWriter w = new FileWriter(resolvePath(rootFilePath, file))) {
            w.write(sentences);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
