package test.cases.utils;

import application.utils.TextUtils;

public class TextUtilsTest {

    private static final String DOC = "Manifesto.txt";

    public void getFileLinesTest() {
        String[] resultLines = TextUtils.getFileLines(DOC).split("\n");
        try {
            if(resultLines.length < 2) {
                throw new Exception("Manifesto must have at least 2 lines of configuration: [Main-Class, Class-Path] which are the most important ones");
            }
            for(String r: resultLines) {
                String v = r.split(":", 2)[1];
                if(v.isBlank()) {
                    throw new Exception("In Manifesto the variable can't containt empty values");
                }
            }
            System.out.println("\r\t[Info] No error's on getFileLinesTest execution");
        } catch (Exception e) {
            System.err.println("\t[Error] " + e.getLocalizedMessage());
        }
    }
}
