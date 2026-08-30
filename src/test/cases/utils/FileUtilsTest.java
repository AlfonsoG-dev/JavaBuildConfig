package test.cases.utils;

import module java.base;

import application.utils.FileUtils;

public class FileUtilsTest {

    private List<Path> list;
    private FileUtils fileUtils;

    public FileUtilsTest(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    public void createDirectoryTest() {
        try {
            String directory = "docs";
            // if the directory already exists returns true
            if(!fileUtils.createDirectory(directory)) {
                throw new Exception("Create directory fails to create on " + directory + " path.");
            }
            IO.println("\r\t[Info] No errors present on createDirectoryTest execution");
        } catch(Exception e) {
            IO.println("\t[Error] " + e.getLocalizedMessage());
        }
    }
    public void countFilesTest() {
        try {
            String directory = "bin" + File.separator + "application" + File.separator + "models";
            if(fileUtils.countFiles(Paths.get(directory)) <= 0) {
                throw new Exception("The directory " + directory + " has at least 1 file in it.");
            }
            IO.println("\r\t[Info] No errors present on countFilesTest execution");
        } catch (Exception e) {
            IO.println("\t[Error] " + e.getLocalizedMessage());
        }
    }

    public void listPathsTest() {
        try {
            String pathURI = "bin" + File.separator + "application" + File.separator + "operation";
            list = fileUtils.listPaths(pathURI, 2);
            if(list.isEmpty()) {
                throw new Exception("The path " + pathURI + " has at least 1 file in it.");
            }
            IO.println("\r\t[Info] No errors present on listPathsTest execution");
        } catch (Exception e) {
            IO.println("\t[Error] " + e.getLocalizedMessage());
        }
    }
    public void callableListTest() {
        String pathURI = "bin" + File.separator + "application" + File.separator + "operation";
        Future<List<Path>> results = null;
        try (ExecutorService ex = Executors.newFixedThreadPool(1)) {
            results = ex.submit(fileUtils.callableList(pathURI, 2));
            list = results.get();
            if(list.isEmpty()) {
                throw new Exception("The path " + pathURI + " has at least 1 file in it.");
            }
            IO.println("\r\t[Info] No errors present on callableListTest execution");
        } catch(RejectedExecutionException | InterruptedException | ExecutionException e) {
            IO.println("\t[Error] on execution of task " + e.getLocalizedMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            IO.println("\t[Error] " + e.getLocalizedMessage());
        }
    }
}
