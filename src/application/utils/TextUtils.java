package application.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public record TextUtils() {

    public static record Colors() {
        /**
         * color rojo para los valores numéricos
         */
        public static final String ANSI_RED = "\u001B[41m";
        /**
         * color cyan para el path del archivo
         */
        public static final String ANSI_CYAN = "\u001B[46m";
        /**
         * quitar color para los resultados no exactos
         */
        public static final String ANSI_RESET = "\u001B[0m";
        /**
         * color amarillo para los resultados
         */
        public static final String ANSI_YELLOW = "\u001B[33m";
        /**
         * color amarillo con under-linea 
         */
        public static final String YELLOW_UNDERLINED = "\033[4;33m";
        /**
         * color con linea baja
         */
        public static final String GREEN_UNDERLINED = "\033[4;32m";
        /**
         * color para link de file
         */
        public static final String RED_UNDERLINED = "\033[4;31m";
    }
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
    public static void writeLines(String fileURL, String lines) {
        try(FileWriter w = new FileWriter(new File(fileURL))) {
            if(!lines.isEmpty()) {
                w.write(lines);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
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
