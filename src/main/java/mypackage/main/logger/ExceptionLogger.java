package mypackage.main.logger;

import org.json.JSONException;

public class ExceptionLogger {
    public static void logException(Exception e) {
        System.out.println(e.getClass() + " : " + e.getMessage());
    }

    public static void logException(JSONException e) {
        System.out.println(e.getClass() + " : " + e.getMessage());
    }
}
