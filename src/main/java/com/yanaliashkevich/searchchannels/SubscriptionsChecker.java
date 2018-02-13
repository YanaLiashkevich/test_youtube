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
 * Created by Liashkevich_Y on 13.02.2018.
 */
public class SubscriptionsChecker {

    public static String getSuscriptionsChannels(Channel channel){
        String result = null;

        Properties properties = PropertiesHelper.getProperties();
        String apiKey = properties.getProperty("youtube.apikey");

        String url = "https://www.googleapis.com/youtube/v3/subscriptions?part=id&channelId=" + channel.getId() + "&key=" + apiKey;

        String pageToken = null;
        int page = 1;

        if(page == 1){
            url = "https://www.googleapis.com/youtube/v3/subscriptions?part=id&channelId="
                    + channel.getId() + "&key=" + apiKey;
        } else if (pageToken != null){
            url = "https://www.googleapis.com/youtube/v3/subscriptions?part=id&channelId="
                    + channel.getId() + "&pageToken=" + pageToken + "&key=" + apiKey;
        } else {

        }

        try {
            Document doc = Jsoup.connect(url).timeout(0).ignoreContentType(true).get();
            String getJson = doc.text();

            JSONObject jsonObject = (JSONObject) new JSONTokener(getJson).nextValue();
            JSONArray items = jsonObject.getJSONArray("items");
            JSONObject item = items.getJSONObject(0);
            result =item.getString("id");

        }catch (IOException e){
            e.printStackTrace();
        }

        return result;
    }
}
