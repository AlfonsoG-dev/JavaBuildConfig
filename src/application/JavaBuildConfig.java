package application;

import java.io.File;

import application.operation.FileOperation;

class JavaBuildConfig {
    public static void main(String[] args) {
        String pathURL = "." + File.separator;
        FileOperation op = new FileOperation(pathURL);
        op.populateList(pathURL + "src");
        op.sourceDirs()
            .stream()
            .forEach(System.out::println);
    }
}
