package mypackage.main;

import org.apache.lucene.document.Document;
import spark.Spark;

import java.util.ArrayList;

public class RESTServer {
    /**
     * Server main app.
     */
    public static void main(String[] args) {
        LuceneApp luceneApp = new LuceneApp();

        /**  Try to initialize Lucene app.  */
        try {
            luceneApp.initialize();
        } catch (Exception e) {
            ExceptionLogger.logException(e);
            return;
        }

        Spark.port(9090);
        Spark.initExceptionHandler(e -> ExceptionLogger.logException(e));

        RESTServer.enableCORS(
                "*",
                "GET,PUT,DELETE,POST,OPTIONS",
                "Access-Control-Allow-Origin, "
                        + "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With"
        );


        /**  Indexing document.  **/
        Spark.post("/index", (req, res) -> {
            ArrayList<Document> documents = DataExtractor.extractSites(req.body());

            luceneApp.addDocuments(documents);
            luceneApp.commitWritting();

            return "Ok!";
        });


        /**  Search for documents.  **/
        Spark.get("/search/:query-string", (req, res) -> {
            String query = req.params("query-string");

            ArrayList<Document> documents = luceneApp.search(query);

            return DataExtractor.toResponseString(documents);
        });


        /**  Send stop signal to server.  **/
        Spark.options("/stop", (req, res) -> {
            Spark.stop();
            luceneApp.close();
            return "Server has stopped!";
        });
    }


    /**
     * CORS Plugin for SparkJava.
     * @param origin
     * @param methods
     * @param headers
     */
    private static void enableCORS(final String origin, final String methods, final String headers) {
        Spark.options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        Spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }
}
