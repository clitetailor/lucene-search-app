package mypackage.main;

public class ExceptionLogger {
    static void logException(Exception e) {
        System.out.println(e.getClass() + " : " + e.getMessage());
    }
}
