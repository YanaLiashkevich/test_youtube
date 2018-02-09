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
public class Channels {
    public static void main(String[] args) {
        Properties properties = PropertiesHelper.getProperties();

        String apiKey = properties.getProperty("youtube.apikey");

        String keyword = "DIY";

        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=channel&regionCode=US&maxResults=20&q=" + keyword + "&key=" + apiKey;

        ArrayList<Channel> channels = new ArrayList<Channel>();

        try {
            RetrieveSmallChannels smallChannels = new RetrieveSmallChannels();

            Document doc = Jsoup.connect(url).timeout(0).ignoreContentType(true).get();

            String getJson = doc.text();
            JSONObject jsonObject = (JSONObject) new JSONTokener(getJson).nextValue();
            JSONArray items = jsonObject.getJSONArray("items");

            for (int i = 0; i < items.length(); i++){
                JSONObject item = items.getJSONObject(i);
                JSONObject snippet = item.getJSONObject("snippet");

                JSONObject id = item.getJSONObject("id");
                String channelId = id.getString("channelId");

                String title = snippet.getString("title");

                Channel channel = new Channel();
                channel.setId(channelId);
                channel.setTitle(title);
                channels.add(channel);

                getChannels(channels);
                smallChannels.searchSmallChannel(channels, channelId);
            }




        }catch (IOException e){
            e.printStackTrace();
        }


    }

    public static void getChannels(ArrayList<Channel> channels){
        for (Channel channel : channels) {
            String id = channel.getId();
            String title = channel.getTitle();
            System.out.println(title + "\nhttps://www.youtube.com/channel/" + id);
        }
    }
}