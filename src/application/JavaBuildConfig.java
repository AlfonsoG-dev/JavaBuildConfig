package application;

import java.io.File;

import application.operation.FileOperation;
import application.models.RunModel;

class JavaBuildConfig {
    public static void main(String[] args) {
        String rootURL = "." + File.separator;
        FileOperation op = new FileOperation(rootURL);
        op.populateList(rootURL + "src");
        RunModel cm = new RunModel(rootURL, op);
        String command = cm.getCommand("bin", "", false);
        System.out.println(command);
    }
}
