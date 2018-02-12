package com.yanaliashkevich.searchchannels;

import com.yanaliashkevich.common.model.Channel;

import java.util.ArrayList;

/**
 * Created by Yana on 11.02.2018.
 */
public class Main {
    public static void main(String[] args) {
        ArrayList<Channel> listOfChannels = Searcher.searchChannels();
        Searcher.printChannels(listOfChannels);
    }
}
