package application.utils;

import java.io.File;

import java.lang.ProcessBuilder;
import java.lang.Process;

import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;

import java.lang.InterruptedException;

public record ExecutorUtils() {
    private final static String LOCAL_PATH = "." + File.separator;

    public<T> T getResult(Callable<T> task) {
        T value = null;
        try(ExecutorService ex = Executors.newCachedThreadPool()) {
            Future<T> result = ex.submit(task);
            if(result.state() == Future.State.RUNNING) {
                TextUtils.message("Waiting for results...");
            }
            value = result.get();
        } catch(RejectedExecutionException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return value;
    }
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
            if(System.getProperty("os.name").toLowerCase().contains("windows")) {
                builder.command("pwsh", "-NoProfile", "-Command", command);
            } else if(System.getProperty("os.name").toLowerCase().contains("linux")) {
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
