import mypackage.main.LuceneApp;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

import java.io.IOException;
import java.util.ArrayList;

public class LuceneAppTest {
    public static void main(String[] args) {
        LuceneApp luceneApp = null;
        try {
            luceneApp = new LuceneApp("./test");
        } catch (IOException e) {
            System.out.println(e.getClass() + " : " + e.getMessage());
        }

        Document document1 = new Document();

        document1.add(new TextField("title", "lucene core", Field.Store.YES));
        document1.add(new TextField("content", "lucene app", Field.Store.YES));

        Document document2 = new Document();

        document2.add(new TextField("title", "what's up!", Field.Store.YES));
        document2.add(new TextField("content", "no relevent!", Field.Store.YES));

        ArrayList<Document> documents = new ArrayList<>();
        documents.add(document1);
        documents.add(document2);

        try {
            luceneApp.writeDocuments(documents);
        } catch (IOException e) {
            System.out.println(e.getClass() + " : " + e.getMessage());
        }

        try {
            documents = luceneApp.search("lucene");

            for (Document document : documents) {
                System.out.println(document.getField("title").stringValue());
            }
        } catch (Exception e) {
            System.out.println(e.getClass() + " : " + e.getMessage());
        }
    }
}
