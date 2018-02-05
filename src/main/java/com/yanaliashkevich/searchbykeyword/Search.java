package com.yanaliashkevich.searchbykeyword;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by Liashkevich_Y on 05.02.2018.
 */
public class Search {
    private static final String PROPERTIES_FILENAME = "youtube.AIzaSyDxsX-11c-wqkOfRf1CmK0UuPZSD1EYYKQ";
    private static final long NUMBER_OF_VIDEOS_RETURNED = 10;

    private static YouTube youtube;

    public static void main(String[] args) {
        Properties properties = new Properties();

        try {
            InputStream in = Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);//????
            properties.load(in);
        }catch (IOException e){
            System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause() + " : " + e.getMessage());
            System.exit(1);
        }

        try {
            youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest httpRequest) throws IOException {
                }
            }).setApplicationName("youtube-cmdline-search-sample").build();

            String queryTerm = getInputQuery();

            YouTube.Search.List search = youtube.search().list("id,snippet");

            String apiKey = properties.getProperty("youtube.AIzaSyDxsX-11c-wqkOfRf1CmK0UuPZSD1EYYKQ");
            search.setKey(apiKey);
            search.setQ(queryTerm);

            search.setType("video");

            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();

            if (searchResultList != null){
                prettyPrint(searchResultList.iterator(), queryTerm);
            }

        }catch (GoogleJsonResponseException e){
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
        }catch (IOException e){
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        }catch (Throwable t){
            t.printStackTrace();
        }
    }

    private static String getInputQuery() throws IOException{
        String inputQuery = "";

        System.out.println("Please enter a search term: ");
        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        inputQuery = bReader.readLine();

        if (inputQuery.length() < 1){
            inputQuery = "cake";
        }

        return inputQuery;
    }

    private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query){
        System.out.println("\n=============================================================");
        System.out.println( "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
        System.out.println("=============================================================\n");

        if (!iteratorSearchResults.hasNext()){
            System.out.println(" There aren't any results for your query.");
        }

        while (iteratorSearchResults.hasNext()){
            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            if (rId.getKind().equals("youtube#video")){
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();

                System.out.println(" Video Id" + rId.getVideoId());
                System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
                System.out.println(" Thumbnail: " + thumbnail.getUrl());
                System.out.println("\n-------------------------------------------------------------\n");
            }
        }
    }
}
