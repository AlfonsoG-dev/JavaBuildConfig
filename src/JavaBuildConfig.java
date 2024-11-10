import Application.Operations.CommandOperations;
class JavaBuildConfig {
    public static void main(String[] args) {
        CommandOperations cp = new CommandOperations("./");
        for(int i=0; i<args.length; ++i) {
            switch(args[i]) {
                case "--compile":
                    System.out.println("[cmd] " + cp.compile());
                    break;
                case "--h":
                    messages();
                    break;
                case "--help":
                    messages();
                    break;
                default:
                    System.out.println("[Info] use --h or help to show help");
                    break;
            }
        }
    }
    private static void messages() {
        System.out.println("[Info] use --compile to compile the project");
    }
}
