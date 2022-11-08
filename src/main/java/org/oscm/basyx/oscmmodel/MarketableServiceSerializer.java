package org.oscm.basyx.oscmmodel;

import com.google.gson.Gson;

public class MarketableServiceSerializer {

    public static String toJson(MarketableServiceInfo serviceInfo) {
        Gson gson = new Gson();
        return gson.toJson(serviceInfo);
    }

    public static MarketableServiceInfo fromJson(String json) {
        Gson gson = new Gson();
        // Convert JSON File to Java Object
        MarketableServiceInfo serviceInfo = gson.fromJson(json, MarketableServiceInfo.class);
        return serviceInfo;
    }
}
