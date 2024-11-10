import java.io.File;
import java.io.IOException;

import Application.Utils.FileUtils;
class JavaBuildConfig {
    public static void main(String[] args) throws IOException {
        FileUtils fu = new FileUtils(String.format(".%s", File.separator));
        String c = fu.getProjectClassNames();
        System.out.println(c);
    }
}
