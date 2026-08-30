package test.cases.builders;

import application.operation.FileOperation;
import java.io.IOException;
import application.builders.CompileBuilder;

public class CompileBuilderTest {

    private CompileBuilder compileBuilder;

    public CompileBuilderTest(String root,FileOperation fileOperation, String libOrder) {
        this.compileBuilder = new CompileBuilder(root, fileOperation);
    }

    public void getCommandTest() {
        try {

            String command = compileBuilder.getCommand("bin", "-Werror", libOrder);
            // check if empty or is white space
            if(command.isBlank()) {
                throw new IOException("Compile command can't be empty");
            }
            IO.println("\r\t[Info] No errors present on getCommandTest execution");
        } catch(IOException e) {
            IO.println("\t[Error] " + e.getLocalizedMessage());
        }
    }

    public void reCompileCommandTest() {
        try {

            String command = compileBuilder.reCompileCommand("bin", "bin", "-Werror", libOrder);
            // check if empty or is white space
            if(command != null && command.isBlank()) {
                throw new IOException("Compile command can't be empty");
            }
            IO.println("\r\t[Info] No errors present on reCompileCommandTest execution");
        } catch(IOException e) {
            IO.println("\t[Error] " + e.getLocalizedMessage());
        }
    }
}

