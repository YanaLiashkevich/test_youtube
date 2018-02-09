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
 * Created by Liashkevich_Y on 09.02.2018.
 */
public class RetrieveSmallChannels {
    public static void main(String[] args) {
    }

    public void searchSmallChannel(ArrayList<Channel> channels, String channelId){
        Properties properties = PropertiesHelper.getProperties();
        String apiKey = properties.getProperty("youtube.apikey");

        ArrayList<Channel> smallChanel = new ArrayList<Channel>();

        String url = "https://www.googleapis.com/youtube/v3/channels?part=statistics" + "&key=" + apiKey;
        try {
            Document doc = Jsoup.connect(url).timeout(0).ignoreContentType(true).get();

            String getJson = doc.text();
            JSONObject jsonObject = (JSONObject) new JSONTokener(getJson).nextValue();
            JSONArray items = jsonObject.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject snippet = item.getJSONObject("snippet");
                JSONObject id = item.getJSONObject("id");
                channelId = id.getString("channelId");
                String title = snippet.getString("title");
                JSONObject statistics = item.getJSONObject("statistics");
                String subscriberCount = statistics.getString("subscriberCount");

                if (Integer.valueOf(subscriberCount) > 1000){
                    continue;
                }

                Channel channel = new Channel();
                channel.setId(channelId);
                channel.setTitle(title);
                smallChanel.add(channel);
            }

            for (Channel channel : channels) {
                String id = channel.getId();
                String title = channel.getTitle();
                System.out.println(title + "\nhttps://www.youtube.com/channel/" + id);
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        /*YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
            public void initialize(HttpRequest httpRequest) throws IOException {
            }
        }).setApplicationName("youtube-cmdline-search-sample").build();  UCap97Ue8K_BpKlrvQRYd6JA

        try {
            HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put("part", "snippet, contentDetails,statistics");
            parameters.put("id", "channelId");

            YouTube.Channels.List channelsListByIdRequest = youtube.channels().list(parameters.get("part").toString());

            if (parameters.containsKey("id") && parameters.get("id") != "") {
                channelsListByIdRequest.setId(parameters.get("id").toString());
            }

            if (parameters.containsKey("subscriberCount") && parameters.get("subscriberCount") > ){

            }

        }catch (IOException e){
            e.printStackTrace();
        }*/
    }
}
