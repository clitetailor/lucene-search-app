package mypackage.main;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import mypackage.main.prototype.Site;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

import java.util.ArrayList;

public class DataExtractor {
    public static ArrayList<Document> extractSites(String restDocument) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<Site> sites = mapper.readValue(restDocument, new TypeReference<ArrayList<Site>>(){});
            ArrayList<Document> docs = new ArrayList<Document>();

            for (int i = 0; i < sites.size(); ++i) {
                Site site = sites.get(i);

                Document doc = new Document();
                doc.add(new TextField("title", site.title, Field.Store.YES));
                doc.add(new TextField("content", site.content, Field.Store.YES));
                doc.add(new TextField("url", site.url, Field.Store.YES));
            }

            return docs;
        } catch (Exception e) {
            ExceptionLogger.logException(e);
            return null;
        }
    }
}
