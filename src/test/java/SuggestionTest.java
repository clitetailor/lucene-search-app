import mypackage.main.lucene.SearchSuggester;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SuggestionTest {
    public static void main(String[] args) {
        Path indexPath = Paths.get("./test").toAbsolutePath();

        SearchSuggester suggester = null;
        try {
            suggester = new SearchSuggester(indexPath);
        } catch (IOException e) {
            System.out.println(e.getClass() + " : " + e.getMessage());
        }

        try {
            suggester.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> strings = null;
        try {
            strings = suggester.suggest("luc");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getClass() + " : " + e.getMessage());
        }
        System.out.println(strings.size());
        System.out.println(strings.toString());
    }
}
