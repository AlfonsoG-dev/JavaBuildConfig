import Application.Operations.CommandOperations;
import Application.Execution;
class JavaBuildConfig {
    public static void main(String[] args) {
        CommandOperations cp = new CommandOperations("./");
        Execution ex = new Execution("./");
        for(int i=0; i<args.length; ++i) {
            switch(args[i]) {
                case "--compile":
                    ex.executeCommand(cp.compile());
                    break;
                case "--run":
                    ex.executeCommand(cp.run());
                    break;
                case "--jar":
                    ex.executeCommand(cp.createJar());
                    break;
                case "--build":
                    ex.executeCommand(cp.compile());
                    ex.executeCommand(cp.createJar());
                    System.out.println("[cmd] build complete");
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
    }
}
