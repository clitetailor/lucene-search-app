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

    public static String toResponseString(ArrayList<Document> documents) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Site> sites = new ArrayList<Site>();

        System.out.println(documents.size());

        ArrayNode arrayNode = mapper.createArrayNode();

        for (Document doc : documents) {
            Site site = new Site();

            site.title = doc.getField("title").stringValue();
            site.content = doc.getField("content").stringValue();
            arrayNode.add(mapper.valueToTree(site));
        }

        return arrayNode.toString();
    }

    public static String toResponseSuggestions(ArrayList<String> suggestions) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        System.out.println(suggestions.size());

        for (String suggest : suggestions) {
            System.out.println(suggest);
        }

        return mapper.writeValueAsString(suggestions);
    }
}
