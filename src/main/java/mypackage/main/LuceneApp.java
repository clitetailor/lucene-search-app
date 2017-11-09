package mypackage.main;

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

    public LuceneApp() {
        this("./index");
    }

    public LuceneApp(String dir) {
        this.indexPath = FileSystems.getDefault().getPath(dir);
    }


    /**
     *  Initialize Lucene App.
     */
    public void initialize() throws IOException {
        openIndexForWritting(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        searcherManager = new SearcherManager(indexWriter, null);
    }


    private void openIndexForWritting(IndexWriterConfig.OpenMode openMode) throws IOException {
        /**
         *  Try to open directory for writting index
         */
        Directory directory = FSDirectory.open(indexPath);

        /**  Create a new standard tokenizer.  **/
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        /**  Set index writter mode.  **/
        config.setOpenMode(openMode);

        /**
         *  Create a new index writter.
         */
        indexWriter = new IndexWriter(directory, config);
    }


    public ArrayList<Document> search(String searchString) throws IOException, ParseException {
        /**
         *  Acquire index searcher from searh manager.
         */
        IndexSearcher indexSearcher = searcherManager.acquire();

        /**
         *  Create a new standard analyzer for search query.
         */
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser queryParser = new QueryParser(
                Version.LUCENE_7_1_0,
                "content",
                analyzer
        );

        /**
         *  Search from search string and return top docs.
         */
        TopDocs topDocs = indexSearcher.search(queryParser.parse(searchString), 20);

        /**
         *  Release index searcher.
         */
        searcherManager.release(indexSearcher);

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        /**
         *  Map from Score Doc to Documents
         */
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
