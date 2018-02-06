package com.yanaliashkevich.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Liashkevich_Y on 06.02.2018.
 */
public class PropertiesHelper {
    private static final String PROPERTIES_FILENAME = "youtube.properties";

    public Properties getProperties(){

        Properties properties = new Properties();

        try {
            InputStream in = PropertiesHelper.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
            properties.load(in);
        }catch (IOException e){
            System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause() + " : " + e.getMessage());
            System.exit(1);
        }
        return properties;
    }
}
