package application;

import application.operation.FileOperation;
import application.models.RunModel;
import application.builders.ScriptBuilder;

class JavaBuildConfig {
    public static void main(String[] args) {
        String rootURL = "src";
        FileOperation op = new FileOperation(rootURL);
        op.populateList(rootURL);
        RunModel cm = new RunModel(rootURL, op);
        ScriptBuilder sc = new ScriptBuilder(cm);
        System.out.println(sc.getScript("bin", false));
    }
}
