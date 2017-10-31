package mypackage.main;

import mypackage.main.prototype.Site;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.json.JSONException;

import java.util.ArrayList;

public class DataExtractor {
    public static ArrayList<Document> sitesToDocuments(ArrayList<Site> sites) {
        ArrayList<Document> docs = new ArrayList<Document>();

        try {
            for (int i = 0; i < sites.size(); ++i) {
                Site site = sites.get(i);

                Document doc = new Document();
                doc.add(new TextField("title", site.title, Field.Store.YES));
                doc.add(new TextField("content", site.content, Field.Store.YES));
                doc.add(new TextField("url", site.url, Field.Store.YES));
            }

            return docs;
        } catch (JSONException e) {
            System.out.println("JSONException: " + e.getClass() + " : " + e.getMessage());
        }

        return new ArrayList<Document>();
    }
}
