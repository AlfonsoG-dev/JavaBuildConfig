package application;

import java.io.File;

import application.operation.FileOperation;
import application.models.CompileModel;

class JavaBuildConfig {
    public static void main(String[] args) {
        String rootURL = "." + File.separator;
        FileOperation op = new FileOperation(rootURL);
        op.populateList(rootURL + "src");
        CompileModel cm = new CompileModel(rootURL, op);
        String command = cm.getCommand("bin", "", false);
        System.out.println(command);
    }
}
