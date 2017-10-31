package mypackage.main;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

public class IndexService {
    private Path indexPath;
    private IndexWriter indexWriter;

    public IndexWriter getIndexWriter() {
        return indexWriter;
    }

    public IndexService() {
        this("./indexes");
    }

    public IndexService(String dir) {
        indexPath = FileSystems.getDefault().getPath(dir);
    }

    public void addDocuments(ArrayList<Document> docs) {
        try {
            if (openIndexForWrite()) {
                indexWriter.addDocuments(docs);
                finishWritting();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getClass() + " : " + e.getMessage());
        }
    }

    private boolean openIndexForWrite() {
        try {
            if (indexPath != null) {
                Directory indexDirectory = FSDirectory.open(indexPath);

                Analyzer analyzer = new StandardAnalyzer();
                IndexWriterConfig iwConfig = new IndexWriterConfig(analyzer);

                iwConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

                indexWriter = new IndexWriter(indexDirectory, iwConfig);

                return true;
            }
        } catch (IOException e) {
            indexWriter = null;
        }

        return false;
    }


    private void finishWritting() {
        try {
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getClass() + " : " + e.getMessage());
        }
    }
}
