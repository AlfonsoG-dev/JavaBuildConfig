package Application.Operations;

import java.io.File;

import java.util.ArrayList;
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
                // TODO: create the config with default values
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
                String[] sentences = configSentences[i].split(":");
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
