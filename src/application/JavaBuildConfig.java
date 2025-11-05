package application;

import application.operation.Operation;

class JavaBuildConfig {
    public static void main(String[] args) {
        String rootURL = "src";
        Operation op = new Operation(rootURL);
        String source = getSubCommand("--s", args);
        String target = getSubCommand("--t", args);
        String libs = getSubCommand("--i", args);
        String flags = getSubCommand("-f", args);
        if(args.length > 0) {
            op.loadConfig();
            op.initializeENV(source, target, libs);
        }
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
                    if((i+1) < args.length) {
                        boolean conditionA = args[i+1].contains("--");
                        boolean conditionB = args[i+1].contains("-");
                        if(!conditionA && !conditionB) {
                            op.executeRunCommand("", args[i+1]);
                        } else {
                            op.executeRunCommand(args[i+1], null);
                        }
                    }
                    break;
                case "--jar":
                    if((i+1) < args.length && !args[i+1].startsWith("-")) {
                        op.executeJarCommand(args[i+1], flags, getSubCommand("-e", args));
                    } else {
                        op.executeJarCommand(null, flags, getSubCommand("-e", args));
                    }
                    break;
                case "--h":
                    System.out.println("use --compile to compile the project");
                    System.out.println("use --compile -Werror to compile the project using JVM flags");
                    System.out.println("\tuse --s to change the project source");
                    System.out.println("\tuse --t to change the project target");


                    System.out.println("use --run to execute the project");
                    System.out.println("use --run app.java to execute other main class");
                    System.out.println("\tuse --s to change the project source");
                    System.out.println("\tuse --t to change the project target");
                    System.out.println("use --run command to execute other commands like --h");

                    System.out.println("use --jar to create the jar file of the project");
                    System.out.println("\t use --jar fileName to change the jar file name");
                    System.out.println("\t use --jar -e application.App to change the main class entry point");
                    System.out.println("\t use --jar -f vn to insert flags to the JVM");
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
