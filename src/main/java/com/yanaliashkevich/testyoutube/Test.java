package com.yanaliashkevich.testyoutube;

import com.yanaliashkevich.common.PropertiesHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Yana on 04.02.2018.
 * test
 */
public class Test {

    public static void main(String[] args) {
        //TODO: вынести заполнение свойств в отдельный класс
        PropertiesHelper propertiesHelper = new PropertiesHelper();
        Properties properties = propertiesHelper.getProperties();

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
