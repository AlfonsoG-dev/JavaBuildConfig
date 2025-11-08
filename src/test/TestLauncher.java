package test;

import test.cases.utils.*;

class TestLauncher {
    public static void main(String[] args) {
        // test text utils
        TextUtilsTest t = new TextUtilsTest();
        t.getFileLinesTest();

        // test files utils
        FileUtilsTest ft = new FileUtilsTest();
        ft.createDirectoryTest();
        ft.countFilesTest();
        ft.listPathsTest();
        ft.callableListTest();
    }
}
