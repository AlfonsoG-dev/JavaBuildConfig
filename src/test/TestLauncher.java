package test;

import application.utils.FileUtils;

import test.cases.utils.*;

class TestLauncher {
    public static void main(String[] args) {
        String root = "src";
        FileUtils fu = new FileUtils(root);
        // test text utils
        TextUtilsTest t = new TextUtilsTest();
        t.getFileLinesTest();

        // test files utils
        FileUtilsTest ft = new FileUtilsTest(fu);
        ft.createDirectoryTest();
        ft.countFilesTest();
        ft.listPathsTest();
        ft.callableListTest();
    }
}
