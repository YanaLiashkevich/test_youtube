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
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by Liashkevich_Y on 07.02.2018.
 */
public class Searcher {

    public static ArrayList<Channel> searchChannels() {
        Properties properties = PropertiesHelper.getProperties();

        String apiKey = properties.getProperty("youtube.apikey");

        ArrayList<Channel> channels = new ArrayList<Channel>();
        List<String> mySubscriptions = SubscriptionsChecker.getSuscriptionsChannels();

        System.out.println(mySubscriptions);

        String keyword = "DIY";
        String pageToken = null;
        long pagesCount = getPagesCountFromUrl("https://www.googleapis.com/youtube/v3/search?part=snippet&type=channel&regionCode=US&maxResults=50&q="
                + keyword + "&key=" + apiKey);

        //достаем колличество
        if (pagesCount > 100){
            Scanner in = new Scanner(System.in);
            System.out.println("Please enter a pages count: ");
            pagesCount = in.nextInt();
        }

        for (int page = 1; page <= pagesCount; page++) {

            System.out.println("Searcher: Processing of " + page + " page...");
            String url;
            if(page == 1){
                url = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=channel&regionCode=US&maxResults=50&q="
                        + keyword + "&key=" + apiKey;
            } else if (pageToken != null){
                url = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=channel&regionCode=US&maxResults=50&q="
                        + keyword + "&pageToken=" + pageToken + "&key=" + apiKey;
            } else {
                break;
            }

            try {
                Document doc = Jsoup.connect(url).timeout(10000).ignoreContentType(true).get();

//                Document doc = Jsoup.connect(url)
//                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                        .referrer("none")
//                        .get();

                /*Document doc = Jsoup
                        .connect(url)
	                    .userAgent("Mozilla/5.0")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .timeout(5000).get();*/

                //Document doc = Jsoup.connect(url).timeout(0).ignoreContentType(true).userAgent("Chrome/63.0.3239.132").referrer("https://www.googleapis.com/").get();

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

                    long subscribersCount = ChannelsChecker.getSubscribersCount(channel); //сортируем по колличеству подписчиков

                    channel.setSubscribersCount(subscribersCount);

                    if (subscribersCount > 0 && subscribersCount < 1000 && !mySubscriptions.contains(channelId)) {
                        channels.add(channel);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return channels;
    }

    public static void printChannels(ArrayList<Channel> channels){
        for (Channel channel : channels) {
            String id = channel.getId();
            String title = channel.getTitle();
            long subscribersCount = channel.getSubscribersCount();
            System.out.println(title + "\ncount of subscribers = " + subscribersCount + "\nhttps://www.youtube.com/channel/" + id);
            System.out.println();
        }
        System.out.println("Channels Found: " + channels.size());
    }

    public static long getPagesCountFromUrl(String url) {

        long pages = 0;
        try {
            Document doc = Jsoup.connect(url).timeout(10000).ignoreContentType(true).get();

            String getJson = doc.text();
            JSONObject jsonObject = (JSONObject) new JSONTokener(getJson).nextValue();
            System.out.println(jsonObject);

            JSONObject pageInfo = jsonObject.getJSONObject("pageInfo");

            Long totalResults = pageInfo.getLong("totalResults");
            System.out.println("totalResults: " + totalResults);
            Long resultsPerPage = pageInfo.getLong("resultsPerPage");
            System.out.println("resultsPerPage: " + resultsPerPage);
            Long endOfDivided = totalResults % resultsPerPage;
            System.out.println("endOfDivided: " + endOfDivided);

            pages = endOfDivided > 0 ? totalResults / resultsPerPage + 1 : totalResults / resultsPerPage;
            System.out.println("pages: " + pages);

        }catch (IOException e){
            e.printStackTrace();
        }

        return pages;
    }
}