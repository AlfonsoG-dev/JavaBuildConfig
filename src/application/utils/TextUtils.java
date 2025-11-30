package application.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStream;
import java.io.InputStreamReader;

public record TextUtils() {
    private static final Console CONSOL = System.console();
    private static final String CONSOL_FORMAT = "%s[%s] %s%s%n";

    /**
     * ANSI colors for output messages.
     */
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
    /**
     * Show messages without formatting.
     * <p> the user need to give the format in the message.
     * @param messages - the message to show.
     */
    public static final void showMessage(String message) {
        CONSOL.printf("%s%n", message);
    }
    /**
     * A formatted message with warning label.
     * <p> the warning label means the message needs to be yellow.
     * @param message - the warning message.
     */
    public static final void warning(String message) {
        CONSOL.printf(CONSOL_FORMAT, Colors.YELLOW_UNDERLINED, "Warning", Colors.ANSI_RESET, message);
    }
    /**
     * A formatted message with info label.
     * <p> the info label means the message needs to be green.
     * @param message - the message to format as info.
     */
    public static final void message(String message) {
        CONSOL.printf(CONSOL_FORMAT, Colors.GREEN_UNDERLINED, "Info", Colors.ANSI_RESET, message);
    }
    /**
     * A formatted message with error label.
     * <p> the error label means the message needs to be red.
     * @param error - the message to format as error.
     */
    public static final void error(String error) {
        CONSOL.printf(CONSOL_FORMAT, Colors.RED_UNDERLINED, "Error", Colors.ANSI_RESET, error);
    }
    /**
     * Get the content of a file as a string.
     * @param pathURI - the path to the file to read its content.
     * @return a string with the file content.
     */
    public static String getFileLines(String pathURI) {
        StringBuilder lines = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(new File(pathURI)))) {
            while(br.ready()) {
                lines.append(br.readLine());
                lines.append("\n");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return lines.toString();
    }
    /**
     * Write the lines given to a certain file path.
     * <p> if the file doesn't exists the file is created.
     * <p> if the file already exists the content is override.
     * @param fileURI - the path to the file.
     * @param lines - the content to write into the file.
     */
    public static void writeLines(String fileURI, String lines) {
        CONSOL.printf("%s", "Writing lines...");
        try(FileWriter w = new FileWriter(fileURI, false)) {
            if(!lines.isEmpty()) {
                w.write(lines);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * show the error message from a Stream.
     * <p> Use this with the process.getErrorStream().
     * @param miCmdStream - the stream to show its message.
     */
    public static void commandOutputError(InputStream miCmdStream) {
        try (BufferedReader miReader = new BufferedReader(new InputStreamReader(miCmdStream))) {
            String line = "";
            while(true) {
                line = miReader.readLine();
                if(line == null) {
                    break;
                }
                // show error message
                error(line);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * show the success message from a Stream.
     * <p> Use this with the process.getInputStream.
     * @param miCmdStream - the stream to show its message.
     */
    public static void commandOutput(InputStream miCmdStream) {
        try (BufferedReader miReader = new BufferedReader(new InputStreamReader(miCmdStream))) {
            String line = "";
            while(true) {
                line = miReader.readLine();
                if(line == null) {
                    break;
                }
                // show success message
                CONSOL.printf("%s%n", line);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
