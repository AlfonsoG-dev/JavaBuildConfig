package test.cases.utils;

import application.utils.FileUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.io.File;

import java.nio.file.Path;
import java.nio.file.Paths;

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
            System.out.println("\r\t[Info] No errors present on createDirectoryTest execution");
        } catch(Exception e) {
            System.err.println("\t[Error] " + e.getLocalizedMessage());
        }
    }
    public void countFilesTest() {
        try {
            String directory = "bin" + File.separator + "application" + File.separator + "models";
            if(fileUtils.countFiles(Paths.get(directory)) <= 0) {
                throw new Exception("The directory " + directory + " has at least 1 file in it.");
            }
            System.out.println("\r\t[Info] No errors present on countFilesTest execution");
        } catch (Exception e) {
            System.err.println("\t[Error] " + e.getLocalizedMessage());
        }
    }

    public void listPathsTest() {
        try {
            String pathURI = "bin" + File.separator + "application" + File.separator + "operation";
            list = fileUtils.listPaths(pathURI, 2);
            if(list.isEmpty()) {
                throw new Exception("The path " + pathURI + " has at least 1 file in it.");
            }
            System.out.println("\r\t[Info] No errors present on listPathsTest execution");
        } catch (Exception e) {
            System.err.println("\t[Error] " + e.getLocalizedMessage());
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
            System.out.println("\r\t[Info] No errors present on callableListTest execution");
        } catch(RejectedExecutionException | InterruptedException | ExecutionException e) {
            System.err.println("\t[Error] on execution of task " + e.getLocalizedMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("\t[Error] " + e.getLocalizedMessage());
        }
    }
}
