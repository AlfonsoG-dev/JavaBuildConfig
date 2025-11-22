package application;

import application.operation.Operation;

class JavaBuildConfig {
    public static void main(String[] args) {
        String rootURI = "src";
        Operation op = new Operation(rootURI);
        String source = getSubCommand("--s", args);
        String target = getSubCommand("--t", args);
        String libs = getSubCommand("--i", args);
        String flags = getSubCommand("--f", args);
        if(args.length > 0) {
            op.loadConfig();
            op.initializeENV(source, target, libs);
        }
        outter:for(int i=0; i<args.length; ++i) {
            switch(args[i]) {
                case "--compile":
                    if((i+1) < args.length && !args[i+1].startsWith("-")) {
                        op.appendCompileProcess(flags, args[i+1]);
                    } else {
                        op.appendCompileProcess(flags, null);
                    }
                    op.executeCommand();
                    break;
                case "--build":
                    op.appendExtractDependenciesProcess(target);
                    op.appendScratchCompileProcess(flags);
                    op.appendJarProcess(null, flags);
                    op.executeCommand();
                    break;
                case "--run":
                    if((i+1) < args.length && !args[i+1].startsWith("-")) {
                        op.appendRunProcess(flags, args[i+1]);
                    } else {
                        op.appendRunProcess(flags, null);
                    }
                    op.executeCommand();
                    break;
                case "--jar":
                    if((i+1) < args.length && !args[i+1].startsWith("-")) {
                        op.appendJarProcess(args[i+1], flags);
                    } else {
                        op.appendJarProcess(null, flags);
                    }
                    op.executeCommand();
                    break;
                case "--add":
                    if ((i+2) < args.length && !args[i+1].startsWith("-") && !args[i+2].startsWith("-")) {
                         op.copyToPath(args[i+1], args[i+2]);
                    } else if((i+1) < args.length && !args[i+1].startsWith("-")) {
                        op.copyToPath(args[i+1], null); 
                    }
                    break;
                case "--extract":
                    op.appendExtractDependenciesProcess(target);
                    op.executeCommand();
                    break;
                case "--config":
                    if((i+1) < args.length && !args[i+1].startsWith("-")) {
                        op.setConfig(args[i+1], getSubCommand("-a", args));
                    } else {
                        op.setConfig(null, getSubCommand("-a", args));
                    }
                    break;
                case "--test":
                    if((i+1) < args.length && !args[i+1].startsWith("-")) {
                        op.appendCompileProcess(flags, target);
                        op.appendTestProcess(args[i+1]);
                    } else {
                        op.appendCompileProcess(flags, target);
                        op.appendTestProcess(null);
                    }
                    op.executeCommand();
                    break;
                case "--script":
                    if((i+1) < args.length && !args[i+1].startsWith("-")) {
                        op.createBuildScript(args[i+1]);
                    } else {
                        op.createBuildScript(null);
                    }
                    break;
                case "--h":
                    System.out.println();
                    System.out.println("All the sub-commands bellow can be used by any command");
                    System.out.println("use --s to change the project source");
                    System.out.println("use --s to change the project source");
                    System.out.println("use --t to change the project target");
                    System.out.println("use -f to insert flags to the compile process like -f -g");
                    System.out.println();
    
                    System.out.println("use --compile to compile the project");
                    System.out.println("use --compile classpath to compile the project and store the .class files in that place");
                    System.out.println("use --compile -f -Werror to compile the project using JVM flags");
    
                    System.out.println("use --build to build the entire project");
                    System.out.println("\t use -e application.App to change the main class entry point if manifesto isn't present");
    
                    System.out.println("use --run to execute the project");
                    System.out.println("use --run app.java to execute other main class");
                    System.out.println("use --run command to execute other commands like --h");
    
                    System.out.println("use --jar to create the jar file of the project");
                    System.out.println("\t use --jar fileName to change the jar file name");
                    System.out.println("\t use -e application.App to change the main class entry point if manifesto isn't present");
                    System.out.println("\t use --jar -f vn to insert flags to the JVM");
    
                    System.out.println("use --config to create the project config");
                    System.out.println("use --config app to change the main class for the config");
                    System.out.println("use --config app -a name-person to change the main author for the manifesto");
                    System.out.println("use --config --i include/exclude lib dependencies");
    
                    System.out.println("use --test to run tests");
                    System.out.println("\tuse --test app.name to run another main class of tests");
                    // terminate the program because default never reach
                    op.terminateProgram();
                    break outter;
                default:
                    System.out.println("Use --h to see the commands");
                    op.terminateProgram();
                    break outter;
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
