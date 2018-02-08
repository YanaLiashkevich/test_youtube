package com.yanaliashkevich.searchchannels;

import com.yanaliashkevich.common.PropertiesHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Liashkevich_Y on 07.02.2018.
 */
public class Channels {
    public static void main(String[] args) {
        Properties properties = PropertiesHelper.getProperties();

        String apiKey = properties.getProperty("youtube.apikey");

        String keyword = "DIY";

        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=channel&regionCode=US&maxResults=10&q=" + keyword + "&key=" + apiKey;

        //ArrayList channelsCount = new ArrayList();

        try {
            Document doc = Jsoup.connect(url).timeout(10*1000).ignoreContentType(true).get();

            String getJson = doc.text();
            JSONObject jsonObject = (JSONObject) new JSONTokener(getJson).nextValue();
            JSONArray items = jsonObject.getJSONArray("items");

            for (int i = 0; i < items.length(); i++){
                JSONObject item = items.getJSONObject(i);
                JSONObject snippet = item.getJSONObject("snippet");

                /*JSONObject statistics = item.getJSONObject("statistics");
                String subscriberCount = statistics.getString("subscriberCount");
                Integer count = Integer.valueOf(subscriberCount);
                if (!(count < 4000 || count > 100000)){
                    continue;
                }*/

                JSONObject id = item.getJSONObject("id");
                String channelId = id.getString("channelId");
                String title = snippet.getString("title");
                System.out.println(title + "\nhttps://www.youtube.com/channel/" + channelId);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}