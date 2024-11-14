package Application.Utils;

import java.io.File;

public class CommandUtils {
    private String rootPath;
    public CommandUtils(String rootPath) {
        this.rootPath = rootPath;
    }
    public String getCompileCommand(String javaFiles, String classFiles, String libFiles) {
        String b = "javac -d " + classFiles;
        if(libFiles != "") {
            b += " -cp '" + libFiles + "'";
        }
        b += " " + javaFiles;
        return b;
    }
    public String getRunCommand(String classFiles, String libFiles, String mainClass) {
        // TODO: add args that can include cli options to the run command
        String c = "";
        File f = null;
        try {
            f = new File(rootPath + mainClass + ".jar");
            if(!f.exists()) {
                c = "java -cp '" + classFiles + ";" + libFiles + "' '" + mainClass + "'";
            } else {
                c = "java -jar " + mainClass + ".jar ";
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return c;
    }
    public String getCreateJarCommand(String classPath, String libFiles, String mainClass) {
        String c = "jar -cfm " + mainClass + ".jar" + " Manifesto.txt -C " + classPath + " .";
        if(libFiles != "") {
            c += " -C " + libFiles + " .";
        }
        return c;
    }
}
