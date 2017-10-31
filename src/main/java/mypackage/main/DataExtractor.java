package mypackage.main;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataExtractor {
    public static ArrayList<Document> extractData(JSONObject inputObject) {
        JSONArray collection = null;

        try {
            collection = inputObject.getJSONArray("collection");
        } catch (JSONException e) {
            System.out.println("JSONException: " + e.getClass() + " : " + e.getMessage());

            return new ArrayList<Document>();
        }

        ArrayList<Document> docs = new ArrayList<Document>();

        try {
            for (int i = 0; i < collection.length(); ++i) {
                JSONObject oQuery = collection.getJSONObject(i);

                Document doc = new Document();
                doc.add(new TextField("title", oQuery.getString("title"), Field.Store.YES));
                doc.add(new TextField("content", oQuery.getString("content"), Field.Store.YES));
            }

            return docs;
        } catch (JSONException e) {
            System.out.println("JSONException: " + e.getClass() + " : " + e.getMessage());
        }

        return new ArrayList<Document>();
    }
}
