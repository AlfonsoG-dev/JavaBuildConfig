package test.cases.builders;

import application.builders.RunBuilder;
import application.operation.FileOperation;

public class RunBuilderTest {
    private RunBuilder runBuilder;
    public RunBuilderTest(String root, FileOperation fileOperation) {
        runBuilder = new RunBuilder(root, fileOperation);
    }

    public void getCommandTest() {
        try {
            String command = runBuilder.getCommand("bin", null, false);
            if(command != null && command.isBlank()) {
                throw new Exception("Run command can't be empty");
            }
            System.out.println("\r\t[Info] No errors present on getCommandTest execution");
        } catch(Exception e) {
            System.err.println("\t[Error] " + e.getLocalizedMessage());
        }
    }
}
