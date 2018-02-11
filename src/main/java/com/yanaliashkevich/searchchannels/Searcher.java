package com.yanaliashkevich.searchchannels;

import com.yanaliashkevich.common.PropertiesHelper;
import com.yanaliashkevich.common.model.Channel;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by Liashkevich_Y on 07.02.2018.
 */
public class Searcher {
    public static void main(String[] args) {
        Properties properties = PropertiesHelper.getProperties();

        String apiKey = properties.getProperty("youtube.apikey");

        ArrayList<Channel> channels = new ArrayList<Channel>();

        String keyword = "DIY";
        String pageToken = null;

        for (int page = 1; page <= 5; page++) {
            System.out.println("Processing of " + page + " page...");
            String url;
            if(page == 1){
                url = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=channel&regionCode=US&maxResults=20&q="
                        + keyword + "&key=" + apiKey;
            } else if (pageToken != null){
                url = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=channel&regionCode=US&maxResults=20&q="
                        + keyword + "&pageToken=" + pageToken + "&key=" + apiKey;
            } else {
                break;
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

                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    JSONObject snippet = item.getJSONObject("snippet");

                    JSONObject id = item.getJSONObject("id");
                    String channelId = id.getString("channelId");

                    String title = snippet.getString("title");

                    Channel channel = new Channel();
                    channel.setId(channelId);
                    channel.setTitle(title);

                    long subscribersCount = ChannelsChecker.getSubscribersCount(channel);

                    channel.setSubscribersCount(subscribersCount);

                    if (subscribersCount > 0 && subscribersCount < 2000) {
                        channels.add(channel);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        printChannels(channels);
    }

    public static void printChannels(ArrayList<Channel> channels){
        for (Channel channel : channels) {
            String id = channel.getId();
            String title = channel.getTitle();
            long subscribersCount = channel.getSubscribersCount();
            System.out.println(title + "\ncount of subscribers = " + subscribersCount + "\nhttps://www.youtube.com/channel/" + id);
            System.out.println();
        }
    }
}