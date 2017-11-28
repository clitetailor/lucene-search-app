package mypackage.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import mypackage.main.prototype.Site;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

import java.io.IOException;
import java.util.ArrayList;


public class DataExtractor {
    public static ArrayList<Document> extractSites(String restDocument) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Site> sites = mapper.readValue(restDocument, new TypeReference<ArrayList<Site>>(){});
        ArrayList<Document> docs = new ArrayList<Document>();

        for (Site site : sites) {
            Document doc = new Document();
            doc.add(new TextField("title", site.title, Field.Store.YES));
            doc.add(new TextField("content", site.content, Field.Store.YES));

            docs.add(doc);
        }

        return docs;
    }

    public static String toResponseString(ArrayList<Site> sites) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();

        for (Site site : sites) {
            arrayNode.add(mapper.valueToTree(site));
        }

        return arrayNode.toString();
    }

    public static String toResponseString(Site site) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(site);
    }

    public static String toResponseSuggestions(ArrayList<String> suggestions) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        ArrayNode arrayNode = mapper.createArrayNode();

        for (String suggestion : suggestions) {
            arrayNode.add(suggestion);
        }
        return arrayNode.toString();
    }
}
