package com.yanaliashkevich.searchchannels;

import com.yanaliashkevich.common.PropertiesHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Liashkevich_Y on 13.02.2018.
 */
public class SubscriptionsChecker {

    public static List<String> getSuscriptionsChannels(){
        List<String> result = new ArrayList<String>();

        Properties properties = PropertiesHelper.getProperties();
        String apiKey = properties.getProperty("youtube.apikey");
        String myChannelId = properties.getProperty("youtube.myChannelId");
        //TODO: вынести в проперти айди своего канала
        String url = "https://www.googleapis.com/youtube/v3/subscriptions?part=id&maxResults=50&channelId=" + myChannelId +"&key="
                + apiKey;

        long pagesCount = Searcher.getPagesCountFromUrl(url);

        String pageToken = null;

        for (int page = 1; page <= pagesCount; page++) {
            if(page > 1 && pageToken != null){
                url = "https://www.googleapis.com/youtube/v3/subscriptions?part=id&maxResults=50&channelId=" + myChannelId + "&pageToken=" +
                        pageToken + "&key=" + apiKey;
            }
            try {
                Document doc = Jsoup.connect(url).timeout(0).ignoreContentType(true).get();
                String getJson = doc.text();

                JSONObject jsonObject = (JSONObject) new JSONTokener(getJson).nextValue();

                if (jsonObject.has("nextPageToken")) {
                    pageToken = jsonObject.getString("nextPageToken");
                } else {
                    pageToken = null;
                }

                JSONArray items = jsonObject.getJSONArray("items");
                JSONObject item = items.getJSONObject(0);
                String channelId = item.getString("id");
                result.add(channelId);


            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return result;
    }
}
