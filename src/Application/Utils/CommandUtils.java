package Application.Utils;

import java.io.File;

public class CommandUtils {
    private String rootPath;
    public CommandUtils(String rootPath) {
        this.rootPath = rootPath;
    }
    public String getCompileCommand(String flags, String javaFiles, String classFiles, String libFiles) {
        String b = "javac " + flags + " -d " + classFiles;
        if(libFiles != "") {
            b += " -cp '" + libFiles + "'";
        }
        b += " " + javaFiles;
        return b;
    }
    public String getRunCommand(String classFiles, String libFiles, String runClassPath, String mainClass) {
        // TODO: add args that can include cli options to the run command
        String c = "";
        File f = null;
        try {
            f = new File(runClassPath);
            if(!f.exists() || runClassPath.equals("")) {
                System.out.println("[Error] invalid classPath, now using config values");
                c += "java -cp '" + classFiles + ";" + libFiles + "' '" + mainClass + "'";
            } else {
                c += "java -cp '" + classFiles + ";" + libFiles + "' " + runClassPath;
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
