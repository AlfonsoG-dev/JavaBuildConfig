package test.cases.utils;

import module java.base;

import application.utils.*;


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
            if(files.isEmpty()) {
                throw new IOException("The path " + pathURI + " has at least 1 file in it.");
            }
            IO.println("\r\t[Info] No errors present on getResultTest execution");
        } catch(IOException e) {
            IO.println("\t[Error] " + e.getLocalizedMessage());
        }
    }
}
