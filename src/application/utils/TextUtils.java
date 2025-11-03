package application.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public record TextUtils() {
    public final static void message(String message) {
        System.out.println("[Info] " + message);
    }
    public final static void error(String error) {
        System.err.println("[Error] " + error);
    }
    public static String getFileLines(String pathURL) {
        String lines = "";
        try(BufferedReader br = new BufferedReader(new FileReader(new File(pathURL)))) {
            while(br.ready()) {
                lines += br.readLine() + "\n";
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
