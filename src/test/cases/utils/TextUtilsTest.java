package test.cases.utils;

import application.utils.TextUtils;
import java.io.IOException;

public class TextUtilsTest {

    private static final String DOC = "Manifesto.txt";

    public void getFileLinesTest() {
        String[] resultLines = TextUtils.getFileLines(DOC).split("\n");
        try {
            if(resultLines.length < 2) {
                throw new IOException("Manifesto must have at least 2 lines of configuration: [Main-Class, Class-Path] which are the most important ones");
            }
            for(String r: resultLines) {
                String v = r.split(":", 2)[1];
                if(v.isBlank()) {
                    throw new IOException("In Manifesto the variable can't containt empty values");
                }
            }
            IO.println("\r\t[Info] No error's on getFileLinesTest execution");
        } catch (IOException e) {
            IO.println("\t[Error] " + e.getLocalizedMessage());
        }
    }
}
