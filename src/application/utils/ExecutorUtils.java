package application.utils;

import java.io.File;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;


public class ExecutorUtils {
    private final static String LOCAL_PATH = "." + File.separator;
    private final static String OS_NAME = System.getProperty("os.name").toLowerCase();
    private List<Callable<ProcessBuilder>> pendingProcess = new ArrayList<>();

    /**
     * Executes callable tasks with new cached thread pools.
     * @param <T> the type to return when future of callable is completed.
     * @param task the callable tasks to complete.
     * @return the generic type of the result of the future of callable completion.
     */
    public<T> T getResult(Callable<T> task) {
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            Future<T> result = executor.submit(task);
            if(!result.isDone()) {
                TextUtils.message("Waiting for results...");
            }
            T value = result.get();
            if(result.isDone()) {
                TextUtils.message("Data is ready...");
            }
            return value;
        } catch(RejectedExecutionException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        } finally {
            if(executor != null) {
                executor.shutdown();
                try {
                    if(!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                        executor.shutdownNow();
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void executeCallableProcess(ExecutorService executor) {
        if(pendingProcess.isEmpty()) {
            System.out.println("[Info] All process has been finished");
            return;
        }
        try {
            List<Future<ProcessBuilder>> futureResults = executor.invokeAll(pendingProcess);
            for(Future<ProcessBuilder> f: futureResults) {
                System.out.println("[Info] Waiting for process to complete...");
                ProcessBuilder b = f.get();
                if(b != null) {
                    Process p = b.start();
                    if(p.getErrorStream() != null) {
                        TextUtils.CommandOutputError(p.getErrorStream());
                    }
                    if(p.getInputStream() != null) {
                        TextUtils.CommandOutput(p.getInputStream());
                    }
                    if(!p.waitFor(5, TimeUnit.SECONDS)) {
                        p.destroy();
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void appendCommandToCallableProcess(String command) {
        pendingProcess.add(new Callable<ProcessBuilder>() {
                @Override
                public ProcessBuilder call() {
                    System.out.println("[Info] Adding command to process...");
                    try {
                        ProcessBuilder builder = new ProcessBuilder();
                        String localFULL = new File(LOCAL_PATH).getCanonicalPath();
                        File local = new File(localFULL);
                        String lc = command;
                        if(lc == null || lc.isEmpty()) {
                            System.out.println("[Warnning] Empty command");
                            lc = "echo Happy-Day";
                        } 
                        System.out.println("[Command] " + command);
                        if(OS_NAME.contains("windows")) {
                            builder.command("pwsh", "-NoProfile", "-Command", lc);
                        } else if(OS_NAME.contains("linux")) {
                            builder.command("/bin/bash", "-c", lc);
                        }
                        builder.directory(local);
                        return builder;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            });
    }
    public void cleanPendingProcess() {
        pendingProcess = null;
    }
    /**
     * Executes the given commands and show the success or error. 
     * @param command the command to execute.
     */
    public void executeCommand(String command) {
        Process p = null;
        try {
            ProcessBuilder builder = new ProcessBuilder();
            String localFULL = new File(LOCAL_PATH).getCanonicalPath();
            File local = new File(localFULL);
            if(command == null || command.isEmpty()) {
                System.out.println("[Warnning] Empty command");
                command = "echo Happy-Day";
            } 
            System.out.println("[Command] " + command);
            if(OS_NAME.contains("windows")) {
                builder.command("pwsh", "-NoProfile", "-Command", command);
            } else if(OS_NAME.contains("linux")) {
                builder.command("/bin/bash", "-c", command);
            }
            builder.directory(local);
            p = builder.start();
            if(p.getErrorStream() != null) {
                TextUtils.CommandOutputError(p.getErrorStream());
            }
            if(p.getInputStream() != null) {
                TextUtils.CommandOutput(p.getInputStream());
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(p != null) {
                try {
                    p.waitFor();
                    p.destroy();
                } catch(Exception e) {
                    e.printStackTrace();
                }
                p = null;
            }
        }
    }
}
