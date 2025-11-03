package application;

import application.operation.FileOperation;
import application.models.RunModel;

class JavaBuildConfig {
    public static void main(String[] args) {
        String rootURL = "src";
        FileOperation op = new FileOperation(rootURL);
        op.populateList(rootURL);
        RunModel cm = new RunModel(rootURL, op);
        String command = cm.getCommand("src\\test\\TestLauncher.java", "bin", "", false);
        System.out.println(command);
    }
}
