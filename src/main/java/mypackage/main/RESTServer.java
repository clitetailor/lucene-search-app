package mypackage.main;

import spark.Spark;

public class RESTServer {
    public static void main(String[] args) {
        LuceneApp luceneApp = new LuceneApp();

        Spark.post("/index", (req, res) -> {
            DataExtractor.extractSites(req.body());

            return "Ok!";
        });

        Spark.get("/search/:query-string", (req, res) -> {
            String query = req.params("query-string");

            query = new String(query.getBytes("UTF-8"), "ASCII");


            return "Search Result!";
        });
    }
}
