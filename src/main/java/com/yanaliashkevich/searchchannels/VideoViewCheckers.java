package com.yanaliashkevich.searchchannels;

import com.yanaliashkevich.common.PropertiesHelper;
import com.yanaliashkevich.common.model.Channel;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Liashkevich_Y on 12.02.2018.
 */
public class VideoViewCheckers {
    public static long getViewsCount(Channel channel){
        long result = 0;

        Properties properties = PropertiesHelper.getProperties();
        String apiKey = properties.getProperty("youtube.apikey");

        String url = "https://www.googleapis.com/youtube/v3/channels?part=statistics&id=" + channel.getId() + "&key=" + apiKey;
        try {
            Document doc = Jsoup.connect(url).timeout(0).ignoreContentType(true).get();

            String getJson = doc.text();
            JSONObject jsonObject = (JSONObject) new JSONTokener(getJson).nextValue();
            JSONArray items = jsonObject.getJSONArray("items");

            JSONObject item = items.getJSONObject(0);
            JSONObject statistics = item.getJSONObject("statistics");
            String viewCount = statistics.getString("viewCount");
            result = Long.valueOf(viewCount);

        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }
}
