package application.operation;

import application.utils.*;

import java.io.File;
import java.io.IOException;

import java.nio.file.Path;

import java.util.List;
import java.util.HashMap;

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
    public boolean haveManifesto() {
        File f = new File("." + File.separator + "Manifesto.txt");
        return f.exists();
    }
    public boolean haveConfig() {
        File f = new File("." + File.separator + "config.txt");
        return f.exists();
    }
    public HashMap<String, String> getConfigValues() {
        HashMap<String, String> configs = new HashMap<>();
        String[] lines = TextUtils.getFileLines("config.txt").split("\n");
        for(String l: lines) {
            String[] values = l.split(":", 2);
            String k = values[0].trim();
            String v = 1 < values.length && !values[1].trim().isEmpty() ? values[1].trim():null;
            configs.put(k, v);
        }
        return configs;
    }
    public List<Path> sourceDirs() {
        List<Path> dirs = listFiles
            .stream()
            .filter(p -> p.toFile().isDirectory())
            .filter(p -> fu.countFiles(p) > 0)
            .toList();
        return dirs;

    }
    public boolean diferDate(Path sourceFile) {
        File source = sourceFile.toFile();
        File classFile = new File(sourceFile.toString().replace(root + File.separator, "bin" + File.separator).replace(".java", ".class"));
        return !classFile.exists() || source.lastModified() > classFile.lastModified();
    }
    public List<Path> libFiles(String sourceURI) {
        return ex.getResult(fu.callableList(sourceURI, 2))
            .stream()
            .filter(p -> p.toFile().isFile() && p.toFile().getName().contains(".jar"))
            .toList();
    }
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
