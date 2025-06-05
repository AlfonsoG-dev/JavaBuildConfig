package Application;

import java.io.File;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Execution {
    private String rootPath;
    public Execution(String rootPath) {
        this.rootPath = rootPath;
    }

    private void CMDOutput(InputStream miCmdStream) {
        BufferedReader miReader = null;
        try {
            miReader = new BufferedReader(new InputStreamReader(miCmdStream));
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
        } finally {
            if(miReader != null) {
                try {
                    miReader.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
                miReader = null;
            }
        }
    }

    private void CMDOutputError(InputStream miCmdStream) {
        BufferedReader miReader = null;
        try {
            miReader = new BufferedReader(new InputStreamReader(miCmdStream));
            String line = "";
            while(true) {
                line = miReader.readLine();
                if(line == null) {
                    break;
                }
                System.out.println("[Error] " + line);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(miReader != null) {
                try {
                    miReader.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
                miReader = null;
            }
        }
    }

    public void executeCommand(String command) {
        Process p = null;
        try {
            ProcessBuilder builder = new ProcessBuilder();
            String localFULL = new File(rootPath).getCanonicalPath();
            File local = new File(localFULL);
            if(command.isEmpty()) {
                throw new Exception("[Error] cannot execute an empty command: ");
            }
            System.out.println("[cmd] " + command);
            if(System.getProperty("os.name").toLowerCase().contains("windows")) {
                builder.command("pwsh", "-NoProfile", "-Command", command);
            } else if(System.getProperty("os.name").toLowerCase().contains("linux")) {
                builder.command("/bin/sh", "-c", command);
            }
            builder.directory(local);
            p = builder.start();
            if(p.getErrorStream() != null) {
                CMDOutputError(p.getErrorStream());
            }
            if(p.getInputStream() != null) {
                CMDOutput(p.getInputStream());
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(p != null) {
                try {
                    p.waitFor();
                    p.destroy();;
                } catch(Exception e) {
                    e.printStackTrace();
                }
                p = null;
            }
        }
    }
}
