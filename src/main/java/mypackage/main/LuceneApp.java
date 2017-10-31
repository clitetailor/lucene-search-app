package mypackage.main;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ReferenceManager;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

public class LuceneApp {
    private Path indexPath;

    private IndexWriter indexWriter;
    ReferenceManager<IndexSearcher> searcherManager;

    public LuceneApp() {
        this("./index");
    }

    public LuceneApp(String dir) {
        this.indexPath = FileSystems.getDefault().getPath(dir);
    }

    private boolean openIndexForWritting() {
        return openIndexForWritting(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
    }

    private boolean openIndexForWritting(IndexWriterConfig.OpenMode openMode) {
        try {
            Directory directory = FSDirectory.open(indexPath);

            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);

            config.setOpenMode(openMode);

            indexWriter = new IndexWriter(directory, config);

            return true;
        } catch (IOException e) {
            ExceptionLogger.logException(e);
            return false;
        }
    }

    private boolean openIndexForBothWriteAndRead(IndexWriterConfig.OpenMode openMode) {
        if (openIndexForWritting(openMode)) {
            try {
                searcherManager = new SearcherManager(indexWriter, null);

                return true;
            } catch (IOException e) {
                ExceptionLogger.logException(e);
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean addDocuments(ArrayList<Document> documents) {
        try {
            indexWriter.addDocuments(documents);

            return true;
        } catch (IOException e) {
            ExceptionLogger.logException(e);
            return false;
        }
    }

    public boolean commitWritting() {
        try {
            indexWriter.commit();
            indexWriter.close();

            return true;
        } catch (IOException e) {
            ExceptionLogger.logException(e);
            return false;
        }
    }
}
