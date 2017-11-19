package mypackage.main;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.simple.SimpleQueryParser;
import org.apache.lucene.queryparser.surround.parser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

public class LuceneApp {
    private Path indexPath;

    private IndexWriter indexWriter;
    private IndexReader indexReader;

    private IndexSearcher searcher;
    private Directory directory;


    public LuceneApp() {
        this("./index");
    }

    public LuceneApp(String dir) {
        indexPath = FileSystems.getDefault().getPath(dir);
    }

    public void writeDocuments(ArrayList<Document> documents) throws IOException {
        directory = FSDirectory.open(indexPath);

        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer());
        indexWriter = new IndexWriter(directory, indexWriterConfig);

        indexWriter.addDocuments(documents);

        indexWriter.commit();
        indexWriter.close();
    }

    public ArrayList<Document> search(String searchString) throws IOException, ParseException {
        directory = FSDirectory.open(indexPath);

        indexReader = DirectoryReader.open(directory);
        searcher = new IndexSearcher(indexReader);

        Query query = new SimpleQueryParser(new StandardAnalyzer(), "title").parse(searchString);

        TopDocs topDocs = searcher.search(query, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        ArrayList<Document> documents = new ArrayList<>();
        for (ScoreDoc scoreDoc : scoreDocs) {
            Document document = searcher.doc(scoreDoc.doc);
            documents.add(document);
        }

        indexReader.close();

        return documents;
    }
}
