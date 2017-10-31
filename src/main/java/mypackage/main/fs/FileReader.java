package mypackage.main.fs;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

public class FileReader {
    private String filename = "index.json";

    public FileReader(String filename) {
        this.filename = filename;
    }

    public String readFile() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        InputStream inputStream = classLoader.getResourceAsStream(filename);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();

        try {
            String line = reader.readLine();

            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }

            return builder.toString();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getClass() + " : " + e.getMessage());
            return null;
        }
    }
}
