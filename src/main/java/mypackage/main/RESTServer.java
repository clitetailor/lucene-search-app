package mypackage.main;

import spark.Spark;

public class RESTServer {
    public static void main(String[] args) {
        Spark.get("/hello", (req, res) -> {

            return "Hello, World";
        });
    }
}
