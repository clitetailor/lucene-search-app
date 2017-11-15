package mypackage.main.logger;

public class ExceptionLogger {
    public static void logException(Exception e) {
        System.out.println(e.getClass() + " : " + e.getMessage());
    }
}
