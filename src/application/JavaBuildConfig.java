package application;

import application.operation.Operation;

class JavaBuildConfig {
    public static void main(String[] args) {
        String rootURL = "src";
        Operation op = new Operation(rootURL);
        op.initializeENV("src\\application", "bin", false);
        op.createBuildScript("app.ps1");
    }
}
