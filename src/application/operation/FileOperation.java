package application.operation;

import application.utils.*;

import java.io.File;
import java.io.IOException;

import java.nio.file.Path;

import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

public class FileOperation {
    private String root;
    private FileUtils fu;
    private ExecutorUtils ex;
    private List<Path> listFiles;
    public FileOperation(String root) {
        this.root = root;
        fu = new FileUtils(this.root);
        ex = new ExecutorUtils();
    }
    public FileOperation(String root, ExecutorUtils executorUtils) {
        this.root = root;
        fu = new FileUtils(this.root);
        this.ex = executorUtils;
    }
    public void populateList(String sourceURI) {
        listFiles = ex.getResult(fu.callableList(sourceURI, 0));
    }
    public boolean createDirectories(String dirURI) {
        return fu.createDirectory(dirURI);
    }
    public void createFile(String fileURI, String lines) {
        fu.createFile(fileURI, lines);
    }
    public List<Path> sourceFiles() {
        List<Path> files = listFiles
            .stream()
            .filter(p -> p.toFile().isFile())
            .toList();
        return files;
    }
    /**
     * search in the source path for the first class that has main attribute present. 
     * @return the main class package.name, or empty string
     */
    public String getMainClass() {
        String sourceRoot = root + File.separator;
        File f = new File(root);
        if(f.listFiles() != null) {
            for(Path p: sourceFiles()) {
                File mf = p.toFile();
                if(mf.isFile() && mf.getName() != "TestLauncher.java") {
                    String[] lines = TextUtils.getFileLines(mf.getPath()).split("\n");
                    for(String l: lines) {
                        if(l.contains("public static void main")) {
                            return mf.getPath().replace(sourceRoot, "").replace(".java", "").replace(File.separator, ".");
                        }
                    }
                }
            }
        }
        return "";
    }
    /**
     * Get the project name form directory canonical path.name
     * @return the project name
     */
    public String getProjectName() {
        String name = "";
        try {
            File r = new File("." + File.separator);
            File local = new File(r.getCanonicalPath());
            name = local.getName();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return name;
    }
    /**
     * verify if manifesto is present.
     * @return true if present, false otherwise 
     */
    public boolean haveManifesto() {
        File f = new File("." + File.separator + "Manifesto.txt");
        return f.exists();
    }
    /**
     * verify if configuration is present.
     * @return true if present, false otherwise 
     */
    public boolean haveConfig() {
        File f = new File("." + File.separator + "config.txt");
        return f.exists();
    }
    /**
     * Get the project author from manifesto.
     * @return the author, null if not defined.
     */
    public String getAuthor() {
        if(haveManifesto()) {
            String[] lines = TextUtils.getFileLines("Manifesto.txt").split("\n");
            for(String l: lines) {
                if(l.contains("Created-By")) {
                    return l.split(":", 2)[1].trim();
                }
            }
        }
        return null;
    }
    /**
     * A table with configuration attributes for the build tool.
     * @return A HasMap with the configuration attributes.
     */
    public HashMap<String, String> getConfigValues() {
        HashMap<String, String> configs = new HashMap<>();
        String[] lines = null;
        if(!new File("config.txt").exists()) {
            lines = new String[] {
                "Root-Path: src\n",
                "Source-Path: src\n",
                "Class-Path: bin\n",
                "Main-Class: \n",
                "Test-Path: src" + File.separator + "test\n",
                "Test-Class: test.TestLauncher\n", 
                "Libraries: exclude\n",
                "Compile-Flags: -Werror"
            };
        } else {
            lines = TextUtils.getFileLines("config.txt").split("\n");
        }
        for(String l: lines) {
            String[] values = l.split(":", 2);
            String k = values[0].trim();
            String v = 1 < values.length && !values[1].trim().isEmpty() ? values[1].trim():null;
            configs.put(k, v);
        }
        return configs;
    }
    /**
     * A list of source directories.
     * @return the source directories.
     */
    public List<Path> sourceDirs() {
        List<Path> dirs = listFiles
            .stream()
            .filter(p -> p.toFile().isDirectory())
            .filter(p -> fu.countFiles(p) > 0)
            .toList();
        return dirs;

    }
    /**
     * Compare last modified date in milliseconds of source files with their counterapart in class path.
     * @param sourceFile path to the .java files.
     * @return true if there is a difference in dates with the source file and class file, false otherwise.
     */
    public boolean differDate(Path sourceFile) {
        File source = sourceFile.toFile();
        File classFile = new File(sourceFile.toString().replace(root + File.separator, "bin" + File.separator).replace(".java", ".class"));
        return !classFile.exists() || source.lastModified() > classFile.lastModified();
    }
    /**
     * Search for the files that depend on a particular file, using the package name with the file name.
     * @param packageName the package of the files that others depend on.
     * @param fileName the file that others depend on.
     * @return the files that depend on the searched file.
     */
    public Set<Path> getDependencies(String packageName, String fileName) {
        Set<Path> imports = new HashSet<>();
        List<Path> files = sourceFiles();
        for(Path p: files) {
            String[] lines = TextUtils.getFileLines(p.toString()).split("\n");
            for(String l: lines) {
                l = l.trim().replace(";", "");
                String packDir = packageName.replace("." + fileName.replace(".java", ""), "");
                if(l.startsWith("import") && l.contains(packageName)) {
                    imports.add(p);
                } else if(l.startsWith("import") && l.endsWith("*") && l.contains(packDir)) {
                   imports.add(p);
                }
            }
        }
        return imports;
    }
    /**
     * List of jar dependencies.
     * @param sourceURI where the .jar dependencies are
     * @return a list with the jar path.
     */
    public List<Path> libFiles(String sourceURI) {
        return ex.getResult(fu.callableList(sourceURI, 2))
            .stream()
            .filter(p -> p.toFile().isFile() && p.toFile().getName().contains(".jar"))
            .toList();
    }
    /**
     * Copy files or directory to a destination path.
     * @param sourceURI the source file or directory.
     * @param destinationURI the destination path.
     */
    public void copyToPath(String sourceURI, String destinationURI) {
        File f = new File(sourceURI);
        System.out.println("[Info] Copying ...");
        if(f.isDirectory()) {
            fu.copyDirectory(f.toPath(), destinationURI);
        } else {
            fu.copyFile(f.toPath(), destinationURI);
        }
    }
}
