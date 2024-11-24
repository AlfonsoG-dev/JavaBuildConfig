package Application.Operations;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    public HashMap<String, String> getConfigValues() {
        HashMap <String, String> projectConfig = new HashMap<>();
        try {
            String configPath = "./config.txt";
            boolean isConfigPresent = fUtils.searchForFileInRoot(configPath);
            if(!isConfigPresent) {
                System.out.println(String.format("[Error] archive '%s' doesn't exists", configPath));
                return null;
            }
            String fileLines = fUtils.getCleanTextFromFile(configPath);
            if(fileLines == null) {
                System.out.println(String.format("[Info] empty file '%s'", configPath));
                return null;
            }
            String[] configSentences = fileLines.split("\n");
            for(int i=0; i<configSentences.length; ++i) {
                // INFO: only get the first split when the sentences have :
                String[] sentences = configSentences[i].split(":", 2);
                String keys = sentences[0].trim();
                String values = "";
                if(sentences.length > 1) {
                    values = sentences[1].trim();
                }
                projectConfig.put(keys, values);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return projectConfig;
    }

    /**
     * return the proyect class names for the compile command.
     */
    public String getProjectClassNames(String source) {
        String b = "";
        List<String> names = new ArrayList<>();
        source = fUtils.getCleanPath(source);
        try {
            File srcFile = new File(rootFilePath + source);
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
                    .stream()
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
            .stream()
            .collect(Collectors.joining());
        return b;
    }
    public String getProjectLibFiles() { 
        String b = "";
        List<String> libFiles = new ArrayList<>();
        File sf = null;
        try {
            sf = new File(rootFilePath + "lib");
            if(sf.listFiles() != null) {
                Files.newDirectoryStream(sf.toPath())
                    .forEach(p -> {
                        File lf = p.toFile();
                        if(lf.isFile() && lf.getName().contains(".jar")) {
                            libFiles.add(lf.getPath());
                        } else if(lf.isDirectory()) {
                            try {
                                Files.newDirectoryStream(lf.toPath())
                                    .forEach(ap -> {
                                        File alf = ap.toFile();
                                        if(alf.isFile() && alf.getName().contains(".jar")) {
                                            libFiles.add(lf.getPath());
                                        }
                                    });
                            } catch(IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        b+=libFiles
            .stream()
            .map(e -> e + ";")
            .collect(Collectors.joining());
        return b;
    }
    public String getMainClass() {
        String b = null;
        try {
            File f = new File(new File(rootFilePath).getCanonicalPath());
            List<File> m = fUtils.getDirectoryFiles(Files.newDirectoryStream(f.toPath()))
                .parallelStream()
                .filter(e -> e.isFile() && e.getName().contains(".java"))
                .toList();
            for(File lf: m) {
                if(lf.getName().equals(f.getName() + ".java")) {
                    b = f.getName();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return b;
    }
    public void createBuildScript(String classPath, String sourcePath, String libFiles, String mainClass) {
        try {
            // TODO: for now only for windows
            String
                classes = "$classes=\"" + this.getProjectClassNames(sourcePath) + "\"\n",
                libs = "$libs=\"" + libFiles + "\"\n",
                compile = "$compile=\"javac -d " + classPath,
                run = "$run=\"java -cp '" + classPath + ";",
                invoke = "$invoke=$compile + \" && \" + $run\n",
                b = "Invoke-Expression $invoke";
            if(libFiles != "") {
                compile += " -cp '$libs' $classes\"\n";
                run += "$libs' '" + mainClass + "'\"\n";
            } else {
                compile += " $classes\"\n";
                run += "' '" + mainClass + "'\"\n";
            }
            fUtils.writeToFile(
                    "build.ps1",
                    classes.concat(libs).concat(compile).concat(run).concat(invoke).concat(b)
            );
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void createConfigFile(String author, String sourcePath, String classPath, String mainClass) {
        try {
            String
                libFiles = this.getProjectLibFiles(),
                b = "";

            b = "Created-By: " + author + "\n" +
                "Source-Path: " + sourcePath + "\n" +
                "Class-Path: " + classPath + "\n" +
                "Libraries: " + libFiles + "\n" +
                "Main-Class: " + mainClass + "\n" +
                "Compile-Flags: ";
            fUtils.writeToFile("config.txt", b);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void createManifesto(String author, String mainClass, boolean includeLibs) {
        try {
            String libFiles = this.getProjectLibFiles();
            String sentence = "Manifest-Version: 1.0\nCreated-By: " + author + "\nMain-Class: " + mainClass + "\n";
            if(includeLibs && !libFiles.equals("")) {
                sentence += "Class-Path: " + libFiles;
            }
            fUtils.writeToFile("Manifesto.txt", sentence);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void createProjectStructure(String[] projectDirectories) {
        File lf = new File(rootFilePath);
        try {
            if(lf.listFiles() != null) {
                System.out.println("[Error] the root must be empty");
            } else {
                Arrays.asList(projectDirectories)
                    .parallelStream()
                    .map(e -> new File(e))
                    .forEach(f -> {
                        if(f.mkdir()) {
                            System.out.println(
                                    String.format("[Info] Created '%s'", f.getPath())
                            );
                        }
                    });
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
