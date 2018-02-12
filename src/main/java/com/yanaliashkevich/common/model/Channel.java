package com.yanaliashkevich.common.model;

/**
 * Created by Yana on 08.02.2018.
 */
public class Channel {
    private String title;
    private String id;

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    private long viewCount;

    public long getSubscribersCount() {
        return subscribersCount;
    }

    public void setSubscribersCount(long subscribersCount) {
        this.subscribersCount = subscribersCount;
    }



    private long subscribersCount;

    public String getTitle(){
        return title;
    }

    public String getId(){
        return id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setId(String id){
        this.id = id;
    }

}
