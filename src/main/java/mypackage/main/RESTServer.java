package mypackage.main;

import mypackage.main.logger.ExceptionLogger;
import org.apache.lucene.document.Document;

import java.io.IOException;
import java.util.ArrayList;

import static spark.Spark.*;

public class RESTServer {
    /**
     * Server main app.
     */
    public static void main(String[] args) {
        LuceneApp luceneApp = null;
        try {
            luceneApp = new LuceneApp("./index");
        } catch (IOException e) {
            ExceptionLogger.logException(e);
            return;
        }

        final LuceneApp finalLuceneApp = luceneApp;

        int portNumber = 9090;

        port(portNumber);

        System.out.println("Server is running on port " + portNumber + "!");
        initExceptionHandler(e -> ExceptionLogger.logException(e));

        RESTServer.enableCORS();

        /**  Indexing document.  **/
        post("/index-docs", (req, res) -> {
            ArrayList<Document> documents = DataExtractor.extractSites(req.body());

            finalLuceneApp.writeDocuments(documents);

            return "Ok!";
        });


        /**  Search for documents.  **/
        get("/search/:query-string", (req, res) -> {
            String query = req.params("query-string");

            ArrayList<Document> documents = finalLuceneApp.search(query);

            return DataExtractor.toResponseString(documents);
        });


        /**  Show search suggestions  **/
        get("/suggest/:suggest-string", (req, res) -> {
            String sourceString = req.params("suggest-string");

            ArrayList<String> suggestions = finalLuceneApp.suggest(sourceString);
            return DataExtractor.toResponseSuggestions(suggestions);
        });


        /**  Send stop signal to server.  **/
        get("/stop", (req, res) -> {
            stop();
            return "Server has stopped!";
        });
    }


    /**
     * CORS Plugin for SparkJava.
     */
    private static void enableCORS() {
         before((request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            response.header("Access-Control-Allow-Origin", "*");
            response.type("application/json");
        });
    }
}
