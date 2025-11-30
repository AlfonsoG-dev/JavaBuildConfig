package test.cases.builders;

import application.operation.FileOperation;

import application.builders.CompileBuilder;

public class CompileBuilderTest {

    private CompileBuilder compileBuilder;

    public CompileBuilderTest(String root,FileOperation fileOperation) {
        this.compileBuilder = new CompileBuilder(root, fileOperation);
    }

    public void getCommandTest() {
        try {

            String command = compileBuilder.getCommand("bin", "-Werror", "ignore");
            // check if empty or is white space
            if(command.isBlank()) {
                throw new Exception("Compile command can't be empty");
            }
            System.out.println("\r\t[Info] No errors present on getCommandTest execution");
        } catch(Exception e) {
            System.err.println("\t[Error] " + e.getLocalizedMessage());
        }
    }

    public void reCompileCommandTest() {
        try {

            String command = compileBuilder.reCompileCommand("bin", "bin", "-Werror", "exclude");
            // check if empty or is white space
            if(command != null && command.isBlank()) {
                throw new Exception("Compile command can't be empty");
            }
            System.out.println("\r\t[Info] No errors present on reCompileCommandTest execution");
        } catch(Exception e) {
            System.err.println("\t[Error] " + e.getLocalizedMessage());
        }
    }
}

