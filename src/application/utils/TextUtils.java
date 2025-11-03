package application.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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
    public static void CommandOutputError(InputStream miCmdStream) {
        try (BufferedReader miReader = new BufferedReader(new InputStreamReader(miCmdStream))) {
            String line = "";
            while(true) {
                line = miReader.readLine();
                if(line == null) {
                    break;
                }
                System.err.println(line);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void CommandOutput(InputStream miCmdStream) {
        try (BufferedReader miReader = new BufferedReader(new InputStreamReader(miCmdStream))) {
            String line = "";
            while(true) {
                line = miReader.readLine();
                if(line == null) {
                    break;
                }
                System.out.println(line);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
