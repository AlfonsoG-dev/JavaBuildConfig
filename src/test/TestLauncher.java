package test;

import application.utils.*;

import test.cases.utils.*;

class TestLauncher {
    public static void main(String[] args) {
        // dependencies
        String root = "src";
        FileUtils fu = new FileUtils(root);
        ExecutorUtils ex = new ExecutorUtils();

        // test text utils
        TextUtilsTest t = new TextUtilsTest();
        t.getFileLinesTest();

        // test files utils
        FileUtilsTest ft = new FileUtilsTest(fu);
        ft.createDirectoryTest();
        ft.countFilesTest();
        ft.listPathsTest();
        ft.callableListTest();

        // test executor utils
        ExecutorUtilsTest ext = new ExecutorUtilsTest(fu, ex);
        ext.getResultTest();
    }
}
