package test.cases.operation;

import application.operation.FileOperation;

import java.util.List;
import java.io.IOException;
import java.nio.file.Path;

public class FileOperationTest {
    private FileOperation fileOperation;
    private static final String[] LOG_LEVEL = {
        "Error",
        "Info"
    };
    private static final String LOG_FORMAT = "[%s] %s";

    public FileOperationTest(FileOperation fileOperation) {
        this.fileOperation = fileOperation;
    }
    public void sourceFilesTest() {
        try {
            List<Path> files = fileOperation.sourceFiles();
            if(files.isEmpty()) {
                throw new IOException("The path has at least 1 file in it.");
            }
            IO.println(String.format(LOG_FORMAT, "\r\t" + LOG_LEVEL[0],  "No errors present on sourceFilesTest execution"));
        } catch(IOException e) {
            IO.println(String.format(LOG_FORMAT, "\t" + LOG_LEVEL[1], e.getLocalizedMessage()));
        }
    }
    public void getMainClassTest() {
        try {
            String mainClass = "application.JavaBuildConfig";
            if(!fileOperation.getMainClass().equals(mainClass)) {
                throw new IOException("The main class doesn't match.");
            }
            IO.println(String.format(LOG_FORMAT,"\r\t" + LOG_LEVEL[0], "No errors present on getMainClassTest execution"));
        } catch(Exception e) {
            IO.println(String.format(LOG_FORMAT, "\t" + LOG_LEVEL[1], e.getLocalizedMessage()));
        }
    }

    public void getProjectNameTest() {
        try {
            String projectName = "JavaBuildConfig";
            if(!fileOperation.getProjectName().equals(projectName)) {
                throw new IOException("The project name doesn't match");
            }
            IO.println(String.format(LOG_FORMAT, "\r\t" + LOG_LEVEL[0] , "No errors present on getProjectNameTest execution"));
        } catch(IOException e) {
            IO.println(String.format(LOG_FORMAT, "\t"+LOG_LEVEL[1], e.getLocalizedMessage()));
        }
    }
    public void sourceDirsTest() {
        try {
            List<Path> files = fileOperation.sourceDirs();
            if(files.isEmpty()) {
                throw new IOException("The path has at least 1 file in it.");
            }
            IO.println(String.format(LOG_FORMAT, "\r\t" +LOG_LEVEL[0] , "No errors present on sourceDirsTest execution"));
        } catch(IOException e) {
            IO.println(String.format(LOG_FORMAT, "\t"+ LOG_LEVEL[1],  e.getLocalizedMessage()));
        }
    }
}
