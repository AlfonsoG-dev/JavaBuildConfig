import Application.Operations.FileOperations;
class JavaBuildConfig {
    public static void main(String[] args) {
        FileOperations op = new FileOperations("./");
        String c = op.getConfigValues().toString();
        System.out.println(c);
    }
}
