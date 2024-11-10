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
            }
        }
        System.out.println("\n[Info] use --h or --help to show command options");
        if(args.length > 0 && (args[0].equals("--h") ||args[0].equals("--help"))) {
            messages();
        }
    }
    private static void messages() {
        System.out.println("[Info] use --compile to compile the project");
        System.out.println("[Info] use --run to execute the project");
    }
}
