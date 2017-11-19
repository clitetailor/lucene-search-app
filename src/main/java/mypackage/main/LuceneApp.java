package mypackage.main;

import mypackage.main.lucene.SearchSuggester;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

public class LuceneApp {
    private Path indexPath;

    private IndexWriter indexWriter;
    private ReferenceManager<IndexSearcher> searcherManager;
    private SearchSuggester suggester;

    public LuceneApp() {
        this("./index");
    }

    public LuceneApp(String dir) {
        indexPath = FileSystems.getDefault().getPath(dir);
        System.out.println(indexPath.toString());
    }

    /**
     *  Initialize Lucene App.
     */
    public void initialize() throws IOException {
        openIndexForWritting(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        searcherManager = new SearcherManager(indexWriter, null);
    }

    /**
     *  Try to open directory for writting index
     */
    private void openIndexForWritting(IndexWriterConfig.OpenMode openMode) throws IOException {
        // Open directory for writting index.
        Directory directory = FSDirectory.open(indexPath);
        suggester = new SearchSuggester(directory);

        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        config.setOpenMode(openMode);

        // Create a new index writter.
        indexWriter = new IndexWriter(directory, config);
    }


    public ArrayList<Document> search(String searchString) throws IOException, ParseException {
        // Acquire a new index searcher.
        IndexSearcher indexSearcher = searcherManager.acquire();

        Analyzer analyzer = new StandardAnalyzer();
        QueryParser queryParser = new QueryParser(
                Version.LUCENE_7_1_0,
                "content",
                analyzer
        );

        TopDocs topDocs = indexSearcher.search(queryParser.parse(searchString), 20);

        // Remember to release searcher after searching.
        searcherManager.release(indexSearcher);

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        ArrayList<Document> docs = new ArrayList<>();
        for (ScoreDoc scoreDoc : scoreDocs) {
            docs.add(
                    indexSearcher.doc(scoreDoc.doc)
            );
        }
        return docs;
    }

    /**
     *  Index a list of documents.
     */
    public void addDocuments(ArrayList<Document> documents) throws IOException {
        indexWriter.addDocuments(documents);

        for (Document document : documents) {
            suggester.add(document.getField("title").toString());
        }
    }

    /**
     *  Give a suggestion for the given search string.
     */
    public ArrayList<String> suggest(String suggestString) {
        return suggester.suggest(suggestString);
    }

    /**
     *  Commit pending index documents.
     *  This method is quite costly, so avoid call this method frequently!
     */
     public void commitWritting() throws IOException {
         indexWriter.commit();
     }

    /**
     *  Try to close index writter and search manager
     *  to finalize closing process.
     */
    public void close() throws IOException {
        indexWriter.close();
        searcherManager.close();
    }
}
