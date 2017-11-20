package mypackage.main.lucene;

import org.apache.lucene.search.suggest.Lookup;
import org.apache.lucene.search.suggest.tst.TSTLookup;
import org.apache.lucene.store.Directory;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SearchSuggester {
    Path indexPath;
    Directory directory;
    TSTLookup tstLookup;

    public SearchSuggester(Path indexPath) throws IOException {
        this.indexPath = indexPath;
        tstLookup = new TSTLookup();
    }

    public void add(String suggestion) {
        tstLookup.add(suggestion, 1);
    }

    public ArrayList<String> suggest(String suggestString) throws UnsupportedEncodingException {
        List<Lookup.LookupResult> lookupResults = tstLookup.lookup(suggestString, null, false, 3);

        ArrayList<String> suggestions = new ArrayList<String>();
        for (Lookup.LookupResult result : lookupResults) {
            suggestions.add(result.key.toString());
        }

        return suggestions;
    }

    public void store() throws IOException {
        File file = new File(indexPath.resolve("./suggestions").toString());
        FileOutputStream outputStream = new FileOutputStream(file);

        tstLookup.store(outputStream);
    }

    public void load() throws IOException {
        File file = new File(indexPath.resolve("./suggestions").toString());

        if (file.exists()) {
            FileInputStream inputStream = new FileInputStream(file);

            tstLookup.load(inputStream);
        }
    }
}
