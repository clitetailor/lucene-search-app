package mypackage.main;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import mypackage.main.prototype.Site;
import spark.Spark;

import java.util.ArrayList;

public class RESTServer {
    public static void main(String[] args) {
        IndexService indexService = new IndexService();

        Spark.post("/index", (req, res) -> {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<Site> sites = mapper.readValue(req.body(), new TypeReference<ArrayList<Site>>(){});

            indexService.addDocuments(DataExtractor.sitesToDocuments(sites));

            return "Ok!";
        });

        Spark.get("/search/:query-string", (req, res) -> {
            return "Search Result!";
        });
    }
}
