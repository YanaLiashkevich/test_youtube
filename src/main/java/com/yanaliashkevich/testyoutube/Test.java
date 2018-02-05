package com.yanaliashkevich.testyoutube;

import com.yanaliashkevich.searchbykeyword.Search;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.json.JSONTokener;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Yana on 04.02.2018.
 * test
 */
public class Test {
    private static final String PROPERTIES_FILENAME = "youtube.properties";

    public static void main(String[] args) {
        //TODO: вынести заполнение свойств в отдельный класс
        Properties properties = new Properties();

        try {
            InputStream in = Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
            properties.load(in);
        }catch (IOException e){
            System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause() + " : " + e.getMessage());
            System.exit(1);
        }

        String apiKey = properties.getProperty("youtube.apikey");

        String keyword = "cake";
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=viewCount&maxResults=10&publishedAfter=2018-01-01T00:00:00Z&q=" + keyword + "&key=" + apiKey;

        try {
            Document doc = Jsoup.connect(url).timeout(10 * 1000).ignoreContentType(true).get();

            String getJson = doc.text();
            JSONObject jsonObject = (JSONObject) new JSONTokener(getJson).nextValue();
            JSONArray items = jsonObject.getJSONArray("items");

            for (int i = 0; i < items.length(); i++){
                JSONObject item = items.getJSONObject(i);
                JSONObject id = item.getJSONObject("id");
                String videoId = id.getString("videoId");
                JSONObject snippet = item.getJSONObject("snippet");
                String title = snippet.getString("title");
                System.out.println(title + "\nhttps://www.youtube.com/watch?v=" + videoId);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
