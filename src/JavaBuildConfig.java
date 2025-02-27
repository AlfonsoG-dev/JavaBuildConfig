import Application.Operations.CommandOperations;
import Application.Execution;
class JavaBuildConfig {
    public static void main(String[] args) {
        CommandOperations cp = new CommandOperations("./");
        Execution ex = new Execution("./");
        for(int i=0; i<args.length; ++i) {
            switch(args[i]) {
                case "--compile":
                    System.out.println("[Info] compiling...");
                    ex.executeCommand(cp.compile());
                    break;
                case "--run":
                    System.out.println("[Info] running...");
                    String
                        c = (i+1) < args.length ? args[i+1] : "",
                        f = (i+2) < args.length ? args[i+2] : "";
                    ex.executeCommand(cp.run(c, f));
                    break;
                case "--jar":
                    ex.executeCommand(cp.createJar());
                    break;
                case "--build":
                    cp.createManifesto(true);
                    ex.executeCommand(cp.compile());
                    ex.executeCommand(cp.createJar());
                    System.out.println("[cmd] building...");
                    break;
                case "-cb":
                    System.out.println("[Info] creating build script in root...");
                    cp.createBuildScript();
                    break;
                case "-cs":
                    System.out.println("[Info] creating project structure...");
                    cp.createProjectStructure();
                    break;
                case "-cm":
                    System.out.println("[Info] creating manifesto...");
                    cp.createManifesto(true);
                    break;
                case "-cc":
                    System.out.println("[Info] creating configuration file...");
                    cp.createConfigFile();
                    break;
                default:
                    System.out.println("\n[Info] use --h or --help to show command options");
                    break;
            }
        }
        if(args.length > 0 && (args[0].equals("--h") ||args[0].equals("--help"))) {
            messages();
        }
    }
    private static void messages() {
        System.out.println("[Info] use --compile to compile the project");
        System.out.println("[Info] use --run to execute the project");
        System.out.println("[Info] use --jar to create the project jar compress file");
        System.out.println("[Info] use -cb to create the build script");
        System.out.println("[Info] use -cs to create the project structure");
        System.out.println("[Info] use -cm to create the Manifesto file");
    }
}
