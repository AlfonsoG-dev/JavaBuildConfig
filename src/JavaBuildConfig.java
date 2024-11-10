import Application.Operations.FileOperations;
class JavaBuildConfig {
    public static void main(String[] args) {
        FileOperations op = new FileOperations("./");
        op.getConfigValues()
            .stream()
            .forEach(e -> {
                System.out.println(e);
            });
    }
}
