package com.yanaliashkevich.common.model;

/**
 * Created by Yana on 08.02.2018.
 */
public class Channel {
    private String title;
    private String id;
//
//    public Channel(String title, String id){
//
//    }

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
