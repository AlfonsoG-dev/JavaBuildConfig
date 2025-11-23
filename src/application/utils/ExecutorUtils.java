package application.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;


public class ExecutorUtils {
    private static final String LOCAL_PATH = "." + File.separator;
    private static final boolean OS_NAME_WINDOWS = System.getProperty("os.name").contains("Windows");
    private List<Callable<ProcessBuilder>> pendingProcess = new ArrayList<>();
    private List<Map<String, Callable<List<Path>>>> pendingLists = new ArrayList<>();

    /**
     * Executes callable tasks with new cached thread pools.
     * @param <T> the type to return when future of callable is completed.
     * @param task the callable tasks to complete.
     * @return the generic type of the result of the future of callable completion.
     */
    public<T> T getResult(Callable<T> task) {
        try(ExecutorService executor = Executors.newCachedThreadPool()) {
            Future<T> result = executor.submit(task);
            if(!result.isDone()) {
                TextUtils.showMessage("Waiting for results...");
            }
            T value = result.get();
            if(result.isDone()) {
                TextUtils.message("Data is ready...");
            }
            return value;
        } catch(RejectedExecutionException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return null;
    }
    public Map<String, List<Path>> getListsResult(ExecutorService executor) {
        HashMap<String, List<Path>> completeResult = new HashMap<>();
        try {
            if(pendingLists.isEmpty()) return completeResult; 
            for(Map<String, Callable<List<Path>>> pending: pendingLists) {
                Set<String> keys = pending.keySet();
                for(String k: keys) {
                    Future<List<Path>> futureResult = executor.submit(pending.get(k));
                    if(!futureResult.isDone()) {
                        TextUtils.showMessage("Waiting for list results...");
                    }
                    List<Path> value = futureResult.get();
                    if(futureResult.isDone()) {
                        TextUtils.message("Populated " + k + " list");
                        completeResult.put(k, value);
                    }
                }
            }
        } catch(RejectedExecutionException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return completeResult;
    }
    /**
     * Execute pending process with appended commands.
     * @param executor the type of executor.
     */
    public void executeCallableProcess(ExecutorService executor) {
        if(pendingProcess.isEmpty()) {
            TextUtils.message("[Info] All process has been finished");
            return;
        }
        try {
            List<Future<ProcessBuilder>> futureResults = executor.invokeAll(pendingProcess);
            for(Future<ProcessBuilder> f: futureResults) {
                TextUtils.showMessage("Waiting for process to complete...");
                ProcessBuilder b = f.get();
                if(b != null) {
                    Process p = b.start();
                    if(p.getErrorStream() != null) {
                        TextUtils.commandOutputError(p.getErrorStream());
                    }
                    if(p.getInputStream() != null) {
                        TextUtils.commandOutput(p.getInputStream());
                    }
                    if(!p.waitFor(5, TimeUnit.SECONDS)) {
                        p.destroy();
                    }
                }
            }
        } catch(IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
    public void appendCommandToCallableProcess(String command) {
        pendingProcess.add(() -> {
                    TextUtils.showMessage("Adding command to process...");
                    try {
                        ProcessBuilder builder = new ProcessBuilder();
                        String localFULL = new File(LOCAL_PATH).getCanonicalPath();
                        File local = new File(localFULL);
                        String lc = command;
                        if(lc == null) {
                            TextUtils.warning("Empty command");
                            lc = "echo Happy-Day";
                        } 
                        TextUtils.showMessage("[Command] " + command);
                        if(OS_NAME_WINDOWS) {
                            builder.command("pwsh", "-NoProfile", "-Command", lc);
                        } else {
                            builder.command("/bin/bash", "-c", lc);
                        }
                        builder.directory(local);
                        return builder;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                });
    }
    public void appendListToCallableProcess(Map<String, Callable<List<Path>>> lists) {
        pendingLists.add(lists);
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
                TextUtils.warning("Empty command");
                command = "echo Happy-Day";
            } 
            TextUtils.showMessage("[Command] " + command);
            if(OS_NAME_WINDOWS) {
                builder.command("pwsh", "-NoProfile", "-Command", command);
            } else {
                builder.command("/bin/bash", "-c", command);
            }
            builder.directory(local);
            p = builder.start();
            if(p.getErrorStream() != null) {
                TextUtils.commandOutputError(p.getErrorStream());
            }
            if(p.getInputStream() != null) {
                TextUtils.commandOutput(p.getInputStream());
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(p != null) {
                try {
                    p.waitFor();
                    p.destroy();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                p = null;
            }
        }
    }
}
