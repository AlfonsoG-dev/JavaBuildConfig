package application;

import application.operation.Operation;

class JavaBuildConfig {
    public static void main(String[] args) {
        String rootURL = "src";
        Operation op = new Operation(rootURL);
        String source = getSubCommand("-s", args);
        String target = getSubCommand("-t", args);
        String libs = getSubCommand("--i", args);
        op.initializeENV(source, target, libs != null ? true:false);
        for(int i=0; i<args.length; ++i) {
            switch(args[i]) {
                case "--compile":
                    op.executeCompileCommand(null);
                    break;
                case "--h":
                    System.out.println("use --compile to compile the project");
                    break;
            }
        }
    }
    private static String getSubCommand(String prefix, String[] args) {
        for(int i=0; i<args.length; ++i) {
            if(args[i].equals(prefix) && (i+1) < args.length) {
                return args[i+1];
            }
        }
        return null;
    }
}
