package com.yanaliashkevich.searchchannels;

import com.yanaliashkevich.common.model.Channel;

import java.util.ArrayList;

/**
 * Created by Yana on 11.02.2018.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Start the program...");
        ArrayList<Channel> listOfChannels = Searcher.searchChannels();
        Searcher.printChannels(listOfChannels);
    }
}
