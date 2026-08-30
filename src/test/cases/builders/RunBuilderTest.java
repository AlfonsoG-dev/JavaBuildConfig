package test.cases.builders;

import application.builders.RunBuilder;
import application.operation.FileOperation;
import java.io.IOException;

public class RunBuilderTest {
    private RunBuilder runBuilder;
    public RunBuilderTest(String root, FileOperation fileOperation) {
        runBuilder = new RunBuilder(root, fileOperation);
    }

    public void getCommandTest() {
        try {
            String command = runBuilder.getCommand("bin", null, "ignore");
            if(command != null && command.isBlank()) {
                throw new IOException("Run command can't be empty");
            }
            IO.println("\r\t[Info] No errors present on getCommandTest execution");
        } catch(IOException e) {
            IO.println("\t[Error] " + e.getLocalizedMessage());
        }
    }
}
