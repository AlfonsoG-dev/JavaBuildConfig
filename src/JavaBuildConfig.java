import Application.Operations.CommandOperations;
class JavaBuildConfig {
    public static void main(String[] args) {
        CommandOperations cp = new CommandOperations("./");
        String c = cp.compile();
        System.out.println(c);
    }
}
