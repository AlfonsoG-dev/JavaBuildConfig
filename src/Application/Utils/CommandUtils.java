package Application.Utils;

public class CommandUtils {
    public String getCompileCommand(String javaFiles, String classFiles, String libFiles) {
        String b = "javac -d " + classFiles;
        if(libFiles != "") {
            b += " -cp '" + libFiles + "'";
        }
        b += " " + javaFiles;
        return b;
    }
}
