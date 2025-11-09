package test;

import application.utils.*;
import application.operation.FileOperation;

import test.cases.utils.*;
import test.cases.operation.*;
import test.cases.builders.*;

import java.io.File;

class TestLauncher {
    public static void main(String[] args) {
        // dependencies
        String root = "src";
        FileUtils fu = new FileUtils(root);
        ExecutorUtils ex = new ExecutorUtils();
        FileOperation fop = new FileOperation(root, ex);

        // start env variables
        fop.populateList("src" + File.separator + "application");

        // test text utils
        System.out.println("\nTesting TextUtils\b");
        TextUtilsTest t = new TextUtilsTest();
        t.getFileLinesTest();

        // test files utils
        System.out.println("\nTesting FileUtils\b");
        FileUtilsTest ft = new FileUtilsTest(fu);
        ft.createDirectoryTest();
        ft.countFilesTest();
        ft.listPathsTest();
        ft.callableListTest();

        // test executor utils
        System.out.println("\nTesting ExecutorUtils\b");
        ExecutorUtilsTest ext = new ExecutorUtilsTest(fu, ex);
        ext.getResultTest();

        // test fileOperationTest
        System.out.println("\nTesting FileOperation\b");
        FileOperationTest fopt = new FileOperationTest(fop);
        fopt.sourceFilesTest();
        fopt.getMainClassTest();
        fopt.getProjectNameTest();
        fopt.sourceDirsTest();

        // test CompileBuilder
        System.out.println("\nTesting CompileBuilder\b");
        CompileBuilderTest cbt = new CompileBuilderTest(root, fop);
        cbt.getCommandTest();
        cbt.reCompileCommandTest();

        // test runBuilder
        System.out.println("\nTesting RunBuilder\b");
        RunBuilderTest rbt = new RunBuilderTest(root, fop);
        rbt.getCommandTest();
    }
}
