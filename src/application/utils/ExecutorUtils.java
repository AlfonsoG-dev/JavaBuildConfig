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
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } catch(RejectedExecutionException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Executes a batch of callable lists and appends them in a map to easily access.
     * @param executor - the type of thread to execute the callable list of tasks.
     * @return a populated map with the result of the callable tasks.
     */
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
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } catch(RejectedExecutionException | ExecutionException e) {
            e.printStackTrace();
        }
        return completeResult;
    }
    /**
     * Execute pending process with commands to execute.
     * @param executor the type of executor.
     */
    public void executeCallableProcess(ExecutorService executor) {
        if(pendingProcess.isEmpty()) {
            TextUtils.message("All process has been finished");
            return;
        }

        try(ExecutorService streamExecutor = Executors.newCachedThreadPool()) {
            // Submit all callables in parallel
            List<Future<ProcessBuilder>> futures = new ArrayList<>();
            for (Callable<ProcessBuilder> c : pendingProcess) {
                futures.add(executor.submit(c));
            }

            // Wait results and execute processes
            for (Future<ProcessBuilder> future : futures) {
                TextUtils.showMessage("Waiting for results...");

                ProcessBuilder builder = future.get(); // wait for callable

                if (builder == null) {
                    TextUtils.warning("Null ProcessBuilder returned, skipping...");
                    continue;
                }

                Process p = builder.start();
                handleProcessStreams(p, streamExecutor);

                boolean finished = p.waitFor(15, TimeUnit.SECONDS);
                if (!finished) {
                    TextUtils.warning("Process timeout →  killing process.");
                    p.destroyForcibly();
                }

                int exitCode = p.exitValue();
                TextUtils.message("Exit code: " + exitCode);
            }

        } catch(InterruptedException e) { 
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } catch (IOException | ExecutionException e) {
            e.printStackTrace();
        }
    }
    /**
     * A method to append the command to the pending list of process to compute.
     * @param command - the command to append to the pending process lists.
     */
    public void appendCommandToCallableProcess(String command) {
        pendingProcess.add(() -> {
                    TextUtils.showMessage("Adding command to process...");
                    String lc = command;
                    if(lc == null) {
                        TextUtils.warning("Empty command");
                        lc = "echo Happy-Day";
                    } 
                    TextUtils.showMessage("[Command] " + lc);
                    return buildProcess(lc);
                });
    }
    /**
     * A method to append the list to the pending list.
     * @param lists - the list to append.
     */
    public void appendListToCallableProcess(Map<String, Callable<List<Path>>> lists) {
        pendingLists.add(lists);
    }
    /**
     * clean up the pending lists.
     * <p> the process lists and the map list.
     */
    public void cleanPendingProcess() {
        pendingProcess.clear();
        pendingLists.clear();
    }
    /**
     * Executes the given commands and show the success or error. 
     * @param command the command to execute.
     */
    public void executeCommand(String command, ExecutorService executor) {
        Process p = null;
        try {
            ProcessBuilder builder = new ProcessBuilder();
            String localFULL = new File(LOCAL_PATH).getCanonicalPath();
            File local = new File(localFULL);
            if(command == null || command.isBlank()) {
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
            handleProcessStreams(p, executor);
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
    private ProcessBuilder buildProcess(String command) throws IOException {
        String localPath = new File(LOCAL_PATH).getCanonicalPath();
        if(OS_NAME_WINDOWS) {
            return new ProcessBuilder("pwsh", "-NoProfile", "-Command", command)
                .directory(new File(localPath));
        } else {
            return new ProcessBuilder("/bin/bash", "-c", command)
                .directory(new File(localPath));
        }
    }
    /**
     * Reading process stream in another thread to avoid blocking.
     * @param process - the process to read its output stream.
     * @param executor - the type of executor that create for thread execution.
     */
    private void handleProcessStreams(Process process, ExecutorService streamExecutor) {
        streamExecutor.submit(() -> TextUtils.commandOutput(process.getInputStream()));
        streamExecutor.submit(() -> TextUtils.commandOutputError(process.getErrorStream()));
    }
}
