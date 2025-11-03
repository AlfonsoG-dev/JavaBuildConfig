package application;

import application.operation.Operation;

class JavaBuildConfig {
    public static void main(String[] args) {
        String rootURL = "src";
        Operation op = new Operation(rootURL);
        String source = getSubCommand("--s", args);
        String target = getSubCommand("--t", args);
        String libs = getSubCommand("--i", args);
        op.initializeENV(source, target, libs != null ? true:false);
        for(int i=0; i<args.length; ++i) {
            switch(args[i]) {
                case "--compile":
                    if((i+1) < args.length && args[i+1].contains("-")) {
                        op.executeCompileCommand(args[i+1]);
                    } else {
                        op.executeCompileCommand(null);
                    }
                    break;
                case "--run":
                    if((i+1) < args.length && !args[i+1].startsWith("-")) {
                        op.executeRunCommand("", args[i+1]);
                    } else {
                        op.executeRunCommand("", "");
                    }
                    break;
                case "--h":
                    System.out.println("use --compile to compile the project");
                    System.out.println("use --compile -Werror to compile the project using JVM flags");
                    System.out.println("\tuse --s to change the project source");
                    System.out.println("\tuse --t to change the project target");


                    System.out.println("use --run to execute the project");
                    System.out.println("\tuse --s to change the project source");
                    System.out.println("\tuse --t to change the project target");
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
