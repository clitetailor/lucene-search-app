package mypackage.main;

import mypackage.main.lucene.SearchSuggester;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.simple.SimpleQueryParser;
import org.apache.lucene.queryparser.surround.parser.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

public class LuceneApp {
    private Path indexPath;

    private IndexWriter indexWriter;
    private IndexReader indexReader;

    private IndexSearcher searcher;

    private Directory directory;
    private SearchSuggester suggester;

    public LuceneApp() throws IOException {
        this("./index");
    }

    public LuceneApp(String dir) throws IOException {
        indexPath = FileSystems.getDefault().getPath(dir);
        suggester = new SearchSuggester(indexPath);

        suggester.load();
    }

    public void writeDocuments(ArrayList<Document> documents) throws IOException {
        Directory directory = FSDirectory.open(indexPath);

        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new WhitespaceAnalyzer());
        indexWriter = new IndexWriter(directory, indexWriterConfig);

        indexWriter.addDocuments(documents);

        for (Document document : documents) {
            suggester.add(document.getField("title").stringValue());
        }

        indexWriter.commit();
        indexWriter.close();
    }

    public ArrayList<Document> search(String searchString) throws IOException, ParseException {
        Directory directory = FSDirectory.open(indexPath);

        indexReader = DirectoryReader.open(directory);
        searcher = new IndexSearcher(indexReader);

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        Query titleQuery = new SimpleQueryParser(new WhitespaceAnalyzer(), "title").parse(searchString);
        Query contentQuery = new SimpleQueryParser(new WhitespaceAnalyzer(), "content").parse(searchString);
        builder.add(titleQuery, BooleanClause.Occur.SHOULD);
        builder.add(contentQuery, BooleanClause.Occur.SHOULD);
        BooleanQuery booleanQuery = builder.build();

        TopDocs topDocs = searcher.search(booleanQuery, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        ArrayList<Document> documents = new ArrayList<>();
        for (ScoreDoc scoreDoc : scoreDocs) {
            Document document = searcher.doc(scoreDoc.doc);
            documents.add(document);
        }

        indexReader.close();

        return documents;
    }

    public ArrayList<String> suggest(String string) throws UnsupportedEncodingException {
        return suggester.suggest(string);
    }
}
