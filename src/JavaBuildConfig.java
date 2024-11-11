import Application.Operations.CommandOperations;
class JavaBuildConfig {
    public static void main(String[] args) {
        CommandOperations cp = new CommandOperations("./");
        for(int i=0; i<args.length; ++i) {
            switch(args[i]) {
                case "--compile":
                    System.out.println("[cmd] " + cp.compile());
                    break;
                case "--run":
                    System.out.println("[cmd] " + cp.run());
                    break;
                case "--jar":
                    System.out.println("[cmd] " + cp.createJar());
                    break;
                case "--build":
                    System.out.println("[cmd] " + cp.compile());
                    System.out.println("[cmd] " + cp.createJar());
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
