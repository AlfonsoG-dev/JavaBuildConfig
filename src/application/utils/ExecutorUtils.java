package application.utils;

import application.utils.TextUtils;

import java.util.concurrent.Future;
import java.util.concurrent.Future.State;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;

import java.lang.InterruptedException;

public record ExecutorUtils() {

    public<T> T getResult(Callable<T> task) {
        T value = null;
        try(ExecutorService ex = Executors.newCachedThreadPool()) {
            Future<T> result = ex.submit(task);
            if(result.state() == Future.State.RUNNING) {
                TextUtils.message("[Info] Waiting for results...");
            }
            value = result.get();
        } catch(RejectedExecutionException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return value;
    }
    public void executeCommand(String command) {
        // TODO: add command execution using system tools like pwsh or cmd
        TextUtils.message(command);
    }
}
