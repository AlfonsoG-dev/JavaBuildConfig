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
    public String getRunCommand(String classFiles, String libFiles, String mainClass, String runClassPath, String flag) {
        // TODO: add args that can include cli options to the run command
        String c = "";
        File f = null;
        try {
            if(runClassPath.isEmpty()) {
                System.out.println("[Error] invalid classPath, now using config values");
                c += "java -cp '" + classFiles + ";" + libFiles + "' '" + mainClass + "' " + flag;
            } else {
                c += "java -cp '" + classFiles + ";" + libFiles + "' " + runClassPath + " " + flag;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return c;
    }
    public String getCreateJarCommand(String classPath, String libFiles, String mainClass) {
        String c = "jar -cfm " + mainClass + ".jar" + " Manifesto.txt -C " + classPath + " .";
        if(libFiles != "") {
            System.out.println("[Error] Adding jar dependency not implemented");
            System.out.println("\t[Info] Lib jar dependency will be ignored");
            /**
             * TODO: implement adding lib jar dependency.
             * 1. extract or include the jar files of the dependency in the project jar build.
             * 2. make a temp file for that.
             * 3. sometimes the user may don't want to include the dependency so don't add it.
             * 4. add some flags to that process in the configuration file like:
             *    Jar-Flags: --i // to include the jar dependency.
             *    Jar-Flags: --n // to not include the dependency.
             */
        }
        return c;
    }
}
