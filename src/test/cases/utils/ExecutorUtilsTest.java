package test.cases.utils;

import application.utils.*;

import java.util.List;

import java.io.File;

import java.nio.file.Path;

public class ExecutorUtilsTest {
    private FileUtils fileUtils;
    private ExecutorUtils executorUtils;

    public ExecutorUtilsTest(FileUtils fileUtils, ExecutorUtils executorUtils) {
        this.fileUtils = fileUtils;
        this.executorUtils = executorUtils;
    }

    public void getResultTest() {
        try {
            String pathURI = "bin" + File.separator + "application" + File.separator + "operation";
            List<Path> files = executorUtils.getResult(fileUtils.callableList(pathURI, 2));
            if(files.size() <= 0) {
                throw new Exception("The path " + pathURI + " has at least 1 file in it.");
            }
            System.out.println("\r\t[Info] No errors present on getResultTest execution");
        } catch(Exception e) {
            System.err.println("\t[Error] " + e.getLocalizedMessage());
        }
    }
}
