package mypackage.main.lucene;

import org.apache.lucene.search.suggest.DocumentDictionary;
import org.apache.lucene.search.suggest.Lookup;
import org.apache.lucene.search.suggest.tst.TSTLookup;
import org.apache.lucene.store.Directory;

import java.util.ArrayList;
import java.util.List;

public class SearchSuggester {
    DocumentDictionary dictionary;
    TSTLookup tstLookup;

    public SearchSuggester(Directory directory) {
        tstLookup = new TSTLookup(directory, "suggest-");
    }

    public void add(String suggestion) {
        tstLookup.add(suggestion, null);
    }

    public ArrayList<String> suggest(String suggestString) {
        List<Lookup.LookupResult> lookupResults = tstLookup.lookup(
                        suggestString,
                        null,
                        false,
                        3
                );

        ArrayList<String> suggestions = new ArrayList<String>();
        for (Lookup.LookupResult result : lookupResults) {
            suggestions.add(result.key.toString());
        }

        return suggestions;
    }
}
