package kozmikoda.passwordspace;

public class Utility {
    public static boolean isWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }

    public static int generateResetCode() {
        return (int) (Math.random() * 1000000);
    }

    public static boolean validateResetCode(int resetCode, int prompt) {
        return resetCode == prompt;
    }

}
