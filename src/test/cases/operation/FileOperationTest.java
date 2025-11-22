package test.cases.operation;

import application.operation.FileOperation;

import java.util.List;

import java.nio.file.Path;

public class FileOperationTest {
    private FileOperation fileOperation;

    public FileOperationTest(FileOperation fileOperation) {
        this.fileOperation = fileOperation;
    }
    public void sourceFilesTest() {
        try {
            List<Path> files = fileOperation.sourceFiles();
            if(files.isEmpty()) {
                throw new Exception("The path has at least 1 file in it.");
            }
            System.out.println("\r\t[Info] No errors present on sourceFilesTest execution");
        } catch(Exception e) {
            System.err.println("\t[Error] " + e.getLocalizedMessage());
        }
    }
    public void getMainClassTest() {
        try {
            String mainClass = "application.JavaBuildConfig";
            if(!fileOperation.getMainClass().equals(mainClass)) {
                throw new Exception("The main class doesn't match.");
            }
            System.out.println("\r\t[Info] No errors present on getMainClassTest execution");
        } catch(Exception e) {
            System.err.println("\t[Error] " + e.getLocalizedMessage());
        }
    }

    public void getProjectNameTest() {
        try {
            String projectName = "JavaBuildConfig";
            if(!fileOperation.getProjectName().equals(projectName)) {
                throw new Exception("The project name doesn't match");
            }
            System.out.println("\r\t[Info] No errors present on getProjectNameTest execution");
        } catch(Exception e) {
            System.err.println("\t[Error] " + e.getLocalizedMessage());
        }
    }
    public void sourceDirsTest() {
        try {
            List<Path> files = fileOperation.sourceDirs();
            if(files.isEmpty()) {
                throw new Exception("The path has at least 1 file in it.");
            }
            System.out.println("\r\t[Info] No errors present on sourceDirsTest execution");
        } catch(Exception e) {
            System.err.println("\t[Error] " + e.getLocalizedMessage());
        }
    }
}
