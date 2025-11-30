package test;

import application.utils.*;
import application.operation.FileOperation;

import test.cases.utils.*;
import test.cases.operation.*;
import test.cases.builders.*;

import java.io.File;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

class TestLauncher {
    private static final String DEFAULT_LIB_CONFIG = "ignore";
    private static final int THREAD_COUNT = 1; 
    private static final ExecutorService EXECUTOR_THREAD = Executors.newFixedThreadPool(THREAD_COUNT);
    public static void main(String[] args) {
        // dependencies
        String root = "src";
        FileUtils fu = new FileUtils(root);
        ExecutorUtils ex = new ExecutorUtils();
        FileOperation fop = new FileOperation(root, ex);

        // start env variables
        fop.appendSource("src" + File.separator + "application");
        fop.appendLists();

        // Don't include lib files for now
        fop.populateList(ex.getListsResult(EXECUTOR_THREAD), DEFAULT_LIB_CONFIG);

        // test text utils
        TextUtils.showMessage("\nTesting TextUtils\b");
        TextUtilsTest t = new TextUtilsTest();
        t.getFileLinesTest();

        // test files utils
        TextUtils.showMessage("\nTesting FileUtils\b");
        FileUtilsTest ft = new FileUtilsTest(fu);
        ft.createDirectoryTest();
        ft.countFilesTest();
        ft.listPathsTest();
        ft.callableListTest();

        // test executor utils
        TextUtils.showMessage("\nTesting ExecutorUtils\b");
        ExecutorUtilsTest ext = new ExecutorUtilsTest(fu, ex);
        ext.getResultTest();

        // test fileOperationTest
        TextUtils.showMessage("\nTesting FileOperation\b");
        FileOperationTest fopt = new FileOperationTest(fop);
        fopt.sourceFilesTest();
        fopt.getMainClassTest();
        fopt.getProjectNameTest();
        fopt.sourceDirsTest();

        // test CompileBuilder
        TextUtils.showMessage("\nTesting CompileBuilder\b");
        CompileBuilderTest cbt = new CompileBuilderTest(root, fop);
        cbt.getCommandTest();
        cbt.reCompileCommandTest();

        // test runBuilder
        TextUtils.showMessage("\nTesting RunBuilder\b");
        RunBuilderTest rbt = new RunBuilderTest(root, fop);
        rbt.getCommandTest();
        try {
            EXECUTOR_THREAD.shutdown();
            if(!EXECUTOR_THREAD.awaitTermination(5, TimeUnit.SECONDS)) {
                EXECUTOR_THREAD.shutdownNow();
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
