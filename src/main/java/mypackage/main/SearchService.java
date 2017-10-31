package mypackage.main;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

public class SearchService {
    private Path indexPath;
    private IndexSearcher indexSearcher;

    public IndexSearcher getIndexSearcher() {
        return indexSearcher;
    }

    public SearchService() {
        this("./indexes");
    }

    public SearchService(String dir) {
        indexPath = FileSystems.getDefault().getPath(dir);
    }

    public ArrayList<Document> search(String text) {
        try {
            if (openIndexForSearch()) {
                Analyzer analyzer = new StandardAnalyzer();

                QueryParser queryParser = new QueryParser(Version.LUCENE_7_1_0, "title", analyzer);
                Query query = queryParser.parse(text);

                ScoreDoc[] hits = indexSearcher.search(query, 1000).scoreDocs;

                ArrayList<Document> docs = new ArrayList<Document>();

                for (int i = 0; i < hits.length; ++i) {
                    Document document = indexSearcher.doc(hits[i].doc);

                    docs.add(document);
                }

                return docs;
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getClass() + " : " + e.getMessage());
        } catch (ParseException e) {
            System.out.println("ParseException: " + e.getClass() + " : " + e.getMessage());
        }

        return new ArrayList<>();
    }

    private boolean openIndexForSearch() {
        try {
            if (this.indexPath != null) {
                IndexReader indexReader = DirectoryReader.open(FSDirectory.open(indexPath));
                indexSearcher = new IndexSearcher(indexReader);

                return true;
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getClass() + " : " + e.getMessage());
            return false;
        }

        return false;
    }
}
